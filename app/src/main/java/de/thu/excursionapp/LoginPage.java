package de.thu.excursionapp;



import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class LoginPage extends BaseActivity {
    private EditText username, password;
    private String table_name = Data.Student_Table_Name;
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        // database instance
        database = super.database;
        // load organisers
        database.loadOrganiser();
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        //viewAll();
    }

    public void loginHandler(View view) {
        String temp1 = null;
        String temp2 = null;
        String userData = username.getText().toString();
        String passData = password.getText().toString();
        // check if the data provided is that of a registered user
        Cursor result = database.checkLogin(userData, passData, table_name);
        while (result.moveToNext()) {
            temp1 = result.getString(0);
            temp2 = result.getString(1);
        }
        if (result != null && result.getCount() > 0) {
            if (temp1.equals(userData) && temp2.equals(passData)) {
                showMessage("Confirmation", "Successful login");
                username.setText("");
                password.setText("");
                // go to the next page
                Intent intent = new Intent(LoginPage.this, WelcomePage.class);
                intent.putExtra("username", userData);
                intent.putExtra("table", table_name);
                startActivity(intent);
            } else {
                showMessage("Failure", "Your login failed");
            }
        } else {
            showMessage("Failure", "Your login failed ");
        }
    }

    // handle radio button events
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

    // a method to fetch all data from the database and show it in a dialog
    public void  viewAll(){
                // store results in a cursor instance
                Cursor result = database.getAllData();
                if(result.getCount()==0){
                    // if nothing is found
                    showMessage("error", "nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(result.moveToNext()){
                    buffer.append("title: "+result.getString(0)+"\n" );
                    buffer.append("Description: "+result.getString(1)+"\n" );
                    buffer.append("picture: "+result.getString(2)+"\n" );

                }
                // show data
                showMessage("Data", buffer.toString());


    }

}

