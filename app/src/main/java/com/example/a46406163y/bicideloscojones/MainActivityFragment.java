package com.example.a46406163y.bicideloscojones;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import org.osmdroid.events.MapEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.overlays.Marker;
import java.util.ArrayList;

;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private View view;
    private MapView map;
    private MyLocationNewOverlay myLocationOverlay;
    private MinimapOverlay mMinimapOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private CompassOverlay mCompassOverlay;
    private IMapController mapController;
    private RadiusMarkerClusterer parkingMarkers;
    private ArrayList<Bicing> bicings;



    public MainActivityFragment() {
    }

    @Override//notificamos al activity quer le añadimos items al menu
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);

        map = (MapView) view.findViewById(R.id.map);

        initializeMap();
        setZoom();
        setOverlays();
        putMarkers();
        map.invalidate();

        return view;
    }

    private void putMarkers() {


        setupMarkerOverlay();
        if (bicings != null) {
            Log.d("LOG", bicings.get(0).toString());
            for (Bicing bici : bicings) {
                Marker marker = new Marker(map);

                GeoPoint point = new GeoPoint(
                        bici.getLat(),
                        bici.getLon()
                );

                Log.d("DEBUG", bici.getStName());

                marker.setPosition(point);

                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                String tipobici = bici.getType();

                if(tipobici.equals("BIKE")){
                    marker.setIcon(getResources().getDrawable(R.drawable.bike4));
                }
               else{
                    marker.setIcon(getResources().getDrawable(R.drawable.bike8));
                }
                marker.setTitle(bici.getStName());
                marker.setSnippet(String.valueOf("Bicicletas disponibles. "  + bici.getNbike()) +"  Ranuras disponibles: " + String.valueOf(bici.getRanuras()));

                marker.setAlpha(0.6f);

                parkingMarkers.add(marker);
                parkingMarkers.invalidate();
                map.invalidate();

            }
        }
    }

    private void setupMarkerOverlay() {
        parkingMarkers = new RadiusMarkerClusterer(getContext());
        map.getOverlays().add(parkingMarkers);

        Drawable clusterIconD = getResources().getDrawable(R.drawable.index);
        Bitmap clusterIcon = ((BitmapDrawable)clusterIconD).getBitmap();

        parkingMarkers.setIcon(clusterIcon);
        parkingMarkers.setRadius(100);
    }

    private void initializeMap() {
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.setTilesScaledToDpi(true);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
    }

    private void setZoom() {
        //  Setteamos el zoom al mismo nivel y ajustamos la posición a un geopunto
        mapController = map.getController();
        mapController.setZoom(14);

    }

    private void setOverlays() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();

        myLocationOverlay = new MyLocationNewOverlay(
                getContext(),
                new GpsMyLocationProvider(getContext()),
                map
        );
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapController.animateTo( myLocationOverlay
                        .getMyLocation());
            }
        });

        /*
        mMinimapOverlay = new MinimapOverlay(getContext(), map.getTileRequestCompleteHandler());
        mMinimapOverlay.setWidth(dm.widthPixels / 5);
        mMinimapOverlay.setHeight(dm.heightPixels / 5);
*/

        mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

        mCompassOverlay = new CompassOverlay(
                getContext(),
                new InternalCompassOrientationProvider(getContext()),
                map
        );

        mCompassOverlay.enableCompass();

        map.getOverlays().add(myLocationOverlay);
        //map.getOverlays().add(this.mMinimapOverlay);
        map.getOverlays().add(this.mScaleBarOverlay);
        map.getOverlays().add(this.mCompassOverlay);
    }

    @Override
    public void onStart(){
        super.onStart();
        RefreshDataTask task = new RefreshDataTask();
        task.execute();
        //Log.d("LOG", datosEstaciones.get(0).toString());
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            RefreshDataTask task = new RefreshDataTask();
            task.execute();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            APIBicing api = new APIBicing();
            bicings = api.getInfoStations();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            putMarkers();
        }

        }
}
