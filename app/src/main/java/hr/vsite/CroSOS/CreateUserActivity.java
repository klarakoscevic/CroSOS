package hr.vsite.CroSOS;

import hr.vsite.myapplication.*;
import android.app.DatePickerDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateUserActivity extends AppCompatActivity {

    private Selection selectionBloodType;
    private Selection selectionRhFactor;
    private Selection selectionGender;
    private DatePickerDialog datePickerDialog;
    private Button btnDateOfBirth;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
    private String[] blood_type_array;
    private String[] rh_factor_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        selectionBloodType = new Selection();
        selectionRhFactor = new Selection();
        selectionGender = new Selection();
        blood_type_array = getResources().getStringArray(R.array.blood_type_array);
        rh_factor_array = getResources().getStringArray(R.array.rh_factor_array);

        btnDateOfBirth = findViewById(R.id.btnDateOfBirth);
        btnDateOfBirth.setText(getTodayDate());

        RadioGroup rgGender = findViewById(R.id.rgGender);

        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            int id = group.getCheckedRadioButtonId();
            RadioButton rb=findViewById(id);
            selectionGender.setMySelection(rb.getText().toString());
        });

        initDatePicker();
        showSpinnerBloodTypeView();
        showSpinnerRhFactorView();

    }

    //region datePicker
    @NonNull
    private String getTodayDate() {
        Date currentDate = Calendar.getInstance().getTime();
        return dateFormatter.format(currentDate);
    }

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

    //region bloodType
    private void showSpinnerBloodTypeView() {
        Spinner spinner = findViewById(R.id.spnrBloodType);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.blood_type_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spnrBloodTypeOnItemSelectedListener);
    }
    private AdapterView.OnItemSelectedListener spnrBloodTypeOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
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
        spinner.setOnItemSelectedListener(spnrRhFacotorOnItemSelectedListener);
    }
    private AdapterView.OnItemSelectedListener spnrRhFacotorOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectionRhFactor.setMySelection(rh_factor_array[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    //endregion

    public void SavePerson(View view) {
        EditText firstName = findViewById(R.id.txtFirstName);
        EditText lastName = findViewById(R.id.txtLastName);
        EditText allergies = findViewById(R.id.txtAllergies);
        EditText medicalConditions = findViewById(R.id.txtMedicalConditions);

        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();

        if (fName.matches("")) {
            Toast.makeText(this, R.string.none_first_name_msg, Toast.LENGTH_SHORT).show();
            return;
        } else if (lName.matches("")) {
            Toast.makeText(this, R.string.none_last_name_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper db = new dbHelper(this);
        long result = db.addPerson(fName,
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
            msg = MessageFormat.format(getString(R.string.failed_create_person_msg), fName, lName);
        } else {
            msg = MessageFormat.format(getString(R.string.successfully_create_person_msg), fName, lName);
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}