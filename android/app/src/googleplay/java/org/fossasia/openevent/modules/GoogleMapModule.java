package org.fossasia.openevent.modules;

import android.support.v4.app.Fragment;

import org.fossasia.openevent.fragments.MapFragment;

/**
 * User: mohit
 * Date: 13/6/15
 */
public class GoogleMapModule implements MapModule {
    /**
     * This guy should not really cache anything
     */
    @Override
    public Fragment provideMapFragment() {
        return new MapFragment();
    }
}
