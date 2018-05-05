package com.takwira.hamza.takwira.AsyncTasks;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.Tables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamza on 17/08/17.
 */

public abstract class CollectMarkers<T> extends AsyncTask<Tables<T>, Void ,List<MarkerOptions>>  {



    @Override
    protected List<MarkerOptions> doInBackground(Tables<T>... tables) {
        List<T> rows ;
        MarkerOptions markerOptions = null;
        List<MarkerOptions> options = new ArrayList<MarkerOptions>();
        rows = tables[0].selectAll(null);

        for (int i = 0; i < rows.size(); i++) {
            markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(0,0))
                    .title("default")
                    .snippet("default");
            personalizeMarkers(rows.get(i),markerOptions);
            options.add(markerOptions);
        }
        return options;
    }

    public abstract void personalizeMarkers(T row  , MarkerOptions markerOptions);
}
