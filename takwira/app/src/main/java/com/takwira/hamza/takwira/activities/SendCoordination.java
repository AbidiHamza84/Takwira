package com.takwira.hamza.takwira.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.takwira.hamza.takwira.Entities.Horaires;
import com.takwira.hamza.takwira.R;
import com.takwira.hamza.takwira.Utils.TimePickerFragment;
import com.takwira.hamza.takwira.database_manager.external_db.TerrainDB;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableTerrain;
import com.takwira.hamza.takwira.Entities.Terrain;

public class SendCoordination extends AppCompatActivity implements TimePickerFragment.OnTimePickedListener {
    private Terrain terrain;
    private TextView adresse;
    private TextView latitude;
    private TextView longitude;

    private Horaires horaires;
    private EditText open;
    private EditText close;
    private Spinner time_party;
    private Spinner pause;

    private boolean openTime = true;

    public SendCoordination(){
        horaires = new Horaires();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_coordinations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_send_conrdinations));
        adresse = (TextView) findViewById(R.id.address2);
        latitude = (TextView) findViewById(R.id.latitude2);
        longitude = (TextView) findViewById(R.id.longitude2);
        open = findViewById(R.id.open);
        close = findViewById(R.id.close);
        time_party = findViewById(R.id.spinner_time_party);
        pause = findViewById(R.id.spinner_pause);

        Intent intent = getIntent() ;
        terrain = (Terrain) intent.getExtras().get("terrain");
        adresse.setText(terrain.getAddress());
        latitude.setText(terrain.getLatitude()+"");
        longitude.setText(terrain.getLongitude()+"");

        horaires.setAdress(terrain.getAddress());


        Spinner spinner = findViewById(R.id.spinner_type_terrains);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                terrain.setType(parentView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        time_party.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                horaires.setTime_party(parentView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        pause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                horaires.setPause(parentView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(terrain.getType() != null && terrain.getType().equals("Choisir") == false) {

                    terrain.setHoraires(horaires);

                    TerrainDB db_terrain = new TerrainDB(SendCoordination.this);
                    //on construit le message qui va etre envoyer par mail
                    String message = "Adresse  : " + terrain.getAddress() + "\n"
                            + "latitude : " + terrain.getLatitude() + "\n"
                            + "longitude: " + terrain.getLongitude() + "\n"
                            + "type     : " + terrain.getType();

//                    startActivity(SendViaGmail.createEmailIntent(getApplication(),
//                            getResources().getString(R.string.mail_app),
//                            "demande d'ajout d'un nouveau terrain", message));
                    TableTerrain tableTerrain = new TableTerrain(getApplicationContext());
                    tableTerrain.openDatabase();
                    tableTerrain.insertInto(terrain);
                    Snackbar.make(view, "Terrain ajouté", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    db_terrain.insertInto(terrain);
                }
                else
                    Snackbar.make(view, getResources().getString(R.string.field_type_forgot), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }


    public void showOpenTimePickerDialog(View view) {
        openTime = true;
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Horaire d'ouverture");
    }

    public void showCloseTimePickerDialog(View view) {
        openTime = false;
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Horaire de ferméture");
    }

    @Override
    public void onTimePicked(String time) {
        if (openTime){
            // c'est le timePicker d'ouverture qui à été cliqué
            open.setText(time);
            horaires.setOpen(time);
        }
        else {
            // c'est le timePicker de ferméture qui à été cliqué
            close.setText(time);
            horaires.setClose(time);
        }
    }


}


