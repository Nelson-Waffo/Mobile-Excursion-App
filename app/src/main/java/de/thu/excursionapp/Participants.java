package de.thu.excursionapp;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.DialogInterface;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * A class used to list participants in a given excursion
 */
public class Participants extends BaseActivity {
    private DatabaseHelper database;
    private ListView listview;
    private String title;
    private TextView mTextView;
    private ArrayList<String> students;
    private ArrayAdapter<String> adapter;
    private AlertDialog.Builder builder;

    /**
     * A method to list participants
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);
        // get an instance of the database object
        database = super.database;
        listview = findViewById(R.id.listview_participants);
        // get the title of the excursion
        title = (String) getIntent().getSerializableExtra("title");
        // title of the page
        mTextView = findViewById(R.id.textview);
        // create the builder object
        builder = new AlertDialog.Builder(this);
        // list of students
        students = new ArrayList<>();
        // show participants
        showParticipants();

        // remove a participant
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //display a dialog where the user confirms that the participant gets removed
                builder.setTitle("Participants List");
                builder.setMessage("Are you sure you want to remove this participant?");
                builder.setCancelable(false);
                // position of the selected name
                final int index = i;
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // remove the student from the participants
                        int decision = database.removeFromTable(students.get(index), title, Data.Booking_Table_Name);
                        // rearrange the list view and show participants
                        if(decision == 1) {
                            showParticipants();
                            // reload the activity
                            finish();
                            Intent intent = new Intent(Participants.this, Participants.class);
                            startActivity(intent);
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }

    /**
     * a method used to create the participants list, also after a participant has been removed
     */
    public void showParticipants(){
        // get all the student of a given excursion
        mTextView.setText("Participants in \n"+ title);
        Cursor result = database.searchExcursions(title, Data.Booking_Col1, Data.Booking_Col2, Data.Booking_Table_Name);
        if(result.getCount()>0) {
            // add all the students in the array list
            while (result.moveToNext()) {
                students.add(result.getString(0));
            }
        }
        else{
            mTextView.setText("No participants currently");
        }
        // create the array adapter
        adapter = new ArrayAdapter<>(this, R.layout.list_view_participant, R.id.item_participant, students);
        // set the list view with the adapter
        listview.setAdapter(adapter);
    }
}
