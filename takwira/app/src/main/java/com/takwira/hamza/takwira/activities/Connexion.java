package com.takwira.hamza.takwira.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.takwira.hamza.takwira.R;
import com.takwira.hamza.takwira.Entities.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Connexion extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    public static GoogleApiClient mGoogleApiClient = null;
    private GoogleSignInOptions gso;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private Context context ;
    private boolean connected = false ;
    private CallbackManager callbackManager ;
    private User user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        initGoogle();

        callbackManager = CallbackManager.Factory.create();


        setContentView(R.layout.activity_connexion);
        context = this;


        user = new User();

        isAlreadyConnected();


        // Set the dimensions of the sign-in button.
        LoginButton signInButtonFacebook = (LoginButton) findViewById(R.id.connexion_button_facebook);

        SignInButton signInButtonGoogle = (SignInButton) findViewById(R.id.connexion_button_gmail);
        signInButtonGoogle.setSize(SignInButton.SIZE_STANDARD);





        signInButtonFacebook.setOnClickListener(this);

        signInButtonGoogle.setOnClickListener(this);

    }

    private void isAlreadyConnected() {
        if(Profile.getCurrentProfile() != null) {

            connected = false;
            Intent home = new Intent(context, Home.class);
            Profile newProfile = Profile.getCurrentProfile();
            user.setId(newProfile.getId());
            user.setConnexionMode("facebook");
            home.putExtra("user", user);
            startActivity(home);
            finish();
        }
        OptionalPendingResult<GoogleSignInResult> pendingResult =
                Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (pendingResult.isDone()) {
            // There's immediate result available.
            handleSignInResult(pendingResult.get());
            Intent home = new Intent(this, Home.class);
            home.putExtra("user", user);
            startActivity(home);
        }

    }


    void logInGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mGoogleApiClient.connect();
        Log.i("init_google logingoogle",mGoogleApiClient.isConnected()+"");
    }

    void logInFacebook() {

            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("user_friends", "email", "public_profile", "user_birthday", "user_location"));
        connectToFacebook();


    }

    private void connectToFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        setFacebookData(loginResult);
                        if (connected == true) {
                            connected = false;
                            Intent home = new Intent(context, Home.class);
                            home.putExtra("user", user);
                            startActivity(home);
                            finish();
                        }
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });
    }


    void initGoogle(){
        Log.i("init_google","creation");
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            if(connected == true) {
                connected = false;
                Intent home = new Intent(this, Home.class);
                home.putExtra("user", user);
                startActivity(home);
            }
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            connected = true ;
            GoogleSignInAccount acct = result.getSignInAccount();
            user.setConnexionMode("google");
            user.setId(acct.getId());
            user.setFirstName(acct.getFamilyName());
            user.setLastName(acct.getGivenName());
            user.setEmail(acct.getEmail());
            user.setBirthday("gm");
            user.setpictureUrl(acct.getPhotoUrl().toString());
            Log.i("image uri ",acct.getPhotoUrl().toString());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connexion_button_facebook:
                logInFacebook();
                break;
            case R.id.connexion_button_gmail:
                logInGoogle();
                break;
        }
    }

    public void setFacebookData(final LoginResult loginResult) {
        connected = true ;
        final GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.i("Response",object.toString());
                            user.setEmail(object.getString("email"));
                            user.setBirthday(object.getString("birthday"));
                            user.setLocation(object.getJSONObject("location").getString("name"));
                            Profile profile = Profile.getCurrentProfile();
                            if (Profile.getCurrentProfile()!=null)
                            {
                                user.setId(profile.getId());
                                user.setFirstName(profile.getFirstName());
                                user.setLastName(profile.getLastName());
                                user.setpictureUrl(profile.getProfilePictureUri(200,200).toString());
                                user.setConnexionMode("facebook");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,birthday,location");
        request.setParameters(parameters);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                request.executeAndWait();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        request.executeAsync();
    }

}



