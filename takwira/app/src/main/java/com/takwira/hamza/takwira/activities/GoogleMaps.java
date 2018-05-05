package com.takwira.hamza.takwira.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.takwira.hamza.takwira.R;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableTerrain;
import com.takwira.hamza.takwira.marker_display.MarkerDisplay;
import com.takwira.hamza.takwira.marker_display.MarkerTerrainsDisplay;
import com.takwira.hamza.takwira.objects.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleMaps extends AppGoogleMaps{

    private ImageView my_location ;
    private EditText search ;
    private List<Terrain> terrains ;
    private TableTerrain tableTerrain ;
    private List<MarkerOptions> markerOptionsList;
    private List<Marker> markers ;
    private MarkerDisplay markerDisplay ;
    private HashMap<String , Terrain> markerOptionsHashMap ;




    @Override
    protected void onStop() {
        super.onStop();
        tableTerrain.closeDatabase();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getToolbar().setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow));

        my_location = (ImageView)findViewById(R.id.my_location);
        my_location.getBackground().setAlpha(200);

        search = (EditText) findViewById(R.id.search);

        getToolbar().setTitle(getResources().getString(R.string.title_activity_maps));
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveCameraToUserLocation();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchAddress(search.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

    }

    @Override
    protected int getMapResourceId(){
        return R.id.map ;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filtre) {
            return true;
        }
        if (id == R.id.action_add) {
            my_location.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if(search.getVisibility() == View.VISIBLE){
            my_location.setVisibility(View.VISIBLE);
            search.setVisibility(View.GONE);
        }
        else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getSnippet() == null) {
            Intent sendCoordination = new Intent(this, SendCoordination.class);
            Terrain terrain = new Terrain();
            terrain.setCity(getCityFromLatLing(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude)));
            terrain.setLatitude(marker.getPosition().latitude);
            terrain.setLongitude(marker.getPosition().longitude);
            terrain.setAddress(marker.getTitle());
            terrain.setNote(0.5f);
            sendCoordination.putExtra("terrain", terrain);
            startActivity(sendCoordination);
        }
        return false;
    }


    @Override
    public int getLayoutResourcesId(){
        return R.layout.activity_maps ;
    }

    @Override
    protected void showMarkersOfThisRegion() {
        markerDisplay.showMarkers();
    }


    @Override
    protected void initializeData(){
        tableTerrain = new TableTerrain(getApplicationContext());
        tableTerrain.openDatabase();
        terrains = new ArrayList<>();

        displayMarkers();

    }

    @Override
    protected void setInfoWindowContents(Marker marker) {

            markerTitle.setText(marker.getTitle());
            markerSnippet.setText(marker.getSnippet());
        if (marker.getSnippet() != null)
            markerRatingBar.setRating(markerOptionsHashMap.get(marker.getTitle()).getNote());
        else
            markerRatingBar.setRating(0);
    }

    private void displayMarkers() {
        markerDisplay = new MarkerTerrainsDisplay(mMap,tableTerrain);
        markerOptionsHashMap = markerDisplay.collectAllMarkers();
        markerDisplay.putMarkersOnMap();
        markerDisplay.showMarkers();
    }



}
