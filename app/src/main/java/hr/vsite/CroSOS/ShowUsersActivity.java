package hr.vsite.CroSOS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hr.vsite.myapplication.dbHelper;

public class ShowUsersActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    protected CustomAdapter mAdapter;
    protected String[] mDataset;
    protected String[] idDataset;
    protected RecyclerView.LayoutManager mLayoutManager;
    dbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_users);

        db = new dbHelper(this);
        rvUsers = findViewById(R.id.rvUsers);

        mLayoutManager = new LinearLayoutManager(this);

        setRecyclerViewLayoutManager(mLayoutManager);

        initDataset();

        mAdapter = new CustomAdapter(mDataset, idDataset);
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

    @SuppressLint("Range")
    private void initDataset() {
        int i = 0;
        Cursor users = db.readAllPersonData();
        mDataset = new String[users.getCount()];
        idDataset = new String[users.getCount()];

        if (users.moveToFirst()) {
            do {
                @SuppressLint("Range") String userFirstName = users.getString(users.getColumnIndex("person_name"));
                @SuppressLint("Range") String userLastName = users.getString(users.getColumnIndex("person_surname"));
                mDataset[i] = userFirstName + " " + userLastName;
                idDataset[i] = users.getString(users.getColumnIndex("_id_person"));
                i++;
            } while (users.moveToNext());
        }
        users.close();
    }

    //region menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_show_users_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mbtnDeleteAll:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(getString(R.string.delete_persons));
                alertDialog.setMessage(getString(R.string.delete_persons_question));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                        (dialogInterface, i) -> deleteAllPeople());
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                        (dialogInterface, i) -> Toast.makeText(ShowUsersActivity.this, getString(R.string.delete_persons_no), Toast.LENGTH_SHORT).show());
                alertDialog.show();
                break;
            case R.id.mbtnAddPerson:
                Intent intent = new Intent(ShowUsersActivity.this, CreateUserActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPeople(){
        db.deleteAllPersons();
        Toast.makeText(this, getString(R.string.successfully_delete_persons_msg), Toast.LENGTH_SHORT).show();
        this.recreate();
    }
    //endregion
}






