package com.example.silva16.spikebasedados;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    //Field Names:
	public static final String KEY_ROWID = "_id";
    public static final String KEY_GAME = "game";
    public static final String KEY_GOAL = "goal";
    public static final String KEY_DEFENSE = "defense";

    // Column Numbers for each field:
    public  static final int COL_ROWID = 0;
    public  static final int COL_GAME = 1;
    public  static final int COL_GOAL = 2;
    public  static final int COL_DEFENSE = 3;

    // DataBase info:
    public static final String DATABASE_NAME = "dbTest";
    public static final String DATABASE_TABLE = "tableTest";
    public static final int DATABASE_VERSION = 2;

    //SQL statement to create database
    public static final String DATABASE_CREATE_SQL = "CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_GAME + " TEXT NOT NULL, " + KEY_GOAL + " TEXT" + KEY_DEFENSE + " TEXT" + ");";

    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx){
        this.context = ctx;
        dbHelper = new DatabaseHelper(context);
    }

    // Open database connection
    public DBAdapter open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public long insertRow(String game, String goal, String defense){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_GAME, game);
        initialValues.put(KEY_GOAL, goal);
        initialValues.put(KEY_DEFENSE, defense);

        return db.insert(DATABASE_TABLE,  null, initialValues);
    }



    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Destroy old database:
            db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);

            // Recreate new database:
            onCreate(db);
        }
    }




	


	
}
