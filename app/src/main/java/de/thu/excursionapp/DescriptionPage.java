package de.thu.excursionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DescriptionPage extends BaseActivity {
    private TextView title, description;
    private String username;
    private DatabaseHelper database;
    private String choice;
    private String userType;
    private String mTitle;
    private Button participants_button;
    private String mDescription;
    private int returnNumber=0;
    private Button cancel_excursion, edit_excursion;
    private Button booking, cancel_booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desciption_page);
        database = super.database;
        title = findViewById(R.id.titles);
        description = findViewById(R.id.descriptions);

        // button for view participants of an excursion
        participants_button = findViewById(R.id.participants);
        // data from the welcome page
        username = (String) getIntent().getSerializableExtra("username");
        mTitle = (String)getIntent().getSerializableExtra("title");
        mDescription = (String)getIntent().getSerializableExtra("description");
        // choice between booking and cancelling; usertype: student or organizer?
        choice = (String) getIntent().getSerializableExtra("choice");
        userType = (String) getIntent().getSerializableExtra("userType");
        // get references to the buttons
        booking = findViewById(R.id.book);
        cancel_booking = findViewById(R.id.cancel_booking);

        cancel_excursion = findViewById(R.id.cancel_ex);
        edit_excursion = findViewById(R.id.edit_ex);

        // visibility of views
        if(choice.equals("book") && userType.equals(Data.Student_Table_Name)){
            cancel_booking.setVisibility(View.INVISIBLE);
            cancel_excursion.setVisibility(View.INVISIBLE);
            edit_excursion.setVisibility(View.INVISIBLE);
            participants_button.setVisibility(View.INVISIBLE);
        }
        if(choice.equals("cancel") && userType.equals(Data.Student_Table_Name)){
            booking.setVisibility(View.INVISIBLE);
            cancel_excursion.setVisibility(View.INVISIBLE);
            edit_excursion.setVisibility(View.INVISIBLE);
            participants_button.setVisibility(View.INVISIBLE);
        }
        if(userType.equals(Data.Organiser_Table_Name)){
            cancel_booking.setVisibility(View.INVISIBLE);
            booking.setVisibility(View.INVISIBLE);
        }
        // set the title and description
        title.setText(mTitle);
        description.setText(mDescription);
    }

    // handle booking events
    public void book(View view){
         // insert the data in the book table
         if(database.insertData(username, mTitle, Data.Booking_Table_Name, Data.Booking_Col1, Data.Booking_Col2)){
             showMessage("Success: ", "You have successfully booked.");
         }
         else{
             showMessage("Failure: ", "You have not successfully booked.");
         }
    }

    // handle cancellations of bookings
    public void cancel_bookings(View view){
        // go to book table, and remove the excursion using the username and excursion title
        returnNumber= database.removeFromTable(username, mTitle, Data.Booking_Table_Name);
        if(returnNumber == 1){

               showMessage("Confirmation", "Booking of "+mTitle+" was successfully cancelled");
        }
        else{
            showMessage("Error", "Cancellation failed");
        }
    }

    // handle editing of excursions
    public void edit_excursion(View view){
         // branch to the add_edit excursion page and pass the current title and description
         Intent intent = new Intent(DescriptionPage.this, AddEditExcursion.class);
         intent.putExtra("title", mTitle);
         intent.putExtra("description", mDescription);
         intent.putExtra("choice", "edit");
         startActivity(intent);
    }

    // cancel excursions
    public void cancel_excursion(View view){
          // cancel this excursion: pass the title to the database and have it deleted
          if(database.deleteExcursion(mTitle) == 1){
              showMessage("Confirmation", "Excursion "+ mTitle + " was successfully deleted");
          }
          else{
              showMessage("Error", "Excursion "+ mTitle + "was not deleted");
          }
    }

    // view participants in this excursion
    public void view_participants(View view){
           // get the excursion title
           // bramch to the list view page and pass the excursion title
           //
    }

    // override the back key: called when the back key is pressed
    /*@Override

    public void onBackPressed(){
        super.onBackPressed();
        if(returnNumber == 1) {
            Intent intent = new Intent(DescriptionPage.this, MyExcursions.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }*/



}
