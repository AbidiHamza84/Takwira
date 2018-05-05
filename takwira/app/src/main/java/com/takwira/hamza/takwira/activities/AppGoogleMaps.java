package com.takwira.hamza.takwira.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.takwira.hamza.takwira.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by hamza on 17/08/17.
 */

public abstract class AppGoogleMaps extends AppCompatActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener , GoogleMap.OnMarkerClickListener , GoogleMap.OnCameraChangeListener

     ,GoogleMap.InfoWindowAdapter   {

    protected GoogleMap mMap;
    private LocationManager locationManager;
    private static Geocoder geoCoder;
    private Toolbar toolbar;
    private boolean move_animate;
    protected String userCity;
    protected String visibleCity;
    private boolean visibleCityChanged = false ;
    private Camera camera ;
    protected Marker tmpMarker ;
    protected TextView markerTitle ;
    protected TextView markerSnippet ;
    protected RatingBar markerRatingBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourcesId());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initMap();

    }

    protected void initMap() {
        move_animate = true ;
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(getMapResourceId());
        map.getMapAsync(this);
    }

    protected abstract int getMapResourceId();

    /**
     * get the toolbar of map
     * @return
     */
    protected Toolbar getToolbar(){
        return toolbar ;
    }
    protected abstract int getLayoutResourcesId() ;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(geoCoder == null)
            geoCoder = new Geocoder(getApplicationContext());

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        mMap = googleMap;

        moveCameraToUserLocation();

        mMap.setOnMapLongClickListener(this);

        mMap.setOnMapClickListener(this);

        mMap.setOnCameraChangeListener(this);
        
        initializeData();

        mMap.setInfoWindowAdapter(this);

        //mode d'affichage de la carte
        mMap.setTrafficEnabled(true);
    }

    protected abstract void initializeData();

    protected void moveCameraToUserLocation() {

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = null;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            mMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,this);
            location = locationManager.getLastKnownLocation(provider);
        }
        if(location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = createCamera(userLocation);
            userCity = getCityFromLatLing(userLocation);
            visibleCity = userCity;
            if(move_animate == true) {
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                move_animate = false;
            }
            else
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    protected CameraPosition createCamera(LatLng latLing){
        CameraPosition camera = new CameraPosition.Builder()
                .target(latLing)
                .zoom(15)
                .build();
        return camera ;
    }


    public static List<Address> getAddressFromLocationName(String address , Geocoder geocoder){
        try {
            return geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }
    protected List<Address> getAddressFromLocationName(String address){
        if(geoCoder == null)
            geoCoder = new Geocoder(getApplicationContext());
        try {
            return geoCoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static List<Address> getAddressFromLatLing(LatLng latLng , Geocoder geocoder){
        try {
            return geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    protected List<Address> getAddressFromLatLing(LatLng latLng){
        if(geoCoder == null)
            geoCoder = new Geocoder(getApplicationContext());
        try {
            return geoCoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static String getCityFromLatLing(LatLng latLong , Geocoder geocoder){
        List<Address> adresse = getAddressFromLatLing(latLong,geocoder);
        try {
            return adresse.get(0).getLocality();
        }
        catch (Exception e){
            Log.e("getCityFromLatLing",e.toString());
        }
        return null ;
    }

    protected String getCityFromLatLing(LatLng latLong){
        if(geoCoder == null)
            geoCoder = new Geocoder(getApplicationContext());
        List<Address> adresse = getAddressFromLatLing(latLong);
        try {
            return adresse.get(0).getLocality();
        }
        catch (Exception e){
            Log.e("getCityFromLatLing",e.toString());
        }
        return null ;
    }

    protected String getUserCity() {
        return userCity;
    }

    protected void searchAddress(String address) {
        List<Address> adresse = getAddressFromLocationName(address);
        if(tmpMarker != null)
            tmpMarker.remove();

        if (adresse != null && adresse.size() > 0) {
            LatLng latLing = new LatLng(adresse.get(0).getLatitude(), adresse.get(0).getLongitude());
            tmpMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLing)
                    .title(address));
            tmpMarker.showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(createCamera(latLing)));
            mMap.setOnMarkerClickListener(this);

        }
        else{
            Toast.makeText(this,getResources().getString(R.string.wrong_address),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if(tmpMarker != null)
            tmpMarker.remove();
        try {
            tmpMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(geoCoder.getFromLocation(latLng.latitude,latLng.longitude,1).get(0).getAddressLine(0)));
            tmpMarker.showInfoWindow();
            mMap.setOnMarkerClickListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(tmpMarker != null)
            tmpMarker.remove();
    }

    @Override
    public void onLocationChanged(Location location) {

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

    protected LatLngBounds getVisibleRegionBounds (){
        return mMap.getProjection().getVisibleRegion().latLngBounds;
    }

    protected boolean isVisibleRegionContains(LatLng point){
        return getVisibleRegionBounds().contains(point);
    }

    protected abstract void showMarkersOfThisRegion();

    protected String getVisibleCityName (){
        return getCityFromLatLing(mMap.getCameraPosition().target);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        showMarkersOfThisRegion();
    }

    protected boolean isCityChanged(){
        return visibleCityChanged ;
    }


    @Override
    public View getInfoWindow(Marker arg0) {
        View v = getLayoutInflater().inflate(R.layout.info_window_marker, null);
        markerTitle = (TextView) v.findViewById(R.id.markerTitle);
        markerSnippet = (TextView) v.findViewById(R.id.markerSnippet);
        markerRatingBar= (RatingBar) v.findViewById(R.id.markerRatingBar);
        setInfoWindowContents(arg0);
        return v;
    }

    protected abstract void setInfoWindowContents(Marker marker);

    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }


}