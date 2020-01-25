// this activity display the login page
package de.thu.excursionapp;



import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * A class to log in the user when correct credentials are provided
 */
public class LoginPage extends BaseActivity {
    private EditText username, password;
    private String table_name = Data.Student_Table_Name;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        // set background color for the activity
        getWindow().getDecorView().setBackgroundColor(Color.rgb(204, 230, 255));
        // database instance
        database = super.database;
        // load the organiser data
        database.loadOrganiser();
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
    }

    /**
     * A method to login a user
     *
     * @param view
     */
    public void loginHandler(View view) {
        String temp1 = null;
        String temp2 = null;
        // store credentials
        String userData = username.getText().toString();
        String passData = password.getText().toString();
        // check credentials
        Cursor result = database.checkLogin(userData, passData, table_name);
        while (result.moveToNext()) {
            temp1 = result.getString(0);
            temp2 = result.getString(1);
        }
        // go to the welcome page, if credentials are validated
        if (result != null && result.getCount() > 0) {
            if (temp1.equals(userData) && temp2.equals(passData)) {
                username.setText("");
                password.setText("");
                // go to the welcome page
                Intent intent = new Intent(LoginPage.this, WelcomePage.class);
                intent.putExtra("username", userData);
                intent.putExtra("table", table_name);
                startActivity(intent);
            } else {
                showMessage("Failure", "Your login failed", "please try again");
            }
        } else {
            showMessage("Failure", "Your login failed ", "please try again");
        }
    }

    /**
     * A method which sets the user type based on the user choice óf radio buttons
     *
     * @param view
     */
    public void onClickRadioButton(View view) {
        // Is the button now checked?
        boolean value = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.student_radio:
                if (value)
                    table_name = Data.Student_Table_Name;
                break;
            case R.id.organiser_radio:
                if (value)
                    table_name = Data.Organiser_Table_Name;
                break;
        }
    }

    /**
     * A method to return to the previous activity the main activity page
     */
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginPage.this, MainActivity.class);
         startActivity(intent);
    }
}
