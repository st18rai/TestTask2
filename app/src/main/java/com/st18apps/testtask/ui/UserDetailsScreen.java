package com.st18apps.testtask.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.st18apps.testtask.App;
import com.st18apps.testtask.R;
import com.st18apps.testtask.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsScreen extends FragmentActivity implements OnMapReadyCallback {

    private TextView name;
    private TextView email;
    private TextView phone;
    private WebView webView;
    private List<User> userList;
    private GoogleMap mMap;
    private double userLat;
    private double userLng;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_screen);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        name = findViewById(R.id.textViewDetailName);
        email = findViewById(R.id.textViewDetailEmail);
        phone = findViewById(R.id.textViewDetailPhone);
        webView = findViewById(R.id.webView);

        userList = new ArrayList<>();
        userList = App.getInstance().getUserList();

        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);

        String url = "http://" + userList.get(position).getWebsite();

        name.setText(userList.get(position).getName());
        email.setText(userList.get(position).getEmail());
        phone.setText(userList.get(position).getPhone());


        if (!userList.get(position).getWebsite().isEmpty()) {
            Log.e("TAG", "onCreate: " + url);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setUserAgentString("Android WebView");
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            webView.loadUrl(url);
        }

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail(email.getText().toString());
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber(phone.getText().toString());
            }
        });

        userLat = Double.parseDouble(userList.get(position).getAddress().getGeo().getLat());
        userLng = Double.parseDouble(userList.get(position).getAddress().getGeo().getLng());

        Log.e("TAG", "onCreateMap: " + "lat: " + userLat + " lng: " + userLng);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng userLocation = new LatLng(userLat, userLng);
        mMap.addMarker(new MarkerOptions().position(userLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 5));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.setTitle(userLocation.toString());
                return false;
            }
        });
    }

    //email intent
    public void composeEmail(String address) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Test");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //phone intent
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
