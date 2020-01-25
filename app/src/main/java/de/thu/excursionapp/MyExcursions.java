package de.thu.excursionapp;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A class which simply lists the excursions of a given student
 */
public class MyExcursions extends BaseActivity {
    private DatabaseHelper database;
    private String username;
    private TextView page_title;
    ArrayList<String> excursions;

    /**
     * A method to display the current excursions of a student
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myexcursions);
        // get the instance
        database=super.database;
        // get the username of this student from the previous page
        username = (String) getIntent().getSerializableExtra("username");
        // get the excursions of this student
        Cursor result = database.searchExcursions(username, Data.Booking_Col2, Data.Booking_Col1, Data.Booking_Table_Name);
        // create a array list for the excursions of this student
        excursions = new ArrayList<>();
        // set title of the page
        page_title = findViewById(R.id.pagetext);
        while(result.moveToNext()){
            excursions.add(result.getString(0));
        }
        if(excursions.size() > 0){
            page_title.setText("Your Excursions");
        }
        else{
            page_title.setText("Your List of Excursions is Empty");
        }
        // create the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.my_excursion_items, R.id.my_items, excursions);
        // create a reference to the list view
        ListView listview = findViewById(R.id.my_ex_list);
        // display the excursions of this student
        listview.setAdapter(adapter);

        // a listener which makes it possible to interact with the list entries
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyExcursions.this, DescriptionPage.class);
                intent.putExtra("title", excursions.get(i));
                // pass the description of this excursion title
                intent.putExtra("description", database.searchExcursionTable(excursions.get(i), Data.Excursion_Col2, Data.Excursion_Col1, Data.Excursion_Table_Name));
                intent.putExtra("choice", "cancel");
                intent.putExtra("username", username);
                intent.putExtra("userType", Data.Student_Table_Name);
                startActivity(intent);
            }
        });

    }

    /**
     * A method to direct flow in the app with the back button
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyExcursions.this, WelcomePage.class);
        intent.putExtra("username", username);
        intent.putExtra("table", Data.Student_Table_Name);
        startActivity(intent);
    }
}
