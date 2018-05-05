package com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.takwira.hamza.takwira.objects.Terrain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamza on 10/08/17.
 */

public class TableTerrain extends Tables<Terrain> {
    public static final String table_name = "TERRAIN";

    public static final String name = "name" ;
    public static final String type = "type" ;
    public static final String address = "address" ;
    public static final String city = "city" ;
    public static final String phone = "phone" ;
    public static final String latitude = "latitude" ;
    public static final String longitude = "longitude" ;
    public static final String note = "note" ;
    public static final String pictureUrl = "pictureUrl" ;
    public static final int size = 10 ;
    public static final String columns = TableTerrain.address + " , " + TableTerrain.latitude + " , " + TableTerrain.longitude + " , " +
            TableTerrain.city + " , " + TableTerrain.name + " , " + TableTerrain.note + " , " + TableTerrain.pictureUrl + " , " + TableTerrain.phone + " , " + TableTerrain.type ;


    public static final String CREATE_TERRAIN_TABLE ="CREATE TABLE IF NOT EXISTS " +

            TableTerrain.table_name + " (" +

            TableTerrain.address + " TEXT PRIMARY KEY , " +

            TableTerrain.name + " TEXT , " +

            TableTerrain.type + " TEXT , " +

            TableTerrain.city + " TEXT , " +

            TableTerrain.phone + " INTEGER , " +

            TableTerrain.latitude + " REAL , " +

            TableTerrain.longitude + " REAL , " +

            TableTerrain.note + " REAL , " +

            TableTerrain.pictureUrl + " TEXT );";

    public static final String DROP_TERRAIN_TABLE = "DROP TABLE IF EXISTS " + TableTerrain.table_name + ";";

    public TableTerrain(Context pContext) {
        super(pContext,columns);
    }


    /**
     *
     * @param terrain le nouveau user à ajouter dans la base de données
     */
    public void insertInto (Terrain terrain){
        if(mydb == null || mydb.isOpen() == false)
            openDatabase();

        ContentValues values = new ContentValues();

        try {

            values.put(TableTerrain.address, terrain.getAddress());
            values.put(TableTerrain.latitude, terrain.getLatitude());
            values.put(TableTerrain.longitude, terrain.getLongitude());
            values.put(TableTerrain.city, terrain.getCity());
            values.put(TableTerrain.type, terrain.getType());
            values.put(TableTerrain.name, terrain.getName());
            values.put(TableTerrain.phone, terrain.getPhone());
            values.put(TableTerrain.note, terrain.getNote());
            values.put(TableTerrain.pictureUrl, terrain.getPictureUrl());
            mydb.insert(TableTerrain.table_name, null, values);
        }
        catch (Exception e){
            mydb.close();
        }

    }

    public List<Terrain> selectTerrainsByCityName(String[] l , String visibleCity) {
        if(mydb == null || mydb.isOpen() == false)
            openDatabase();

        String recoverColumns = getColumns(l);


        Cursor cursor = mydb.rawQuery("select "+recoverColumns+" from "+table_name+" where "
                +TableTerrain.city+" like ? ",new String[]{visibleCity});

        Terrain terrain;
        List<Terrain> terrains = new ArrayList<Terrain>(cursor.getCount());
        try {
            while (cursor.moveToNext()) {

                terrain = new Terrain();

                terrain.setAddress(cursor.getString(cursor.getColumnIndex(TableTerrain.address)));
                terrain.setName(cursor.getString(cursor.getColumnIndex(TableTerrain.name)));
                terrain.setType(cursor.getString(cursor.getColumnIndex(TableTerrain.type)));
                terrain.setLatitude(cursor.getDouble(cursor.getColumnIndex(TableTerrain.latitude)));
                terrain.setLongitude(cursor.getDouble(cursor.getColumnIndex(TableTerrain.longitude)));
                terrain.setNote(cursor.getFloat(cursor.getColumnIndex(TableTerrain.note)));
                terrain.setCity(cursor.getString(cursor.getColumnIndex(TableTerrain.city)));
                terrain.setPhone(cursor.getString(cursor.getColumnIndex(TableTerrain.phone)));
                terrain.setPictureUrl(cursor.getString(cursor.getColumnIndex(TableTerrain.pictureUrl)));

                terrains.add(terrain);

            }
        }
        finally {
            cursor.close();
            return terrains;
        }

    }




    public List<Terrain> selectAll(String[] l) {
        if(mydb == null || mydb.isOpen() == false)
            openDatabase();

        String recoverColumns = getColumns(l);


        Cursor cursor = mydb.rawQuery("select "+recoverColumns+" from "+table_name,null);

        Terrain terrain;
        List<Terrain> terrains = new ArrayList<>(cursor.getCount());
        try {
            while (cursor.moveToNext()) {

                terrain = new Terrain();

                terrain.setAddress(cursor.getString(cursor.getColumnIndex(TableTerrain.address)));
                terrain.setName(cursor.getString(cursor.getColumnIndex(TableTerrain.name)));
                terrain.setType(cursor.getString(cursor.getColumnIndex(TableTerrain.type)));
                terrain.setLatitude(cursor.getDouble(cursor.getColumnIndex(TableTerrain.latitude)));
                terrain.setLongitude(cursor.getDouble(cursor.getColumnIndex(TableTerrain.longitude)));
                terrain.setNote(cursor.getFloat(cursor.getColumnIndex(TableTerrain.note)));
                terrain.setCity(cursor.getString(cursor.getColumnIndex(TableTerrain.city)));
                terrain.setPhone(cursor.getString(cursor.getColumnIndex(TableTerrain.phone)));
                terrain.setPictureUrl(cursor.getString(cursor.getColumnIndex(TableTerrain.pictureUrl)));

                terrains.add(terrain);
                Log.i("terrain",terrain.getAddress());

            }
        }
        finally {
            cursor.close();
            return terrains;
        }

    }

}
