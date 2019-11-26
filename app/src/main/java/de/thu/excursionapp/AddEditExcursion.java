package de.thu.excursionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddEditExcursion extends BaseActivity {
    private DatabaseHelper database;
    private EditText title, description;
    private String choice;
    private Button add, edit;
    private TextView welcomeMessage;
    private String currentTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_excursion);
        // define objects
        database = super.database;
        welcomeMessage = findViewById(R.id.welcome);
        title = findViewById(R.id.ex_title);
        description = findViewById(R.id.ex_desc);
        // buttons for adding or editing excursions
        add = findViewById(R.id.add_button);
        edit = findViewById(R.id.edit_button);
        // from the welcome page or description page
        choice = (String)getIntent().getSerializableExtra("choice");
        // the current title and description
        currentTitle = (String) getIntent().getSerializableExtra("title");
        String currentDescription = (String) getIntent().getSerializableExtra("description");
        // set text to be edited
        title.setText(currentTitle);
        description.setText(currentDescription);

        if(choice.equals("add")){
            edit.setVisibility(View.INVISIBLE);
            welcomeMessage.setText("Please Enter An Excursion Below");
        }
        if(choice.equals("edit")){
            add.setVisibility(View.INVISIBLE);
            welcomeMessage.setText("Edit The Excursion Below and then Save Your Changes");
        }
    }

    public void excursion_adder(View view){
        // when the button is pressed, edit fiedls get extracted
        String newTitle = title.getText().toString();
        String newDesc = description.getText().toString();
        // pass this new data to the method for adding excursion to the database
        if(database.insertData(newTitle, newDesc, Data.Excursion_Table_Name, Data.Excursion_Col1, Data.Excursion_Col2)){
            showMessage("Confirmation", "A new excusion was successfully added");
            title.setText("");
            description.setText("");
        }
        else{
            showMessage("Failure", "Your excursion was not added");
        }
    }

    public void excursion_editor(View view){
        // extract the edit text
        String newTitle = title.getText().toString();
        String newDesc = description.getText().toString();
        // pass the old one and new one
        // tring oldValue, String newValue1, String newValue2, String column1, String column2, String table
        if(database.updateTable(currentTitle, newTitle, newDesc, Data.Excursion_Col1, Data.Excursion_Col2, Data.Excursion_Table_Name)){
            showMessage("Confirmation", "Excursion was successfully updated");
        }
        else{
            showMessage("Failure", "Excursion was not updated");
        }
    }
}
