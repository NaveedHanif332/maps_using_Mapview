package com.example.mapssamples;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Map_Activity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap mgoogleMap;
    SearchView searchView;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_);
//

        mapView = findViewById(R.id.map);
        searchView = findViewById(R.id.search_place);
        //check for services
        //check for permissions
        //check for gps enable if current location is used


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 999);
        }


    }

    private void go_to_map() {
        mapView.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap = googleMap;
        mgoogleMap.getUiSettings().setZoomControlsEnabled(true);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mgoogleMap.clear();
                mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15));
                mgoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                //getting the latlag
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastknownlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng mylocation=new LatLng(lastknownlocation.getLatitude(),lastknownlocation.getLongitude());
                    mgoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    mgoogleMap.addMarker(new MarkerOptions().position(mylocation));
                    mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation,15));
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener,null);
            mgoogleMap.setMyLocationEnabled(true);
            //search view
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    if(s!=null && !s.isEmpty())
                    {
                        Geocoder geocoder=new Geocoder(getApplicationContext());
                        try {
                            List<Address> list_for_address=geocoder.getFromLocationName(s,1);
                            if(list_for_address!=null &&  !list_for_address.isEmpty()) {
                                Address address = list_for_address.get(0);
                                LatLng sear_latlng=new LatLng(address.getLatitude(),address.getLongitude());
                                mgoogleMap.addMarker(new MarkerOptions().position(sear_latlng)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                                mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sear_latlng,15));
                                // mgoogleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(address.getLatitude()-.01,address.getLongitude()-01),new LatLng(address.getLatitude()+.01,address.getLongitude()+01)));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //requesting
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    go_to_map();
                }
            } else {
                Toast.makeText(this, "location is needed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}