package com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables;

import android.content.Context;

import com.takwira.hamza.takwira.objects.Horaires;

import java.util.List;

/**
 * Created by hamza on 10/08/17.
 */

public class TableHoraires extends Tables<Horaires> {
    public static final String table_name = "HORAIRES";
    public static final String ouverture = "ouverture" ;
    public static final String fermeture = "fermeture" ;
    public static final String time_party = "temps_par_partie" ;
    public static final String pause = "pause" ;
    public static final String address_terrain = "adresse" ;

    public static final int size = 8 ;

    public static final String columns = "";

    public static final String CREATE_HORAIRES_TABLE ="CREATE TABLE IF NOT EXISTS " +

            TableHoraires.table_name + " (" +

            TableHoraires.ouverture + " TIME , " +

            TableHoraires.fermeture + " TIME , " +

            TableHoraires.time_party + " TIME , " +

            TableHoraires.pause + " TIME , " +

            TableHoraires.address_terrain + " TEXT , " +

            "FOREIGN KEY (" + TableHoraires.address_terrain + " ) REFERENCES " + TableTerrain.table_name + "("+ TableTerrain.address +"));";

    public static final String DROP_HORAIRES_TABLE = "DROP TABLE IF EXISTS " + TableHoraires.table_name + ";";

    public TableHoraires(Context pContext) {
        super(pContext,columns);
    }

    @Override
    public List<Horaires> selectAll(String[] l) {
        return null;
    }
}
