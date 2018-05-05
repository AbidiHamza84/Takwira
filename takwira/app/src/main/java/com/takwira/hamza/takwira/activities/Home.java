package com.takwira.hamza.takwira.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.takwira.hamza.takwira.AsyncTasks.BitmapDownloader;
import com.takwira.hamza.takwira.AsyncTasks.Logout;
import com.takwira.hamza.takwira.R;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableTerrain;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableUser;
import com.takwira.hamza.takwira.listview.Card;
import com.takwira.hamza.takwira.listview.CardArrayAdapter;
import com.takwira.hamza.takwira.marker_display.MarkerDisplay;
import com.takwira.hamza.takwira.marker_display.MarkerTerrainsDisplay;
import com.takwira.hamza.takwira.objects.Terrain;
import com.takwira.hamza.takwira.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

//import com.takwira.hamza.takwira.recycle_view.CardViewContents;
//import com.takwira.hamza.takwira.recycle_view.RVAdapter;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , RecyclerView.OnItemTouchListener ,OnMapReadyCallback, LocationListener, GoogleMap.OnCameraChangeListener , GoogleMap.OnMarkerClickListener , AbsListView.OnScrollListener , GoogleMap.OnMapClickListener {
    private User user;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Bundle mapViewBundle = null;
    private TextView userName;
    private TextView userMail;
    private ImageView userPicture;
    private MapView background;
    GoogleMap mGoogleMap;
    LocationManager locationManager;
    String provider;
    String userCity ;
    private boolean permission ;
    private int lastTopValue = 0;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private TableTerrain tableTerrain;
    private MarkerDisplay markerDisplay;
    private Rect rect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        checkLocationPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.lv);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.cardview_activity) {
            @Override
            public void onRowClick(int position) {
                switch (position){
                    case 0 :Intent intent = new Intent(getContext(),CreateParty.class);
                        finish();
                        startActivity(intent);
                        break;
                    case 1 :
                        break;
                }
            }
        };


            Card card = new Card("Organiser une partie", "");
            cardArrayAdapter.add(card);

            card = new Card("Terrains favoris", "");
            cardArrayAdapter.add(card);

            card = new Card("Amis", "");
            cardArrayAdapter.add(card);

            card = new Card("Nous contacter", "");
            cardArrayAdapter.add(card);

        listView.setAdapter(cardArrayAdapter);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_home, listView, false);
        listView.addHeaderView(header, null, false);


        background = (MapView) findViewById(R.id.mapViewHeader);


        initiliserMapView();

        listView.setOnScrollListener(this);




        //recuperation des données du user connecté
        Intent intent = getIntent();
        user = (User) intent.getExtras().get("user");

        updataData();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        initialiserHeaderView();



    }

    private void initialiserHeaderView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        userPicture = headerView.findViewById(R.id.userPicture);

        Bitmap image = null;
        BitmapDownloader bmm = new BitmapDownloader(this);
//        Log.i("picture_google", user.getpictureUrl());
        bmm.execute(user.getpictureUrl());
        try {
            image = (Bitmap) bmm.get();
            userPicture.setImageBitmap(image);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        userName = headerView.findViewById(R.id.userName);
        userName.setText(user.getFirstName() + " " + user.getLastName());

        userMail = headerView.findViewById(R.id.userMail);
        userMail.setText(user.getEmail());

        navigationView.setNavigationItemSelectedListener(this);
    }


    private void updataData() {
        // connexion sur la base de données
        TableUser tableUser = new TableUser(this);
        tableUser.openDatabase();

        HashMap<String,String> list = tableUser.selectById(null, user.getId(), user.getConnexionMode());
        if (list == null) {
            tableUser.insertInto(user);
        } else if (user.getConnexionMode().equals("facebook")) {
            user.resetUserFrom(list);
        }
        tableUser.closeDatabase();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profil) {
            // Handle the camera action
        } else if (id == R.id.param) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_disconnect) {
            if (user.getConnexionMode().equals("facebook")) {
                LoginManager.getInstance().logOut();
                Intent connexion = new Intent(this, Connexion.class);
                startActivity(connexion);
            } else {
                Logout logout = new Logout(this);
                logout.execute();
                try {
                    if (logout.get() == true) {
                        Intent connexion = new Intent(this, Connexion.class);
                        startActivity(connexion);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        // mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
//        locationManager.removeUpdates(this);
//        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // mapView.onLowMemory();
    }



    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        rect = new Rect();
        Log.i("rect","avant : "+rect.top);
        background.getLocalVisibleRect(rect);
        Log.i("rect","apres : "+rect.top);
        if (lastTopValue != rect.top) {
            Log.i("rect","last top value "+lastTopValue);
            lastTopValue = rect.top;
            Log.i("rect","top of rect "+rect.left+","+rect.top+","+rect.right+","+rect.bottom);
            background.setY((float) (rect.top / 2.0));
        }
    }

    public void initiliserMapView() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        background.onCreate(mapViewBundle);
        background.onResume();
        background.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnCameraChangeListener(this);
        mGoogleMap.setOnMapClickListener(this);
        initializeData();
        moveCameraToUserLocation();
        mGoogleMap.setOnMarkerClickListener(this);

        //mode d'affichage de la carte
        mGoogleMap.setTrafficEnabled(true);
    }


    private void moveCameraToUserLocation() {
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = null;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            mGoogleMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            location = locationManager.getLastKnownLocation(provider);
        }
        if(location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition camera = new CameraPosition.Builder()
                    .target(userLocation)
                    .zoom(15)
                    .build();

            userCity = AppGoogleMaps.getCityFromLatLing(userLocation,new Geocoder(this));
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        }
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

    protected void initializeData(){
        tableTerrain = new TableTerrain(this);
        tableTerrain.openDatabase();
        ArrayList<Terrain> terrains = new ArrayList<>();

        displayMarkers();

    }

    private void displayMarkers() {
        markerDisplay = new MarkerTerrainsDisplay(mGoogleMap,tableTerrain);
        markerDisplay.collectAllMarkers();
        markerDisplay.putMarkersOnMap();
        markerDisplay.showMarkers();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        markerDisplay.showMarkers();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();
        return false;
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Home.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                permission = true ;
            }
            permission = false;
            return false;
        } else {
            permission = true ;
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mGoogleMap.setMyLocationEnabled(true);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
                        permission = true ;
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Intent googleMap = new Intent(this,GoogleMaps.class);
        startActivity(googleMap);
    }
}