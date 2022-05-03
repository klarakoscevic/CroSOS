package hr.vsite.CroSOS;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.MessageFormat;
import java.util.Calendar;
import hr.vsite.myapplication.dbHelper;


public class EditUserActivity extends AppCompatActivity {
    public static final String ARG_PERSON_ID = "_id_person";
    private Selection selectionBloodType;
    private Selection selectionRhFactor;
    private Selection selectionGender;
    private DatePickerDialog datePickerDialog;
    private Button btnDateOfBirth;
    private RadioGroup rgGender;
    private String[] blood_type_array;
    private String[] rh_factor_array;
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

        selectionBloodType = new Selection();
        selectionRhFactor = new Selection();
        selectionGender = new Selection();
        blood_type_array = getResources().getStringArray(R.array.blood_type_array);
        rh_factor_array = getResources().getStringArray(R.array.rh_factor_array);
        btnDateOfBirth = findViewById(R.id.btnDateOfBirth);
        db = new dbHelper(this);

        firstName = findViewById(R.id.txtFirstName);
        lastName = findViewById(R.id.txtLastName);
        allergies = findViewById(R.id.txtAllergies);
        medicalConditions = findViewById(R.id.txtMedicalConditions);

        rgGender = findViewById(R.id.rgGender);
        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            int id = group.getCheckedRadioButtonId();
            RadioButton rb = findViewById(id);
            selectionGender.setMySelection(rb.getText().toString());
        });

        getPerson();
        showSpinnerBloodTypeView();
        showSpinnerRhFactorView();
        initDatePicker();
    }

    @SuppressLint("Range")
    private void getPerson() {


        Cursor user = db.readPersonDataByID(idPerson);

        if (user.moveToFirst()) {
                firstName.setText(user.getString(user.getColumnIndex("person_name")));
                lastName.setText(user.getString(user.getColumnIndex("person_surname")));
                allergies.setText(user.getString(user.getColumnIndex("person_allergies")));
                medicalConditions.setText(user.getString(user.getColumnIndex("person_medical_condition")));
                selectionGender.setMySelection(user.getString(user.getColumnIndex("person_gender")));
                selectionBloodType.setMySelection(user.getString(user.getColumnIndex("person_blood_type")));
                selectionRhFactor.setMySelection(user.getString(user.getColumnIndex("person_rh_factor")));
                btnDateOfBirth.setText(user.getString(user.getColumnIndex("person_date_of_birth")));

                if (selectionGender.getMySelection().equals(getString(R.string.male)))
                    rgGender.check(R.id.rbtnMale);
                else
                    rgGender.check(R.id.rbtnFemale);
        }
        user.close();
    }

    //region bloodType
    private void showSpinnerBloodTypeView() {
        Spinner spinner = findViewById(R.id.spnrBloodType);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.blood_type_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(getIndex(spinner, selectionBloodType.getMySelection()));
        spinner.setOnItemSelectedListener(spnrBloodTypeOnItemSelectedListener);
    }

    private final AdapterView.OnItemSelectedListener spnrBloodTypeOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectionBloodType.setMySelection(blood_type_array[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    //endregion

    //region rhFactor
    private void showSpinnerRhFactorView() {
        Spinner spinner = findViewById(R.id.spnrRhFactor);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.rh_factor_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(getIndex(spinner, selectionRhFactor.getMySelection()));
        spinner.setOnItemSelectedListener(spnrRhFacotorOnItemSelectedListener);
    }

    private final AdapterView.OnItemSelectedListener spnrRhFacotorOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectionRhFactor.setMySelection(rh_factor_array[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    //endregion

    //region datePicker
    DatePickerDialog.OnDateSetListener datePickerDialogOnSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month = month + 1;
            btnDateOfBirth.setText(day + "." + month + "." + year);
        }
    };

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener datePickerDialogListener = datePickerDialogOnSetListener;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, datePickerDialogListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void OpenDatePicker(View view) {
        datePickerDialog.show();
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
                selectionGender.getMySelection(),
                btnDateOfBirth.getText().toString(),
                "none",
                selectionBloodType.getMySelection(),
                selectionRhFactor.getMySelection(),
                allergies.getText().toString(),
                medicalConditions.getText().toString());

        String msg;

        if (result == -1) {
            msg = MessageFormat.format(getString(R.string.failed_update_person_msg), fName, lName);
        } else {
            msg = MessageFormat.format(getString(R.string.successfully_update_person_msg), fName, lName);
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ShowUsersActivity.class);
        startActivity(intent);
    }
}