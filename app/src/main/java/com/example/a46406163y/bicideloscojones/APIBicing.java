package com.example.a46406163y.bicideloscojones;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class APIBicing {


    private static final String BASE_URL = "http://wservice.viabicing.cat/v2/stations";


    static ArrayList<Bicing> getInfoStations() {
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .build();
        String url = builtUri.toString();

        return doCall(url);

    }

    @Nullable
    private static ArrayList<Bicing> doCall(String url){
        try {
            String JsonResponse = HttpUtils.get(url);
            ArrayList<Bicing> bicing = new ArrayList<>();

            JSONObject data = new JSONObject(JsonResponse);
            JSONArray jsonBoss = data.getJSONArray("stations");

            for (int i = 0; i < jsonBoss.length(); i++) {
                Bicing bici = new Bicing();
                JSONObject object = jsonBoss.getJSONObject(i);
                bici.setId(object.getInt("id"));
                bici.setType(object.getString("type"));
                bici.setLat(object.getDouble("latitude"));
                bici.setLon(object.getDouble("longitude"));
                bici.setStName(object.getString("streetName"));
                bici.setRanuras(object.getInt("slots"));
                bici.setNbike(object.getInt("bikes"));

                bicing.add(bici);
            }

            return bicing;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

