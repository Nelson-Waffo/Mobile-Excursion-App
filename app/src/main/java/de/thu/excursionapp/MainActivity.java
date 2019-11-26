package de.thu.excursionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private static DatabaseHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // handles clicks on login button
    public void loginInterface(View view){
          // go to the login page
          intent = new Intent(MainActivity.this, LoginPage.class);
          startActivity(intent);
    }
    // handles events on register button
    public void registerInterface(View view){
          // go to the register page
        intent = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(intent);
    }


}
