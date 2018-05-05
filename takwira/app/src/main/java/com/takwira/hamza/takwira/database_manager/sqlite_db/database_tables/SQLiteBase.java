package com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hamza on 05/08/17.
 */

public class SQLiteBase {
    protected final static int VERSION = 1;
    protected final static String DATABASE_NAME = "takwira.db";

    protected SQLiteDatabase mydb = null;
    protected DatabaseHandler databaseHandler = null;

    public SQLiteBase(Context pContext) {
        this.databaseHandler = new DatabaseHandler(pContext, DATABASE_NAME, null, VERSION);
    }

    public SQLiteDatabase openDatabase() {
        mydb = databaseHandler.getWritableDatabase();
        return mydb;
    }

    public void closeDatabase() {
        mydb.close();
    }

    public SQLiteDatabase getDatabase() {
        return mydb;
    }
}
