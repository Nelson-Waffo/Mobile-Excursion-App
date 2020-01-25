package de.thu.excursionapp;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * A class to register students in the system
 */
public class RegisterPage extends BaseActivity {
    private static DatabaseHelper database;
    private EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        // get the database instance
        database = super.database;
        // get user name and password of the future user
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
    }

    /**
     * A method to handle the registration of a student
     * @param view
     */
    public void registerHandler(View view){
            String the_name = username.getText().toString();
            String the_password = password.getText().toString();
            // make sure there no space
            if(the_name.contains(" ") || the_password.contains(" ")){
                showMessage("Failure", "no space allowed in your credentials", "no space allowed");
                username.setText("");
                password.setText("");
                return;
            }
            // the first character cannot be a digit
            if(the_name.charAt(0)>='0' && the_name.charAt(0)<='9'){
                showMessage("Failure", "The first character cannot be a digit", "no digits allowed");
                username.setText("");
                password.setText("");
                return;
            }
            // inform the user whether it was successful or not
            if(database != null && database.insertData(the_name, the_password, Data.Student_Table_Name
            , Data.Student_Col1, Data.Student_Col2)){
                showMessage("Confirmation", "Successful Registration", "new student registered");
                username.setText("");
                password.setText("");
            }
            else{
                showMessage("Failure", "Failed registration", "try again");
                username.setText("");
                password.setText("");
            }
    }

}
