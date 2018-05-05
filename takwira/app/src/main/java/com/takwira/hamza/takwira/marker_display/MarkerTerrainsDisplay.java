package com.takwira.hamza.takwira.marker_display;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.takwira.hamza.takwira.AsyncTasks.CollectMarkers;
import com.takwira.hamza.takwira.R;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableTerrain;
import com.takwira.hamza.takwira.objects.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by hamza on 19/08/17.
 */

public class MarkerTerrainsDisplay extends MarkerDisplay {
    private TableTerrain table ;
    private HashMap<String,Terrain> hashMarkerOptions ;

    public MarkerTerrainsDisplay(GoogleMap mMap, TableTerrain table) {
        super(mMap);
        this.table = table ;
        hashMarkerOptions = new HashMap<>();
    }

    @Override
    public HashMap<String,Terrain> collectAllMarkers() {
        CollectMarkers<Terrain> collectMarkers = new CollectMarkers() {
            @Override
            public void personalizeMarkers( Object row, MarkerOptions markerOptions) {
                Terrain t =(Terrain) row;
                hashMarkerOptions.put(t.getAddress(),t);
                markerOptions.position(new LatLng(t.getLatitude(), t.getLongitude())).position(new LatLng(t.getLatitude(), t.getLongitude()))
                        .title(t.getAddress())
                        .snippet(t.getType());
                switch (t.getType()) {
                    case "Terrain de foot-ball":
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_footbal));
                        break;
                    case "Terrain de hand-ball":
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_handball));
                        break;
                    case "Terrain de basket-ball":
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_basketball));
                        break;
                    case "Terrain de volley-ball":
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_volleyball));
                        break;
                }
            }
        };
        markerOptionsList = new ArrayList<>();
        collectMarkers.execute(table);
        try {
            markerOptionsList = collectMarkers.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        markers = new ArrayList<>(markerOptionsList.size());
        return hashMarkerOptions;
    }
}
