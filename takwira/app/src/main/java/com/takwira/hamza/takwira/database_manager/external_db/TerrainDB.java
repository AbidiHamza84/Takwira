package com.takwira.hamza.takwira.database_manager.external_db;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.takwira.hamza.takwira.AppConfig;
import com.takwira.hamza.takwira.AppController;
import com.takwira.hamza.takwira.activities.GoogleMaps;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableTerrain;
import com.takwira.hamza.takwira.objects.Terrain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by hamza on 25/08/17.
 */

public class TerrainDB {
    private ProgressDialog pDialog;
    private static final String TAG = TerrainDB.class.getSimpleName();
    private Context context ;

    public TerrainDB(Context context) {
        this.context = context;
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
    }
    /**
     * function to verify terrain details in mysql db
     * */
    public void insertInto(final Terrain terrain) {
        // Tag used to cancel the request
        String tag_string_req = "req_storing";

        pDialog.setMessage("Storing ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_STORE_TERRAIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "storing Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // terrain successfully added
                        // Launch main activity
                        Intent intent = new Intent(context,
                                GoogleMaps.class);
                        context.startActivity(intent);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context.getApplicationContext(),
                                "error_msg : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(context.getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError)
                    Log.e(TAG, "Login Error: TIME OUT ERROR");
                if(error instanceof ServerError)
                    Log.e(TAG, "Login Error: SERVER ERROR");
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(context.getApplicationContext(),
                        "Login Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(TableTerrain.address, terrain.getAddress());
                params.put(TableTerrain.latitude, String.valueOf(terrain.getLatitude()));
                params.put(TableTerrain.longitude, String.valueOf(terrain.getLongitude()));
                params.put(TableTerrain.city, terrain.getCity());
                params.put(TableTerrain.type, terrain.getType());
                params.put(TableTerrain.name, terrain.getName());
                params.put(TableTerrain.phone, terrain.getPhone());
                params.put(TableTerrain.note, String.valueOf(terrain.getNote()));
                params.put(TableTerrain.pictureUrl, terrain.getPictureUrl());

                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                AppConfig.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
