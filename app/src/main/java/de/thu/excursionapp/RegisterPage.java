package de.thu.excursionapp;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterPage extends BaseActivity {
    private static DatabaseHelper database;
    private EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        // get the database instance
        database = super.database;
        // get important references
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
    }

    // handle registration events
    public void registerHandler(View view){
            // insert data
            // inform the user whether it was successful or not
            if(database == null){
                 showMessage("Null", "Null");
            }
            if(database != null && database.insertData(username.getText().toString(), password.getText().toString(), Data.Student_Table_Name
            , Data.Student_Col1, Data.Student_Col2)){
                showMessage("Confirmation", "successful registration");
                username.setText("");
                password.setText("");
            }
            else{
                showMessage("Failure", "failed registration");
                username.setText("");
                password.setText("");
            }
    }

}
