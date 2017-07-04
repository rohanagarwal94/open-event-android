package org.fossasia.openevent.utils.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.fossasia.openevent.R;

/**
 * Created by rohanagarwal94 on 4/7/17.
 */
public class ClusterRenderer extends DefaultClusterRenderer<MicrolocationClusterWrapper> {

    private Context context;

    public ClusterRenderer(Context context, GoogleMap map,
                             ClusterManager<MicrolocationClusterWrapper> clusterManager) {
        super(context, map, clusterManager);
        this.context=context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MicrolocationClusterWrapper item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        markerOptions.title(item.getMicrolocation().getName());
        markerOptions.icon(ImageUtils.vectorToBitmap(context, R.drawable.map_marker, R.color.dark_grey));
    }

    @Override
    protected void onClusterItemRendered(final MicrolocationClusterWrapper clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        clusterItem.setMarker(marker);
   }
}
