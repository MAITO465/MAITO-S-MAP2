package com.maito.map2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int PERM_REQUEST_CODE = 300;
    private TextView tvLat, tvLon;
    private RequestQueue networkQueue;
    private LocationManager locManager;
    
    // Remplacez par l'adresse IP locale de votre machine exécutant XAMPP
    private final String API_INSERT_URL = "http://192.168.1.X/MAITO-S-MAP2/backend/save_location_api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLat = findViewById(R.id.tvLat);
        tvLon = findViewById(R.id.tvLon);
        Button btnMap = findViewById(R.id.btnMap);

        networkQueue = Volley.newRequestQueue(this);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnMap.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapsActivity.class)));

        requestPermissionsAndTrack();
    }

    private void requestPermissionsAndTrack() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE
            }, PERM_REQUEST_CODE);
        } else {
            initLocationTracking();
        }
    }

    @SuppressLint("MissingPermission")
    private void initLocationTracking() {
        try {
            locManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    60000,
                    150,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            tvLat.setText("Latitude: " + lat);
                            tvLon.setText("Longitude: " + lon);

                            pushLocationToServer(lat, lon);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushLocationToServer(double lat, double lon) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, API_INSERT_URL,
                response -> Toast.makeText(MainActivity.this, "Position envoyée au serveur", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(MainActivity.this, "Erreur serveur", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String deviceId = getDeviceID();

                params.put("latitude", String.valueOf(lat));
                params.put("longitude", String.valueOf(lon));
                params.put("date", currentTime);
                params.put("imei", deviceId);

                return params;
            }
        };

        networkQueue.add(postRequest);
    }

    private String getDeviceID() {
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId != null && !androidId.isEmpty()) return androidId;

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null && tm.getDeviceId() != null) return tm.getDeviceId();
            }
        } catch (Exception ignored) {}

        return "UNKNOWN_USER";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initLocationTracking();
        } else {
            Toast.makeText(this, "Permission GPS requise", Toast.LENGTH_LONG).show();
        }
    }
}
