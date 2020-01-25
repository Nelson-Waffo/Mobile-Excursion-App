/**
 * Excursion Manager is a basic mobile application for managing excursions of Hochschule Ulm.
 * It enables students to book excursions, and also to cancel their bookings in case their plans change
 * On the other side, organiser can manage excursions and manage bookings.
 * to log in as a student, one must first register. Existing student login details: username: student; password: student
 * organisers cannot log in, their details are loaded when the app starts. existing organiser login details are:
 * username: organiser
 * password: organiser
 * gitHub: https://github.com/Nelson-Waffo/Mobile-Excursion-App
 */
package de.thu.excursionapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * A class which is the entry point of the application. It offers the user two possibilities: to login or to register
 * Author: Nelson Waffo
 */
public class MainActivity extends BaseActivity{
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * A method to take the user to the login User Interface
     * @param view
     */
    public void loginInterface(View view){
        // go to the login page
          intent = new Intent(MainActivity.this, LoginPage.class);
          startActivity(intent);
    }
    /**
     * A method to take the user to the Registration User Interface
     * */
    public void registerInterface(View view){
          // go to the register page
        intent = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent);
    }

    /**
     * A method for directing flow within the app with the back button
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
