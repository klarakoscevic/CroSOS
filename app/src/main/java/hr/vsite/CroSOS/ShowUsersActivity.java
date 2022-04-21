package hr.vsite.CroSOS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.vsite.myapplication.dbHelper;

public class ShowUsersActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    protected CustomAdapter mAdapter;
    protected String[] mDataset;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(this);

        setRecyclerViewLayoutManager(mLayoutManager);

        initDataset();

        mAdapter = new CustomAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        rvUsers.setAdapter(mAdapter);
    }


    public void setRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (rvUsers.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) rvUsers.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        rvUsers.setLayoutManager(mLayoutManager);
        rvUsers.scrollToPosition(scrollPosition);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        int i = 0;
        dbHelper db = new dbHelper(this);
        Cursor users = db.readAllPersonData();
        mDataset = new String[users.getCount()];

        if(users.moveToFirst()){
            do{
                @SuppressLint("Range") String userFirstName = users.getString(users.getColumnIndex("person_name"));
                @SuppressLint("Range") String userLastName = users.getString(users.getColumnIndex("person_surname"));
                mDataset[i] = userFirstName + " " + userLastName;
                i++;
            }while(users.moveToNext());
        }users.close();
    }
}


    /*
    private void UpdateGUI(){

        dbHelper db = new dbHelper(this);
        Cursor users = db.readAllPersonData();

        if(users.moveToFirst()){
            do{
                @SuppressLint("Range") String userFirstName = users.getString(users.getColumnIndex("person_name"));
                @SuppressLint("Range") String userLastName = users.getString(users.getColumnIndex("person_surname"));
                TextView txtUser = findViewById(R.id.txtUser);
                txtUser.setText(userFirstName + " " + userLastName);
            }while(users.moveToNext());
        }users.close();

    }*/






