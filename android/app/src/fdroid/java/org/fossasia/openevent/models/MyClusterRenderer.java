package org.fossasia.openevent.models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by rohanagarwal94 on 11/8/16.
 */
public class MyClusterRenderer extends DefaultClusterRenderer<MarkerItem> {

    public MyClusterRenderer(Context context, GoogleMap map,
                             ClusterManager<MarkerItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(MarkerItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        markerOptions.title(item.getTitle());
        markerOptions.snippet(item.getsubtitle());
    }

    @Override
    protected void onClusterItemRendered(final MarkerItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
    }



}
