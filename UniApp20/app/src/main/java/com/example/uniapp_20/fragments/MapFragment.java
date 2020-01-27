package com.example.uniapp_20.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.uniapp_20.R;
import com.example.uniapp_20.SBActivities.MapFilterActivity;

import com.example.uniapp_20.internet.AppController;
import com.example.uniapp_20.utils.FrontEngine;
import com.example.uniapp_20.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;

    private HashMap<String, JSONObject> locationsSb = new HashMap();

    private HashMap<String, JSONObject> locationsHom = new HashMap();

    private static final LatLng HOMBURG_OVERLAY_POSITION = new LatLng(49.3072, 7.3441);

    private static final LatLng SB_OVERLAY_POSITION = new LatLng(49.255, 7.0427);

    private static final LatLng CAMERA_POSITION_SB = new LatLng(49.255, 7.0433);

    private static final LatLng CAMERA_POSITION_HOM = new LatLng(49.30718, 7.3448);


    private SimpleCursorAdapter mAdapter;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SessionManager.getInstance(getContext()).setScreen("MapFragment");


        final String[] from = new String[]{"buildingName"};
        final int[] to = new int[]{android.R.id.text1};

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.map_search_background,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        menu.findItem(R.id.action_events).setVisible(false);
        menu.findItem(R.id.activity_search).setVisible(true);

        SearchView searchView = (SearchView) menu.findItem(R.id.activity_search).getActionView();

        if (SessionManager.getInstance(getContext()).isSB()) {
            searchView.setQueryHint("Suche Gebäude z.B. E13");
        } else {
            searchView.setQueryHint("Suche");
        }

        searchView.setSuggestionsAdapter(mAdapter);

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {


                Cursor cursor = (Cursor) mAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex("buildingName"));
                String[] s = txt.split("\\s+");
                String key = s[0];

                if (SessionManager.getInstance(getContext()).isSB()) {
                    changeCameraPosition(Objects.requireNonNull(locationsSb.get(key)));
                } else {
                    changeCameraPosition(Objects.requireNonNull(locationsHom.get(key)));
                }
                searchView.setQuery(txt, true);


                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                populateAdapter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                Log.d("Text has changed",newText);
                populateAdapter(newText);
                return true;
            }

        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    private void changeCameraPosition(JSONObject jsonObject) {

        String lat = "";
        String lon = "";
        try {

            if (jsonObject.has("latitude") && jsonObject.has("longitude")) {
                lat = (String) jsonObject.get("latitude");
                lon = (String) jsonObject.get("longitude");
            }

            float latInt = Float.parseFloat(lat);
            float lonInt = Float.parseFloat(lon);

            InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity())
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            // check if no view has focus:
            View currentFocusedView = getActivity().getCurrentFocus();
            if (currentFocusedView != null) {
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }


            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latInt, lonInt))
                    .title(jsonObject.getString("name")));

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .bearing(googleMap.getCameraPosition().bearing)
                    .target(new LatLng(latInt, lonInt))
                    .tilt(0)
                    .zoom((float) 17)
                    .build()
            ));

        } catch (JSONException ignored) {

        }


    }

    private void populateAdapter(String query) {

        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "buildingName"});
        int i = 0;

        HashMap<String, JSONObject> locations;

        if (SessionManager.getInstance(getContext()).isSB()) {
            locations = locationsSb;

        } else {
            locations = locationsHom;
        }

        for (String s : locations.keySet()) {

            try {
                if (s.toLowerCase().startsWith(query.toLowerCase())) {
                    JSONObject building = locations.get(s);
                    if (building.has("function")) {
                        c.addRow(new Object[]{i, s + "  " + building.get("function")});
                    } else {
                        c.addRow(new Object[]{i, s});
                    }
                    i++;
                }
            } catch (JSONException e) {
                continue;
            }
        }
        mAdapter.changeCursor(c);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.map_content, container, false);
        Objects.requireNonNull(getActivity()).setTitle("Map");

        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;


            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    googleMap.setMyLocationEnabled(true);
                }
            }


            //unable useful features...
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);

            //Check if in Map Settings Saarbrücken is selected otherwise Homburg is shown...
            if (SessionManager.getInstance(getContext()).isSB()) {

                BitmapDescriptor SB_OVERLAY = BitmapDescriptorFactory.fromResource(R.drawable.campus_sb3);

                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .bearing((float) -7.5)
                        .target(CAMERA_POSITION_SB)
                        .tilt(0)
                        .zoom((float) 14.57)
                        .build()
                ));


                GroundOverlayOptions uniMap = new GroundOverlayOptions().image(SB_OVERLAY).position(SB_OVERLAY_POSITION, 1460f, 1000f);

                //newarkMap.transparency((float) 0.6);
                uniMap.bearing((float) -7.5);

                // Add an overlay to the map, retaining a handle to the GroundOverlay object.
                googleMap.addGroundOverlay(uniMap);

            } else {

                BitmapDescriptor HOM_OVERLAY = BitmapDescriptorFactory.fromResource(R.drawable.homburg_map);

                //Rotate camera 180 degree since the Homburg map is upside down
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .bearing((float) 181)
                        .target(CAMERA_POSITION_HOM)
                        .tilt(0)
                        .zoom((float) 15.23)
                        .build()
                ));


                GroundOverlayOptions uniMap = new GroundOverlayOptions().image(HOM_OVERLAY).position(HOMBURG_OVERLAY_POSITION, 1100f, 1280f);

                //Rotate overlay because the map is upside down
                uniMap.bearing((float) 181);
                // Add an overlay to the map, retaining a handle to the GroundOverlay object.
                googleMap.addGroundOverlay(uniMap);


            }
            loadMapBuildings();


        });


        return view;
    }

    private void loadMapBuildings() {

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                FrontEngine.getInstance().get_map_url(), null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject building = response.getJSONObject(i);
                            if (building.has("name")) {
                                if (building.has("campus")) {
                                    String name = building.getString("name");
                                    if (building.get("campus").equals("saar")) {
                                        locationsSb.put(name.split("\\s+")[0], building);
                                    } else {
                                        locationsHom.put(name.split("\\s+")[0], building);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> VolleyLog.d(TAG, "Error: " + error.getMessage()));

        AppController.getInstance().addToRequestQueue(jsonObjReq, FrontEngine.getInstance().tag_json_obj);
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("")
                        .setMessage("")
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(getActivity(), new String[]
                                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_favorite) {
            return false;
        }// Do onlick on menu action here
        startActivity(new Intent(getActivity(), MapFilterActivity.class));
        Objects.requireNonNull(getActivity()).finish();
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }

            }
        }
    }

}







