package org.fossasia.openevent.models;

/**
 * Created by rohanagarwal94 on 10/8/16.
 */
public class GetMarkerFromString {
    private double lat;
    private double lng;
    private String name;

    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
