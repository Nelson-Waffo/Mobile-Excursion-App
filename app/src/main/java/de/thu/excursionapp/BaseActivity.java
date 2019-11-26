package de.thu.excursionapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
    protected static DatabaseHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        // instance of the database
        database = DatabaseHelper.getInstance(this);
    }

    // show all the data using alert
    // a method to display an alert dialog
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        // show the dialog
        builder.show();
    }
}
