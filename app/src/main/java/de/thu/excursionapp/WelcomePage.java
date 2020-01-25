package de.thu.excursionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * A class to display the current excursions to the user
 */
public class WelcomePage extends BaseActivity {
    private String username;
    private DatabaseHelper database;
    private ListView listview;
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private TextView mTextView;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        database = super.database;
        // store the user name and user type
        username = (String) getIntent().getSerializableExtra("username");
        userType = (String) getIntent().getSerializableExtra("table");
        listview = findViewById(R.id.list_view);
        mTextView = findViewById(R.id.pagetitle);
        // create the toolbar object
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        toolbar.getBackground().setAlpha(0);
        // set the toolbar as the app bar for the activity
        setSupportActionBar(toolbar);
        // hide app title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // display excursions
        displayExcursions();
    }

    /**
     * Display excursions of a given student
     */
    public void myExcursions() {
        // branch to the page
        Intent intent = new Intent(WelcomePage.this, MyExcursions.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * A method to inflate the selected group of menu items
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu xml file
        switch(userType){
            case Data.Organiser_Table_Name:
                getMenuInflater().inflate(R.menu.welcome_menu_organiser, menu);
                break;
            case Data.Student_Table_Name:
                getMenuInflater().inflate(R.menu.welcome_menu_student, menu);
                break;
        }
        return true;
    }

    /**
     * A method to handle menu events by calling other methods
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                logout();
                return true;
            case R.id.new_excursion:
                addExcursion();
                return true;
            case R.id.my_excursions:
                myExcursions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A method to display excursions for the user
     */
    public void displayExcursions() {
        titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        // get excursion data from the database
        Cursor result = database.excursionDetails();
        while (result.moveToNext()) {
            titles.add(result.getString(0));
            descriptions.add(result.getString(1));
        }
        // set the title of the page
        if(titles.size() > 0){
            mTextView.setText("Available Excursions");
        }else{
            mTextView.setText("No Excursions Currently");
        }
        // set the list view with the data from the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_view_item, R.id.item, titles);
        listview.setAdapter(adapter);
        // to select one excursion and view its description
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(WelcomePage.this, DescriptionPage.class);

                intent.putExtra("title", titles.get(i));
                intent.putExtra("description", descriptions.get(i));
                intent.putExtra("username", username);
                intent.putExtra("choice", "book");
                intent.putExtra("userType", userType);
                startActivity(intent);
            }
        });
    }

    /**
     * A method to enable the organiser to create excursions
     */
    public void addExcursion() {
        // go to a page where you can add a title and a description
        // then when press the button, the contents are extracted and stored in the excursion table
        Intent intent = new Intent(WelcomePage.this, AddEditExcursion.class);
        intent.putExtra("choice", "add");
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * A logout method to load the login page
     */
    public void logout() {
        // confirm that the user wants to log out
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to log out?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // return to the login page
                Intent intent = new Intent(WelcomePage.this, LoginPage.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    /**
     * A method to ensure that the press on the back button does not log out the user
     */
    @Override
    public void onBackPressed() {
        // the back button is disabled.
        // this enforcing the user of the logout functionality
    }
}
