package org.fossasia.openevent.models;

/**
 * Created by rohanpc on 2/3/2016.
 */

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class MarkerItem implements ClusterItem {
    private final LatLng mPosition;
    private String title;
    private String subtitle;
    private float floor;

    public MarkerItem(double lat, double lng, String stitle, String ssubtitle, float sfloor ) {

        mPosition = new LatLng(lat, lng);
        title=stitle;
        subtitle=ssubtitle;
        floor=sfloor;

    }

    public String getTitle() {
        return title;
    }

    public String getsubtitle() {
        return subtitle;
    }

    public float getfloor() {
        return floor;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}

