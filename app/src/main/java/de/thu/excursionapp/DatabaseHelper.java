package de.thu.excursionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int Version = 8;
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

    /**
     * A mthod to create the tables
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create the tables
        sqLiteDatabase.execSQL(Data.Student_Table);
        sqLiteDatabase.execSQL(Data.Organiser_Table);
        sqLiteDatabase.execSQL(Data.Excursion_Table);
        sqLiteDatabase.execSQL(Data.Booking_Table);
    }

    /**
     * A method to upgrade the tables
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
         // drop tables and create new tables
            if(i < i1) {
                sqLiteDatabase.execSQL(Data.DropStudent);
                sqLiteDatabase.execSQL(Data.DropOrganiser);
                sqLiteDatabase.execSQL(Data.DropExcursion);
                sqLiteDatabase.execSQL(Data.DropBooking);
                onCreate(sqLiteDatabase);
            }
    }

    /**
     * load the organiser table
     */
    public long loadOrganiser(){
        // get an instance of the writable database
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(Data.Organiser_Col1, "organiser");
        contentValues1.put(Data.Organiser_Col2, "organiser");
        long num = database.insert(Data.Organiser_Table_Name, null, contentValues1);
       return num;
    }

    /**
     * An insert method for the registration of students
     * @param entry1
     * @param entry2
     * @param table
     * @param column1
     * @param column2
     * @return
     */
    public boolean insertData(String entry1, String entry2, String table, String column1, String column2){
        // first get an instance of the writable database
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

    /**
     * a method using for logging in users
     * @param username
     * @param password
     * @param table
     * @return
     */
    public Cursor checkLogin(String username, String password, String table){
        Cursor result = null;
        // get the readable database instance
        SQLiteDatabase database = this.getReadableDatabase();
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

    /**
     * A method to return excursion details
     * @return
     */
    public Cursor excursionDetails(){
        // get the readable database instance
        SQLiteDatabase database = this.getReadableDatabase();
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

    /**
     * A method that returns excursion data for a given student
     * @param value
     * @param column1
     * @param column2
     * @param table
     * @return
     */
    public Cursor searchExcursions(String value, String column1, String column2, String table){
        // get the readable database instance
        SQLiteDatabase database = this.getReadableDatabase();
        // query
        String query = "select "+ column1 + " from "+ table + " where "+
                column2 +"='"+value+"'";
        return database.rawQuery(query, null);
    }

    /**
     * A method to search a table and return a string value
     * @param value
     * @param column1
     * @param column2
     * @param table
     * @return
     */
    public String searchExcursionTable(String value, String column1, String column2, String table){
        // get the readable database instance
        SQLiteDatabase database = this.getReadableDatabase();
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

    /**
     *  method for cancelling a booking or removing a participant. if it returns 1, then the removal was successful
     * @param username
     * @param title
     * @param table
     * @return
     */

    public Integer removeFromTable(String username, String title, String table){
        // get the writable database instance
        SQLiteDatabase database = this.getWritableDatabase();
        String[] elements = {username, title};
        // delete the requested entry
        return database.delete(table, "student = ? and excursion = ?", elements);
    }

    /**
     * method for cancelling a booking or removing a participant. if it returns 1, then everything went well
     * @param title
     */
    public Integer deleteExcursion(String title) {
        // get the writable database instance
        SQLiteDatabase database = this.getWritableDatabase();
        String[] elements = {title};
        // an excursion can only be deleted, if no one has booked it. before deleting an excursion,
        // the organiser must first remove all its participants from the list
        Cursor result = searchExcursions(title, Data.Booking_Col1, Data.Booking_Col2, Data.Booking_Table_Name);
        String temp = "";
        while(result.moveToNext()){
            // to see if even one student name will be returned
            temp = result.getString(0);
        }
        // in case of no returned name, the deletion can take place
        if (temp.length() == 0){
            return database.delete(Data.Excursion_Table_Name, "title = ?", elements);
         }
        // if there was a student name then there is still a booked student. no deletion in this case
        return 0;
    }

    /**
     * A method to update a record a table
     * @param oldValue
     * @param newValue1
     * @param newValue2
     * @param column1
     * @param column2
     * @param table
     * @return
     */
    public boolean updateTable(String oldValue, String newValue1, String newValue2, String column1, String column2, String table){
        // get the writable sql database instance
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // fill it
        contentValues.put(column1, newValue1);
        contentValues.put(column2, newValue2);
        String[] value = {oldValue};
        database.update(table, contentValues, "title=?", value);
        return true;
    }

}
