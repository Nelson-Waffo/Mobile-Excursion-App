package de.thu.excursionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WelcomePage extends BaseActivity {
    private String username;
    private DatabaseHelper database;
    private ListView listview;
    private Button view_ex;
    private Button add_ex;
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private String userType;
    private ArrayList<String> excursions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        database = super.database;
        // store the user name
        username = (String)getIntent().getSerializableExtra("username");
        userType = (String)getIntent().getSerializableExtra("table");
        listview = findViewById(R.id.list_view);
        // set button visibility
        view_ex = findViewById(R.id.myex);
        add_ex = findViewById(R.id.addex);
        if(userType.equals(Data.Student_Table_Name)){
            add_ex.setVisibility(View.INVISIBLE);
        }
        if(userType.equals(Data.Organiser_Table_Name)){
            view_ex.setVisibility(View.INVISIBLE);
        }
        displayExcursions();
        //viewAll();
    }

    // display excursions of this user
    public void myExcursions(View v){
        // branch to the page
        Intent intent = new Intent(WelcomePage.this, MyExcursions.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    // display data fetched from the database
    public void displayExcursions(){
        titles = new ArrayList<>();
        titles.add("Select an Excursion and view its description");
        //titles.add("");
        descriptions = new ArrayList<>();
        descriptions.add("Descriptions of excursions");
        //descriptions.add("");
        // get data from the database
        Cursor result = database.excursionDetails();
        while(result.moveToNext()){
            String temp1 = result.getString(0);
            String temp2 = result.getString(1);
            titles.add(temp1);
            descriptions.add(temp2);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_view_item, R.id.item, titles);
        listview.setAdapter(adapter);
        // to select one excursion and view its description
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Intent intent = new Intent(WelcomePage.this, DescriptionPage.class);
                 intent.putExtra("title", titles.get(i));
                 intent.putExtra("description", descriptions.get(i));
                 intent.putExtra("username", username);
                 intent.putExtra("choice", "book");
                 intent.putExtra("userType", userType);
                 startActivity(intent);
        }
        });
    }

    // a method to add excursions to the system
    public void addExcursion(View view){
        // go to a page where you can add a title and a description
        // then when press the button, the contents are extracted and stored in the excursion table
        Intent intent = new Intent(WelcomePage.this, AddEditExcursion.class);
        intent.putExtra("choice", "add");
        startActivity(intent);
    }

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
                    buffer.append("description: "+result.getString(1)+"\n" );
                   // buffer.append("picture: "+result.getString(2)+"\n" );

                }
                // show data
                showMessage("Data", buffer.toString());
            }

}
