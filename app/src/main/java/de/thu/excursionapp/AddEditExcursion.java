package de.thu.excursionapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A class used to create or edit excursions
 */
public class AddEditExcursion extends BaseActivity {
    private DatabaseHelper database;
    private EditText title, description;
    private String choice;
    private Button add, edit;
    private TextView welcomeMessage;
    private String currentTitle;
    private String username;
    private String newTitle;
    private String newDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_excursion);
        // set background color for the activity
        getWindow().getDecorView().setBackgroundColor(Color.rgb(204, 230, 255));
        // set references for the attributes
        database = super.database;
        welcomeMessage = findViewById(R.id.welcome);
        title = findViewById(R.id.ex_title);
        description = findViewById(R.id.ex_desc);
        // buttons for adding or editing excursions
        add = findViewById(R.id.add_button);
        edit = findViewById(R.id.edit_button);
        // store the choice of the user: to edit or to add an excursion
        choice = (String)getIntent().getSerializableExtra("choice");
        username = (String)getIntent().getSerializableExtra("username");
        // the current title and description
        currentTitle = (String) getIntent().getSerializableExtra("title");
        String currentDescription = (String) getIntent().getSerializableExtra("description");
        // set text to be edited
        title.setText(currentTitle);
        description.setText(currentDescription);
        // if the user does not modify anything in the edit window,
        // and just exits it then the new title and description are:
        newTitle = title.getText().toString();
        newDesc = description.getText().toString();
        // set the welcome message, based on the user choice
        if(choice.equals("add")){
            edit.setVisibility(View.INVISIBLE);
            welcomeMessage.setText("Excursion Creation");
        }
        if(choice.equals("edit")){
            add.setVisibility(View.INVISIBLE);
            welcomeMessage.setText("Excursion Editing");
        }
    }

    /**
     * A method to create an excursion
     * @param view
     */
    public void excursion_adder(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to create this excursion?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // when the button is pressed, edit fields get extracted
                String newTitle = title.getText().toString();
                String newDesc = description.getText().toString();
                // pass this new data to the method for adding excursion to the database
                if(newTitle.length()>0 && newDesc.length()>0 && database.insertData(newTitle, newDesc, Data.Excursion_Table_Name, Data.Excursion_Col1, Data.Excursion_Col2)){
                    showMessage("Confirmation", "The excursion was successfully created", "an excursion was added");
                    title.setText("");
                    description.setText("");
                }
                else{
                    showMessage("Failure", "Your excursion was not created", "you may try again");
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * A method to edit an existing excursion
     * @param view
     */
    public void excursion_editor(View view){
        // extract the edited text for this excursion and set them as new title and description
        newTitle = title.getText().toString();
        newDesc = description.getText().toString();
        // edit the excursion
        if(newTitle.length()>0 && newDesc.length()>0 && database.updateTable(currentTitle, newTitle, newDesc, Data.Excursion_Col1, Data.Excursion_Col2, Data.Excursion_Table_Name)){
            showMessage("Confirmation", "Excursion was successfully updated", "Saved");
        }
        else{
            showMessage("Failure", "Excursion was not updated", "Try again");
        }
    }

    /**
     * A method to return to the welcome page
     */
    @Override
    public void onBackPressed(){
        if(choice.equals("add")) {
            Intent intent = new Intent(AddEditExcursion.this, WelcomePage.class);
            // return to the welcome page with the relevant data
            intent.putExtra("username", username);
            intent.putExtra("table", Data.Organiser_Table_Name);
            startActivity(intent);
        }
        else if(choice.equals("edit")){
            Intent intent = new Intent(AddEditExcursion.this, DescriptionPage.class);
            intent.putExtra("username", username);
            intent.putExtra("userType", Data.Organiser_Table_Name);
            intent.putExtra("choice", "");
            intent.putExtra("title",newTitle );
            intent.putExtra("description", newDesc);
            startActivity(intent);
        }
        else{
            super.onBackPressed();
        }
    }
}
