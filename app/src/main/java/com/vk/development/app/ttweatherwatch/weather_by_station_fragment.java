package com.vk.development.app.ttweatherwatch;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vikash on 05/01/2016.
 */
public class weather_by_station_fragment extends Fragment implements OnMapReadyCallback{


    GoogleMap googleMap;
    private static final String JSON_URL = "http://192.168.8.100/test_android.php";
    private static String marker_string = "";
    private String ret = "hi";
    View rootview;

    @Override
    @Nullable

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.weather_by_station_fragment, container, false);

        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        return rootview;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        LatLng Trinidad_Tobago = new LatLng(10.691803, -61.222503);


        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context1 = getActivity();
                LayoutInflater inflater = (LayoutInflater)context1.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);

                View v = inflater.inflate(R.layout.info_window, null);

                TextView myLocality = (TextView) v.findViewById(R.id.mylocality);
                TextView Temperature = (TextView) v.findViewById(R.id.Temperature);
                TextView mySnippet = (TextView) v.findViewById(R.id.mysnippet);

                myLocality.setText(marker.getTitle());
                Temperature.setText(getJSON(JSON_URL));
                mySnippet.setText(marker.getSnippet());
                return v;
            }

        });

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Trinidad_Tobago, 9));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("")
                .position(Trinidad_Tobago));

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
    }

    private String getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // loading = ProgressDialog.show(MainActivity.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                String ret="";
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString();

                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                marker_string = s.replace("<br />","");
                marker_string.replace("<br />","");
                //loading.dismiss();
                //Temperature.setText(Html.fromHtml(s));
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
        return marker_string;
    }

}




