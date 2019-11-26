package de.thu.excursionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int Version = 2;
    private static DatabaseHelper database;

    private DatabaseHelper(@Nullable Context context) {
        // create the database
        super(context, Data.Database_Name, null, Version);
    }
    // singleton pattern
    public static DatabaseHelper getInstance(Context context){
        if(database == null){
            database = new DatabaseHelper(context.getApplicationContext());
        }
        return database;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create the tables
        sqLiteDatabase.execSQL(Data.Student_Table);
        sqLiteDatabase.execSQL(Data.Organiser_Table);
        sqLiteDatabase.execSQL(Data.Excursion_Table);
        sqLiteDatabase.execSQL(Data.Booking_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
         // drop tables to upgrade them
        sqLiteDatabase.execSQL(Data.DropStudent);
        sqLiteDatabase.execSQL(Data.DropOrganiser);
        sqLiteDatabase.execSQL(Data.DropExcursion);
        sqLiteDatabase.execSQL(Data.DropBooking);
        onCreate(sqLiteDatabase);
    }

    // load organiser table
    public void loadOrganiser(){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(Data.Organiser_Col1, "Master");
        contentValues1.put(Data.Organiser_Col2, "manages");
        contentValues1.put(Data.Organiser_Col1, "Mistress");
        contentValues1.put(Data.Organiser_Col2, "manages");
        database.insert(Data.Organiser_Table_Name, null, contentValues1);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Data.Excursion_Col1, "BMW Excursion");
        contentValues2.put(Data.Excursion_Col2, " visit the biggest company in Germany");
        contentValues2.put(Data.Excursion_Col3, "bmw");
        contentValues2.put(Data.Excursion_Col1, "Berlin visit");
        contentValues2.put(Data.Excursion_Col2, " visit the capital of Germany");
        contentValues2.put(Data.Excursion_Col3, "city");
        contentValues2.put(Data.Excursion_Col1, "Vector Excursion");
        contentValues2.put(Data.Excursion_Col2, "visit the biggest company in Stuttgart");
        contentValues2.put(Data.Excursion_Col3, "vector");
       database.insert(Data.Excursion_Table_Name, null, contentValues2);
    }

    // insert method for the registration
    public boolean insertData(String entry1, String entry2, String table, String column1, String column2){
        // first get an instance of the database
        SQLiteDatabase database = this.getWritableDatabase();
        // store the values in a content values object
        ContentValues contentValues = new ContentValues();
        contentValues.put(column1, entry1);
        contentValues.put(column2, entry2);
        // insert these in the database
        long result = database.insert(table, null, contentValues);
        if(result == -1){
            return false;
        }
        return true;
    }

    // a method for checking the login
    public Cursor checkLogin(String username, String password, String table){
        Cursor result = null;
        // get the writable database instance
        SQLiteDatabase database = this.getWritableDatabase();
        // login query
        String studentLogin = "select * from "+ table +" where "+Data.Student_Col1+"='"+username
                +"' and "+Data.Student_Col2+"='"+password+"'";
        String organiserLogin = "select * from "+ table +" where "+Data.Organiser_Col1+"='"+username
                +"' and "+Data.Organiser_Col2+"='"+password+"'";
        try {
            if (table == Data.Student_Table_Name) {
                result = database.rawQuery(studentLogin, null);
            } else if (table == Data.Organiser_Table_Name) {
                result = database.rawQuery(organiserLogin, null);
            }
            return result;
        }
        catch(SQLiteException e){
            e.getMessage();
            return null;
        }
    }

    public Cursor excursionDetails(){
        // get the writable database instance
        SQLiteDatabase database = this.getWritableDatabase();
        // query to get all excursion titles
        String titlesQuery = "Select "+ Data.Excursion_Col1 + ", " + Data.Excursion_Col2 +" from "+ Data.Excursion_Table_Name;
        try {
            return database.rawQuery(titlesQuery, null);
        }
        catch(SQLiteException e){
            e.getMessage();
            return null;
        }
    }
    // returns the excursions of a given student
    public Cursor viewMyExcursions(String username){
        // get the writable database instance
        SQLiteDatabase database = this.getWritableDatabase();
        // query
        String query = "select "+ Data.Booking_Col2 + " from "+Data.Booking_Table_Name + " where "+
                Data.Booking_Col1 +"='"+username+"'";
        return database.rawQuery(query, null);
    }



    // search the book table
    public String searchExcursionTable(String value, String column1, String column2, String table){
        // get the writable database instance
        SQLiteDatabase database = this.getWritableDatabase();
        // query
        String query = "select "+ column1 + " from "+table + " where "+
                column2 +"='"+value+"'";
        Cursor result = database.rawQuery(query, null);
        if(result != null && result.getCount()>0){
            while(result.moveToNext()) {
                return result.getString(0);
            }
        }
        return "";
    }

    // method for cancelling a booking or removing a participant
    public Integer removeFromTable(String username, String title, String table){
        // get the writable database instance
        SQLiteDatabase database = this.getWritableDatabase();
        String[] elements = {username, title};
        return database.delete(table, "student = ? and excursion = ?", elements);
    }
    // method for cancelling a booking or removing a participant
    public Integer deleteExcursion(String title){
        // get the writable database instance
        SQLiteDatabase database = this.getWritableDatabase();
        String[] elements = {title};
        return database.delete(Data.Excursion_Table_Name, "title = ?", elements);
    }

    // update method
    public boolean updateTable(String oldValue, String newValue1, String newValue2, String column1, String column2, String table){
        // get the sqldatabase instance
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // fill it
        contentValues.put(column1, newValue1);
        contentValues.put(column2, newValue2);
        String[] value = {oldValue};
        database.update(table, contentValues, "title=?", value);
        return true;
    }

    public Cursor getAllData(){
        // get the sqldatabase instance
        SQLiteDatabase database = this.getWritableDatabase();
        // instance of a cursor class
        // query the database and store the result in the cursor instance
        Cursor result = database.rawQuery("select * from "+Data.Organiser_Table_Name, null);
        return result;
    }




}
