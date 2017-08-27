package com.example.isaac.exemploretrofit.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Isaac on 22/08/2017.
 */

public class MeuOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "banco";


    public MeuOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table filmes(id INTEGER PRIMARY KEY, " +
                               "title TEXT, original_title TEXT, poster_path TEXT, release_date TEXT, " +
                               "overview TEXT, runtime INTEGER, cod_filme INTEGER, visto INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //upgrade de banco de dados
        //sqLiteDatabase.execSQL("alter table add()");
    }

}
