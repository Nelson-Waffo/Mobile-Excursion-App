package de.thu.excursionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

/**
 * A class to display one excursion
 */
public class DescriptionPage extends BaseActivity {
    private TextView title, description;
    private String username;
    private DatabaseHelper database;
    private String choice;
    private String userType;
    private String mTitle;
    private String mDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // display the layout of this page
        setContentView(R.layout.activity_desciption_page);
        // get a database instance
        database = super.database;
        title = findViewById(R.id.titles);
        description = findViewById(R.id.descriptions);
        // create the toolbar object
        Toolbar toolbar = findViewById(R.id.mydesctoolbar);
        toolbar.getBackground().setAlpha(0);
        // set the toolbar as the app bar for the activity
        setSupportActionBar(toolbar);
        // hide app title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // store data from the welcome page
        mTitle = (String)getIntent().getSerializableExtra("title");
        mDescription = (String)getIntent().getSerializableExtra("description");
        username = (String) getIntent().getSerializableExtra("username");
        // choice between booking and cancelling
        choice = (String) getIntent().getSerializableExtra("choice");
        // user type: student or organizer?
        userType = (String) getIntent().getSerializableExtra("userType");
        // set the title and description for this excursion
        title.setText(mTitle);
        description.setText(mDescription);
    }

    /**
     * A method to inflate the menu items
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the relevant menu
        if(choice.equals("book") && userType.equals(Data.Student_Table_Name)){
            getMenuInflater().inflate(R.menu.description_menu_book, menu);
        }
        if(userType.equals(Data.Organiser_Table_Name)){
            getMenuInflater().inflate(R.menu.description_menu_organiser, menu);
        }
        if(choice.equals("cancel") && userType.equals(Data.Student_Table_Name)){
            getMenuInflater().inflate(R.menu.description_menu_cancel, menu);
        }
        return true;
    }

    /**
     * A method to handle menu events
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                edit_excursion();
                return true;
            case R.id.delete:
                delete_excursion();
                return true;
            case R.id.the_participants:
                view_participants();
                return true;
            case R.id.booking:
                book();
                return true;
            case R.id.cancel_booking:
                cancelBooking();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A method responsible for implementing bookings
     */
    public void book(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to book this excursion?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // book this excursion
                if(database.insertData(username, mTitle, Data.Booking_Table_Name, Data.Booking_Col1, Data.Booking_Col2)){
                    showMessage("Confirmation", mTitle + " was successfully booked by you", "you are booked.");
                }
                else{
                    showMessage("Failure", mTitle + " could not be booked.", "You have probably " +
                            "already booked it");
                }
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
     * A method to handle booking cancellations
     */
    public void cancelBooking(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to cancel this booking?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cancel this excursion: pass the title to the database and have it deleted
                if (database.removeFromTable(username, mTitle, Data.Booking_Table_Name) == 1) {
                    showMessage("Confirmation", "Booking of "+mTitle+" was successfully cancelled", "cancelled booking");
                }
                else{
                    showMessage("Failure", "The booking of " + mTitle + " could not be cancelled",
                            "you have probably already cancelled it");
                }
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
     * A method for editing excursions
     */
    public void edit_excursion(){
         // branch to the add_edit excursion page and pass the current title and description
         Intent intent = new Intent(DescriptionPage.this, AddEditExcursion.class);
         // send extra data to the next page
         intent.putExtra("title", mTitle);
         intent.putExtra("description", mDescription);
         intent.putExtra("choice", "edit");
         startActivity(intent);
    }

    /**
     * A method responsible for deleting excursions
     */
    public void delete_excursion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this excursion?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cancel this excursion: pass the title to the database and have it deleted
                if (database.deleteExcursion(mTitle) == 1) {
                    Intent intent = new Intent(DescriptionPage.this, WelcomePage.class);
                    intent.putExtra("username", username);
                    intent.putExtra("table", Data.Organiser_Table_Name);
                    startActivity(intent);
                }
                else{
                    showMessage("Error", mTitle + " could not be deleted", "it probably still has participants");
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    /**
     * A method to display participants in this excursion
     */
    public void view_participants(){
           // get the excursion title
            Intent intent = new Intent(DescriptionPage.this, Participants.class);
            intent.putExtra("title", mTitle);
            // branch to the list view page and pass the excursion title
            startActivity(intent);
    }

    /**
     * A method to direct app flow with the back button
     */
    @Override
    public void onBackPressed() {
        // return the myExcursion page
        if(choice.equals("cancel") && userType.equals(Data.Student_Table_Name)){
            Intent intent = new Intent(DescriptionPage.this, MyExcursions.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else if(choice.equals("")&& userType.equals(Data.Organiser_Table_Name)){
            Intent intent = new Intent(DescriptionPage.this, WelcomePage.class);
            intent.putExtra("username", username);
            intent.putExtra("table", Data.Organiser_Table_Name);
            startActivity(intent);
        }
        else{
            super.onBackPressed();
        }
    }
}
