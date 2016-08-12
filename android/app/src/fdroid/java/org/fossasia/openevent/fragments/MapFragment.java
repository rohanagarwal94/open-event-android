package org.fossasia.openevent.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.IndoorLevel;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.fossasia.openevent.R;
import org.fossasia.openevent.utils.MyClusterRenderer;
import org.fossasia.openevent.models.MarkerItem;
import org.fossasia.openevent.models.getMarkerFromString;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    private static final int REQUEST_MAP_LOCATION = 100;
    private static final double DESTINATION_LATITUDE = 52.52433;
    private static final double DESTINATION_LONGITUDE = 13.389893;
    private static final int MAXLEVEL=3;

    private AutoCompleteTextView actv;
    private List<MarkerItem> markerItems = new ArrayList<>();
    private String jsonString;
    private ArrayList<getMarkerFromString> markerList = new ArrayList<>();
    private List<String> actvItems = new ArrayList<>();
    private GoogleMap mMap;
    private ClusterManager<MarkerItem> clusterManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        actv = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpMapIfNeeded();

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment supportMapFragment = ((SupportMapFragment)
                    getChildFragmentManager().findFragmentById(R.id.map));
            supportMapFragment.getMapAsync(this);

//             Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void initCamera() {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(DESTINATION_LATITUDE, DESTINATION_LONGITUDE))
                .zoom(18f)
                .bearing(0.0f)
                .tilt(40f)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), null);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.setOnIndoorStateChangeListener(new GoogleMap.OnIndoorStateChangeListener() {
            @Override
            public void onIndoorBuildingFocused() {
                if(mMap.getFocusedBuilding()==null)
                {
                    clusterManager.clearItems();
                    clusterManager.addItems(markerItems);
                    clusterManager.setRenderer(new MyClusterRenderer(getActivity(), mMap, clusterManager));
                }
            }

            @Override
            public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {
                if (indoorBuilding != null) {
                    IndoorLevel level =
                            indoorBuilding.getLevels().get(indoorBuilding.getActiveLevelIndex());
                    if (level != null) {
                        clusterManager.clearItems();
                        int currentLevel = MAXLEVEL-indoorBuilding.getActiveLevelIndex();
                        List<MarkerItem> currentLevelMarkerItems = new ArrayList<>();
                        for (MarkerItem markerItem : markerItems) {
                            if (markerItem.getfloor() == currentLevel) {
                                currentLevelMarkerItems.add(markerItem);
                            }
                        }
                        clusterManager.addItems(currentLevelMarkerItems);
                        clusterManager.setRenderer(new MyClusterRenderer(getActivity(), mMap, clusterManager));
                    }
                }
                else
                {
                    clusterManager.clearItems();
                    clusterManager.addItems(markerItems);
                    clusterManager.setRenderer(new MyClusterRenderer(getActivity(), mMap, clusterManager));
                }
            }
        });


    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_MAP_LOCATION);
        }

        initCamera();
        clusterManager = new ClusterManager<>(getActivity(), mMap);
        mMap.setOnCameraChangeListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

        clusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MarkerItem>() {
                    @Override
                    public boolean onClusterClick(final Cluster<MarkerItem> cluster) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                cluster.getPosition(), (float) Math.floor(mMap
                                        .getCameraPosition().zoom + 2)), 300,
                                null);

                        return true;
                    }
                });

        //Parsing the local JSON file.

        try {
            InputStream inputstream = getResources().getAssets().open("map.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
            jsonString = reader.toString();
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                total.append(line);
            }
            jsonString = total.toString();
        } catch (IOException ex) {
            Toast.makeText(getActivity(), getResources().getString(R.string.failure_json), Toast.LENGTH_SHORT).show();
        }
        new Thread(){
            @Override
            public void run() {

                try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.optJSONArray("map");
            int arraylength = jsonArray.length();

            for (int i = 0; i < arraylength; i++) {

                JSONObject jsonChildNode = jsonArray.getJSONObject(i);
                String title = jsonChildNode.optString("name");
                String subtitle = jsonChildNode.optString("room");
                int floor = jsonChildNode.optInt("floor");
                double lat1 = jsonChildNode.optDouble("latitude");
                double lng1 = jsonChildNode.optDouble("longitude");
                markerItems.add(new MarkerItem(lat1, lng1, title, subtitle, floor));
                actvItems.add(title);
                getMarkerFromString user = new getMarkerFromString();
                user.setLat(lat1);
                user.setLng(lng1);
                user.setName(title);
                markerList.add(user);

            }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
            clusterManager.addItems(markerItems);
            clusterManager.setRenderer(new MyClusterRenderer(getActivity(), mMap, clusterManager));
            clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
                @Override
                public boolean onClusterItemClick(MarkerItem markerItem) {
                    return false;
                }
            });
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
            }
        }.start();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, actvItems);
        actv.setAdapter(adapter);

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //... your stuff
                View scene = getActivity().getCurrentFocus();
                if (scene != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(scene.getWindowToken(), 0);
                }
                String s = parent.getItemAtPosition(position).toString();
                int pos=actvItems.indexOf(s);
                LatLng lng = new LatLng(markerList.get(pos).getLat(), markerList.get(pos).getLng());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lng,
                        (float) Math.floor(mMap.getCameraPosition().zoom + 8)));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        switch (reqCode) {
            case REQUEST_MAP_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
