package com.example.final_project_djones54;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import java.lang.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> mobileArray = new ArrayList<String>();
    private ArrayList<Integer> state = new ArrayList<Integer>(); // used to track if an item is done or not
    private ArrayList<Integer> idnumber = new ArrayList<Integer>();
    private Button messagebutton, videobutton, documentbutton, backbutton;
    private TextView textView;
    ArrayAdapter adapter;
    private int workoutexist = 0;

    private SimpleCursorAdapter myAdapter;
    public static ListView listView;
    public int option = 0;
    private static Cursor mCursor;
    DBHelper mDatabaseH;
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabaseH = new DBHelper(this);

        populateListView();
        workoutexist = 1;

        messagebutton = (Button) findViewById(R.id.messageButton_home);
        videobutton = (Button) findViewById(R.id.videoButton_home);
        documentbutton = (Button) findViewById(R.id.documentButton_home);
        backbutton = (Button) findViewById(R.id.backButton_home);

        messagebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent message = new Intent(getBaseContext(), MainActivityMessage.class);
                message.putExtra("new_workout", workoutexist);
                message.putExtra("k_num", option);
                startActivity(message);
            }
        });

        videobutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent workout = new Intent(getBaseContext(), MainActivityVideo.class);
                workout.putExtra("new_workout", workoutexist);
                workout.putExtra("k_num", option);
                startActivity(workout);
            }
        });

        documentbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent workout = new Intent(getBaseContext(), MainActivityDocuments.class);
                workout.putExtra("new_workout", workoutexist);
                workout.putExtra("k_num", option);
                startActivity(workout);
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Feature not implemented yet!",
                        Toast.LENGTH_LONG).show();
                /*for(int i = 0; i < mobileArray.size(); i++) {
                    deleteData(idnumber.get(i));
                }*/
            }
        });

    }

    // Initialize ListView
    private void populateListView() {
        Log.d(TAG, "populateListView: Display data in the ListView.");
        Cursor data = mDatabaseH.getData();
        mobileArray.clear();
        state.clear();
        idnumber.clear();
        while(data.moveToNext()) {
            state.add(Integer.parseInt(data.getString(2)));
            int t = Integer.parseInt(data.getString(2));
            if (t == 1) {
                String m = data.getString(1);
                String d = "DONE: ";
                String h = d.concat(m);
                mobileArray.add(h);
                int g = data.getInt(0);
                idnumber.add(g);
            }
            else
            {
                mobileArray.add(data.getString(1));
                int g = data.getInt(0);
                idnumber.add(g);
            }
        }

        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);
        //mDatabaseH.close();
    }

    // Add Item
    public void addData(String newEntry, String status) {
        boolean insertData = mDatabaseH.addData(newEntry, status);

        if (insertData) {
            Toast.makeText(getApplicationContext(), "Data Inserted Successfully",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Data Insert Failed",
                    Toast.LENGTH_LONG).show();
        }
    }

    // Delete Item
    public void deleteData(int newEntry) {
        boolean delData = mDatabaseH.deleteData(newEntry);

        if (delData) {
            Toast.makeText(getApplicationContext(), "Data Deleted Successfully",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Data Delete Failed",
                    Toast.LENGTH_LONG).show();
        }
    }

    // Update Item
    public void updateData(String newEntry, String num) {
        boolean upData = mDatabaseH.updateData(newEntry, num);

        if (upData) {
            Toast.makeText(getApplicationContext(), "Data Updated Successfully",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Data Update Failed",
                    Toast.LENGTH_LONG).show();
        }
    }
}

// Database Helper Class
class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "EXERCISE_LIST";
    private static final String col1 = "ID";
    private static final String col2 = "NAME";
    private static final String col3 = "DONESTATUS"; // add feature to reset this for each workout
    private static final String col4 = "REPS";
    private static final String col5 = "REPS_OPTION";
    private static final String col6 = "WEIGHTS";
    private static final String col7 = "WEIGHTS_OPT";
    private static final String col8 = "DISTANCE";
    private static final String col9 = "DISTANCE_OPT";
    private static final String col10 = "LENGTH_OF_TIME";
    private static final String col11 = "LENGTH_OF_TIME_OPTION";
    private static final String col12 = "COMMENTS";
    private static final String col13 = "COMMENTS_OPTION";

    public DBHelper(Context context) {

        super(context, TABLE_NAME, null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        System.out.println("DATABASE CREATED");
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + col2 + " TEXT, "
                + col3 + " TEXT, " + col4 + " TEXT, " + col5 + " TEXT, " + col6 + " TEXT, " + col7 + " TEXT, " + col8 + " TEXT, " + col9 + " TEXT, "
                + col10 + " TEXT, " + col11 + " TEXT, " + col12 + " TEXT, " + col13 + " TEXT)";
        db.execSQL(createTable);
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, "Jumping Jacks");
        contentValues.put(col3, "0");
        contentValues.put(col4, "30");
        contentValues.put(col5, "1");
        contentValues.put(col6, "0");
        contentValues.put(col7, "0");
        contentValues.put(col8, "0");
        contentValues.put(col9, "0");
        contentValues.put(col10, "5"); // put in a time or just enter min?
        contentValues.put(col11, "1");
        contentValues.put(col12, "Make sure you're opening your legs!");
        contentValues.put(col13, "1");
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(col2, "Push-ups");
        contentValues2.put(col3, "0");
        contentValues2.put(col4, "20");
        contentValues2.put(col5, "1");
        contentValues2.put(col6, "0");
        contentValues2.put(col7, "0");
        contentValues2.put(col8, "0");
        contentValues2.put(col9, "0");
        contentValues2.put(col10, "12"); // put in a time or just enter min?
        contentValues2.put(col11, "1");
        contentValues2.put(col12, "Keep arms shoulder length apart.");
        contentValues2.put(col13, "1");
        ContentValues contentValues3 = new ContentValues();
        contentValues3.put(col2, "Arm curls");
        contentValues3.put(col3, "0");
        contentValues3.put(col4, "30");
        contentValues3.put(col5, "1");
        contentValues3.put(col6, "0");
        contentValues3.put(col7, "0");
        contentValues3.put(col8, "0");
        contentValues3.put(col9, "0");
        contentValues3.put(col10, "5"); // put in a time or just enter min?
        contentValues3.put(col11, "1");
        contentValues3.put(col12, "Make sure you're bringing the weights all the way up!");
        contentValues3.put(col13, "1");

        Log.d(TAG, "addData: Adding " + "Jumping Jacks" + " to " + TABLE_NAME);
        db.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "addData: Adding " + "Push-ups" + " to " + TABLE_NAME);
        db.insert(TABLE_NAME, null, contentValues2);
        Log.d(TAG, "addData: Adding " + "Arms curls" + " to " + TABLE_NAME);
        db.insert(TABLE_NAME, null, contentValues3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL(String.format("DROP IF TABLE EXISTS %s", TABLE_NAME));
        } catch(Exception e)
        {

        }
        onCreate(db);
    }

    // This will be a brand new exercise created from form
    public boolean addDataSuper(String[] newValues, String[] fieldnames, String a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();

        for(int i = 0; i < fieldnames.length; i++)
        {
            data.put(fieldnames[i], newValues[i]);
        }

        long result = db.insert(TABLE_NAME, null, data);

        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }

    public boolean addData(String item, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col2, item);
        contentValues.put(col3, status);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }

    // Update Excercise Item in Table
    public boolean updateDataSuper(String[] newValues, String[] fieldnames, int itemID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = TABLE_NAME;
        ContentValues data = new ContentValues();

        // Column 2 is where the values start; column one is the ID
        for(int i = 0; i < fieldnames.length; i++)
        {
            //if(Integer.parseInt(updateOption[i]) == 1)
            System.out.println(fieldnames[i] + " | " + newValues[i]);
            data.put(fieldnames[i], newValues[i]);
        }

        long result = db.update(TABLE_NAME, data, "ID = ?", new String[]{String.valueOf(itemID)});
//Log.d(TAG, "updatingData: Updating " + item + " in " + TABLE_NAME);

        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateDataOne(String item, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = TABLE_NAME;
        ContentValues data = new ContentValues();
        data.put("DONESTATUS",status);
        long result = db.update(TABLE_NAME, data, "ID = ?", new String[]{String.valueOf(item)});
        //Log.d(TAG, "updatingData: Updating " + item + " in " + TABLE_NAME);

        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }

    // Update Item in Table -- no longer used?
    public boolean updateData(String item, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = TABLE_NAME;
        ContentValues data = new ContentValues();
        data.put("Name", item);
        data.put("DoneStatus",status);
        long result = db.update(TABLE_NAME, data, "Name=?", new String[]{item});
        Log.d(TAG, "updatingData: Updating " + item + " in " + TABLE_NAME);

        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }

    // Delete item by ID
    public boolean deleteData(int itemID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = TABLE_NAME;
        //Log.d(TAG, "deleteData: Deleting " + item + " to " + TABLE_NAME);
        long result = db.delete(table, "ID = ?", new String[]{String.valueOf(itemID)});

        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteDataOld(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        String table = TABLE_NAME;
        Log.d(TAG, "deleteData: Deleting " + item + " to " + TABLE_NAME);
        long result = db.delete(table, "Name=?", new String[]{item});

        if(result == -1)
        {
            return false;
        } else {
            return true;
        }
    }

    // Return all the data from database
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}