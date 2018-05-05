package com.takwira.hamza.takwira.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import static com.takwira.hamza.takwira.activities.Connexion.mGoogleApiClient;

/**
 * Created by hamza on 08/08/17.
 */

public class Logout extends AsyncTask <Void , Void ,Boolean>{

    boolean operation ;
    ProgressDialog progressBar ;

    public Logout(Context context) {
        progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);
        progressBar.setMessage("Deconnexion\nen cours ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        operation = false;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if(mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<com.google.android.gms.common.api.Status>() {
                        @Override
                        public void onResult(@NonNull com.google.android.gms.common.api.Status status) {
                            if (status.isSuccess()) {
                                Log.d("home", "User Logged out");
                                operation = true;
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("home", "Google API Client Connection Suspended");
            }
        });
        return operation ;
    }

    protected void onPostExecute(Boolean feed) {

    }
}

