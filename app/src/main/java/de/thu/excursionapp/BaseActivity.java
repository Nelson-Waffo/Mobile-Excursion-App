package de.thu.excursionapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * An abstract class used to get an instance of the database helper
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected static DatabaseHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        // instance of the database
        database = DatabaseHelper.getInstance(this);
    }

    /**
     * A method to display messages to the user
     * @param title
     * @param message
     * @param voice
     */
    public void showMessage(String title, String message, final String voice){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), voice, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}
