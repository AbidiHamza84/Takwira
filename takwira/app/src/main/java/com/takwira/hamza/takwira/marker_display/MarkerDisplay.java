package com.takwira.hamza.takwira.marker_display;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.takwira.hamza.takwira.Entities.Terrain;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hamza on 19/08/17.
 */

public abstract class MarkerDisplay {


    protected List<MarkerOptions> markerOptionsList;
    protected List<Marker> markers ;
    private GoogleMap mMap ;

    public MarkerDisplay(GoogleMap mMap) {
        this.mMap = mMap;
        mMap.clear();
    }

    public abstract HashMap<String,Terrain> collectAllMarkers();

    public void putMarkersOnMap() {
        if(markerOptionsList != null ) {
            for (int i = 0; i < markerOptionsList.size(); i++) {
                markers.add(mMap.addMarker(markerOptionsList.get(i)));
            }
        }
    }

    public void showMarkers() {
        if(markers != null) {
            for (int i = 0; i < markers.size(); i++) {
                if (mMap.getProjection()
                        .getVisibleRegion().latLngBounds.contains(markers.get(i).getPosition())) {
                    if (markers.get(i).isVisible() == false) {
                        markers.get(i).setVisible(true);
                    }
                } else
                    markers.get(i).setVisible(false);

            }
        }
    }
}
