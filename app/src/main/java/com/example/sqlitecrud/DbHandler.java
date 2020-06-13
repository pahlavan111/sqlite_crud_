package com.example.sqlitecrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private Context context;
    private List<Person> personList = new ArrayList<>();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "person_db";
    private static final String TBL_NAME = "tbl_person";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CITY = "city";
    private static final String KEY_AGE = "age";

    public DbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TBL_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_AGE +" INTEGER"
                +")";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS "+TBL_NAME);

        //CREATE table again
        onCreate(db);
    }

    long insertPerson(String name, String city, int age){

        SQLiteDatabase db= getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_NAME,name);
        contentValues.put(KEY_CITY,city);
        contentValues.put(KEY_AGE,age);
        return db.insert(TBL_NAME,null,contentValues);
    }

    List<Person> getPersonList(){

        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBL_NAME+ " ORDER BY "+ KEY_ID+" DESC",null);

        while (cursor.moveToNext()){
            Person person = new Person(
                   cursor.getInt( cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_CITY)),
                    cursor.getInt(cursor.getColumnIndex(KEY_AGE))
            );

            personList.add(person);
        }

        return personList;
    }

    boolean deletePerson(int id){
        SQLiteDatabase database=getWritableDatabase();
       return database.delete(TBL_NAME,KEY_ID+" =?",new String[]{String.valueOf(id)})==1;
    }



    boolean updatePerson(int id, String name, String city,int age) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_CITY, city);
        contentValues.put(KEY_AGE, age);

        return sqLiteDatabase.update(TBL_NAME, contentValues, KEY_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

}
