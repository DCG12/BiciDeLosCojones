package com.example.a46406163y.bicideloscojones;

/**
 * Created by 46406163y on 10/02/17.
 */
public class Bicing {

    private int id;
    private String type;
    private double lat;
    private double lon;
    private String StName;
    private int Nbike;
    private String nearbyStations;
    private int ranuras;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getStName() {
        return StName;
    }

    public void setStName(String stName) {
        StName = stName;
    }

    public int getNbike() {
        return Nbike;
    }

    public void setNbike(int nbike) {
        Nbike = nbike;
    }

    public String getNearbyStations() {
        return nearbyStations;
    }

    public void setNearbyStations(String nearbyStations) {
        this.nearbyStations = nearbyStations;
    }

    public int getRanuras() {
        return ranuras;
    }

    public void setRanuras(int ranuras) {
        this.ranuras = ranuras;
    }

    @Override
    public String toString() {
        return "Bicing{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", StName='" + StName + '\'' +
                ", Nbike=" + Nbike +
                ", nearbyStations='" + nearbyStations + '\'' +
                ", ranuras=" + ranuras +
                '}';
    }
}
