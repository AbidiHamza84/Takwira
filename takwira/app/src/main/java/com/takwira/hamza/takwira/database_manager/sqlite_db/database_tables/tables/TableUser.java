package com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.takwira.hamza.takwira.objects.User;

import java.util.HashMap;
import java.util.List;

//import org.json.simple.parser.JSONParser;

/**
 * Created by hamza on 05/08/17.
 */

public class TableUser extends Tables<User> {
    public static final String table_name = "TABLE_USER";
    public static final String id = "id" ;
    public static final String connexion_mode = "connexion_mode" ;
    public static final String first_name = "first_name" ;
    public static final String last_name = "last_name" ;
    public static final String email = "email" ;
    public static final String birthday = "birthday" ;
    public static final String location = "location" ;
    public static final String pictureUrl = "pictureUrl" ;
    public static final int size = 8 ;
    public static final String columns = TableUser.id + " , " + TableUser.connexion_mode + " , " + TableUser.first_name + " , " + TableUser.last_name + " , " +
            TableUser.email + " , " + TableUser.birthday + " , " + TableUser.location + " , " + TableUser.pictureUrl ;

    private static String URL_getUserById = "http://192.168.43.159/takwira/getUserById.php";
    private static String URL_createUser = "http://192.168.43.159/takwira/createUser.php";


    public static final String CREATE_USER_TABLE ="CREATE TABLE IF NOT EXISTS " +

            TableUser.table_name + " (" +

            TableUser.id + " TEXT , " +

            TableUser.connexion_mode + " TEXT , " +

            TableUser.first_name + " TEXT, " +

            TableUser.last_name + " TEXT, " +

            TableUser.email + " TEXT, " +

            TableUser.birthday + " DATE , " +

            TableUser.location + " TEXT , " +

            TableUser.pictureUrl + " TEXT , " +

            "PRIMARY KEY (" + TableUser.id + " , " + TableUser.connexion_mode + "));";

    public static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TableUser.table_name + ";";

    public TableUser(Context appContext) {
        super(appContext,columns);
    }

    /**
     *
     * @param l la liste des colonnes à recupèrer
     * @param uid l'identifiant du user à chercher
     * @param uconnexion_mode facebook ou google
     * @return le resultat de la requete sql
     */
    public HashMap<String, String> selectById (String[] l , String uid , String uconnexion_mode){
        if(mydb == null)
            openDatabase();

        String recoverColumns = getColumns(l);

        Cursor cursor = mydb.rawQuery("select "+columns+" from "+table_name+" where "
                +TableUser.id+" like ? and "+TableUser.connexion_mode+" like ?",new String[]{id,connexion_mode});

        HashMap<String , String> user = new HashMap<String, String>(TableUser.size);
        cursor.moveToFirst();
        if(cursor.getCount() != 0) {
            for (int i = 0 ; i < TableUser.size ; i++){
                user.put(cursor.getColumnName(i),cursor.getString(i));
            }
            cursor.close();
            return user;
        }
        else{
            cursor.close();
            return null;
        }
    }

    /**
     *
     * @param user le nouveau user à ajouter dans la base de données
     */
    public void insertInto (User user){
        ContentValues values = new ContentValues();

        values.put(TableUser.id,user.getId());
        values.put(TableUser.connexion_mode,user.getConnexionMode());
        values.put(TableUser.first_name,user.getFirstName());
        values.put(TableUser.last_name,user.getLastName());
        values.put(TableUser.email,user.getEmail());
        values.put(TableUser.birthday,user.getBirthday());
        values.put(TableUser.location,user.getLocation());
        values.put(TableUser.pictureUrl,user.getpictureUrl());
        mydb.insert(TableUser.table_name,null,values);

    }


    @Override
    public List<User> selectAll(String[] l) {
        return null;
    }

}
