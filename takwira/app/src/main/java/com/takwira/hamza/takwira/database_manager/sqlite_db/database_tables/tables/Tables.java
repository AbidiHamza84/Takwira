package com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.tables;

import android.content.Context;

import com.takwira.hamza.takwira.database_manager.sqlite_db.database_tables.SQLiteBase;

import java.util.List;



/**
 * Created by hamza on 17/08/17.
 */

public abstract class Tables<T> extends SQLiteBase {
    protected String columns ;
    protected Context context ;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    public Tables(Context pContext , String columns) {
        super(pContext);
        this.columns = columns ;
        this.context = pContext ;
    }

    protected String getColumns(String[] l) {
        if(l == null){
            return TableTerrain.columns;
        }
        else {
            String column ;
            column = "";
            for (int i = 0; i < l.length - 1; i++) {
                column += l[i] + " , ";
            }
            column += l[l.length - 1];
            return  column ;
        }
    }

    public abstract List<T> selectAll(String[] l);

}
