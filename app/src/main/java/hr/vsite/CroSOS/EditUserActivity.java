package hr.vsite.CroSOS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import hr.vsite.myapplication.dbHelper;

public class EditUserActivity extends AppCompatActivity {
    public static final String ARG_PERSON_ID = "_id_person";
    UserModel userModel;

    private Date d;
    private RadioGroup rgGender;
    private String idPerson;
    private dbHelper db;
    private EditText firstName;
    private EditText lastName;
    private EditText allergies;
    private EditText medicalConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Intent intent = getIntent();
        idPerson = intent.getStringExtra(ARG_PERSON_ID);

        db = new dbHelper(this);
        userModel = new UserModel();

        userModel.setBloodTypeArray(getResources().getStringArray(R.array.blood_type_array));
        userModel.setRhFactorArray(getResources().getStringArray(R.array.rh_factor_array));
        userModel.btnDateOfBirth = findViewById(R.id.btnDateOfBirth);

        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        allergies = findViewById(R.id.txtAllergies);
        medicalConditions = findViewById(R.id.txtMedicalConditions);

        rgGender = findViewById(R.id.rgGender);
        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            int id = group.getCheckedRadioButtonId();
            RadioButton rb = findViewById(id);
            userModel.selectionGender.setMySelection(rb.getText().toString());
        });

        getPerson();
    }

    @SuppressLint("Range")
    private void getPerson() {
        Cursor user = db.readPersonDataByID(idPerson);
        String dateOfBirth = "";

        if (user.moveToFirst()) {
            firstName.setText(user.getString(user.getColumnIndex("person_name")));
            lastName.setText(user.getString(user.getColumnIndex("person_surname")));
            allergies.setText(user.getString(user.getColumnIndex("person_allergies")));
            medicalConditions.setText(user.getString(user.getColumnIndex("person_medical_condition")));
            userModel.selectionGender.setMySelection(user.getString(user.getColumnIndex("person_gender")));
            userModel.selectionBloodType.setMySelection(user.getString(user.getColumnIndex("person_blood_type")));
            userModel.selectionRhFactor.setMySelection(user.getString(user.getColumnIndex("person_rh_factor")));
            dateOfBirth = user.getString(user.getColumnIndex("person_date_of_birth"));
        }
        user.close();

        if (userModel.selectionGender.getMySelection().equals(getString(R.string.male)))
            rgGender.check(R.id.rbtnMale);
        else
            rgGender.check(R.id.rbtnFemale);

        userModel.btnDateOfBirth.setText(dateOfBirth);
        try {
            d = userModel.dateFormatter.parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        showSpinnerBloodTypeView();
        showSpinnerRhFactorView();
        initDatePicker();
    }

    private void showSpinnerBloodTypeView() {
        Spinner spinner = findViewById(R.id.spnrBloodType);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.blood_type_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(getIndex(spinner, userModel.selectionBloodType.getMySelection()));
        spinner.setOnItemSelectedListener(userModel.spnrBloodTypeOnItemSelectedListener);
    }

    private void showSpinnerRhFactorView() {
        Spinner spinner = findViewById(R.id.spnrRhFactor);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.rh_factor_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(getIndex(spinner, userModel.selectionRhFactor.getMySelection()));
        spinner.setOnItemSelectedListener(userModel.spnrRhFacotorOnItemSelectedListener);
    }

    //region datePicker

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener datePickerDialogListener = userModel.datePickerDialogOnSetListener;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        userModel.datePickerDialog = new DatePickerDialog(this, datePickerDialogListener, year, month, day);
        userModel.datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void OpenDatePicker(View view) {
        userModel.datePickerDialog.show();
    }
    //endregion

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value))
                return i;
        }
        return 0;
    }

    public void SavePerson(View view) {
        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();

        if (fName.matches("")) {
            Toast.makeText(this, R.string.none_first_name_msg, Toast.LENGTH_SHORT).show();
            return;
        } else if (lName.matches("")) {
            Toast.makeText(this, R.string.none_last_name_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        long result = db.updatePersonData(idPerson,
                fName,
                lName,
                userModel.selectionGender.getMySelection(),
                userModel.btnDateOfBirth.getText().toString(),
                "none",
                userModel.selectionBloodType.getMySelection(),
                userModel.selectionRhFactor.getMySelection(),
                allergies.getText().toString(),
                medicalConditions.getText().toString());

        String msg;

        if (result == -1) {
            msg = MessageFormat.format(getString(R.string.failed_update_person_msg), fName, lName);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            msg = MessageFormat.format(getString(R.string.successfully_update_person_msg), fName, lName);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ShowUsersActivity.class);
            startActivity(intent);
            finish();

        }
    }

    //region menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit_user_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mbtnDelete) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.delete_user));
            alertDialog.setMessage(getString(R.string.delete_user_question));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                    (dialogInterface, i) -> deletePerson());
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                    (dialogInterface, i) -> Toast.makeText(EditUserActivity.this, getString(R.string.delete_user_no), Toast.LENGTH_SHORT).show());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePerson(){
        long result = db.deletePersonByID(idPerson);
        if (result == -1) {
            Toast.makeText(getApplicationContext(), getString(R.string.failed_delete_person_msg), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.successfully_delete_person_msg), Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(EditUserActivity.this, ShowUsersActivity.class);
        startActivity(intent);
        finish();

    }
    //endregion
}