package hr.vsite.CroSOS;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hr.vsite.myapplication.dbHelper;

public class ShowUsersActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    protected CustomAdapter mAdapter;
    protected String[] mDataset;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        rvUsers = findViewById(R.id.rvUsers);

        mLayoutManager = new LinearLayoutManager(this);

        setRecyclerViewLayoutManager(mLayoutManager);

        initDataset();

        mAdapter = new CustomAdapter(mDataset);
        rvUsers.setAdapter(mAdapter);
    }


    public void setRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManager) {
        int scrollPosition = 0;

        if (rvUsers.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) rvUsers.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        rvUsers.setLayoutManager(mLayoutManager);
        rvUsers.scrollToPosition(scrollPosition);
    }

    private void initDataset() {
        int i = 0;
        dbHelper db = new dbHelper(this);
        Cursor users = db.readAllPersonData();
        mDataset = new String[users.getCount()];

        if (users.moveToFirst()) {
            do {
                @SuppressLint("Range") String userFirstName = users.getString(users.getColumnIndex("person_name"));
                @SuppressLint("Range") String userLastName = users.getString(users.getColumnIndex("person_surname"));
                mDataset[i] = userFirstName + " " + userLastName;
                i++;
            } while (users.moveToNext());
        }
        users.close();
    }
}






