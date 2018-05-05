package com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableHoraires;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableTerrain;
import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables.TableUser;

/**
 * Created by hamza on 05/08/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TableUser.CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(TableTerrain.CREATE_TERRAIN_TABLE);
        sqLiteDatabase.execSQL(TableHoraires.CREATE_HORAIRES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TableUser.DROP_USER_TABLE);
        sqLiteDatabase.execSQL(TableTerrain.DROP_TERRAIN_TABLE);
        sqLiteDatabase.execSQL(TableHoraires.DROP_HORAIRES_TABLE);
        onCreate(sqLiteDatabase);
    }
}
