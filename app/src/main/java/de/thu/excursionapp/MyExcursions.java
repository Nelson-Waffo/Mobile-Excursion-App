// this class shows all your current excursions
package de.thu.excursionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MyExcursions extends BaseActivity {
    private DatabaseHelper database;
    private String username;
    ArrayList<String> excursions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_excursions);
        // get the instance
        database=super.database;
        // get the username
        username = (String) getIntent().getSerializableExtra("username");
        // get the excursions
        Cursor result = database.viewMyExcursions(username);
        // create a arraylist for the excursions
        excursions = new ArrayList<>();
        excursions.add("Check out your excursions below");
        excursions.add("");
        while(result.moveToNext()){
            excursions.add(result.getString(0));
        }
        // create the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.myexcursions, R.id.my, excursions);
        // create a reference to this listview
        ListView listview = findViewById(R.id.listex);
        // display my excursions
        listview.setAdapter(adapter);

        // now the description for this excursion should be fetched

        // now you want to be able to click on an excursion and view the description then decide whether to delete it or not
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyExcursions.this, DescriptionPage.class);
                intent.putExtra("title", excursions.get(i));
                // pass the description of this excursion title
                intent.putExtra("description", database.searchExcursionTable(excursions.get(i), Data.Excursion_Col2, Data.Excursion_Col1, Data.Excursion_Table_Name));
                intent.putExtra("choice", "cancel");
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }
}
