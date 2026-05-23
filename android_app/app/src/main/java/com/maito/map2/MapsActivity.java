package com.maito.map2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMapInstance;
    private RequestQueue networkQueue;
    
    // Remplacez par l'adresse IP locale de votre machine exécutant XAMPP
    private final String API_FETCH_URL = "http://192.168.1.X/MAITO-S-MAP2/backend/fetch_locations_api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        networkQueue = Volley.newRequestQueue(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMapInstance = googleMap;
        fetchAndDisplayMarkers();
    }

    private void fetchAndDisplayMarkers() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_FETCH_URL, null,
                response -> {
                    try {
                        JSONArray positionsArray = response.getJSONArray("positions");
                        LatLng lastPosition = null;

                        for (int i = 0; i < positionsArray.length(); i++) {
                            JSONObject posObject = positionsArray.getJSONObject(i);
                            double lat = posObject.getDouble("lat");
                            double lon = posObject.getDouble("lon");
                            String time = posObject.getString("recorded_time");

                            LatLng point = new LatLng(lat, lon);
                            lastPosition = point;

                            googleMapInstance.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title("Relevé à " + time));
                        }

                        if (lastPosition != null) {
                            googleMapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, 10f));
                        } else {
                            Toast.makeText(this, "Historique vide", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show()
        );

        networkQueue.add(request);
    }
}
