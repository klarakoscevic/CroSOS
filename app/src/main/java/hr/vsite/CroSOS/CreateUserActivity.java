package hr.vsite.CroSOS;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hr.vsite.myapplication.dbHelper;

public class CreateUserActivity extends AppCompatActivity {
    UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        userModel = new UserModel();
        userModel.selectionGender.setMySelection(getString(R.string.male));
        userModel.btnDateOfBirth = findViewById(R.id.btnDateOfBirth);
        userModel.btnDateOfBirth.setText(getTodayDate());

        userModel.setBloodTypeArray(getResources().getStringArray(R.array.blood_type_array));
        userModel.setRhFactorArray(getResources().getStringArray(R.array.rh_factor_array));

        RadioGroup rgGender = findViewById(R.id.rgGender);

        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = findViewById(checkedId);
            userModel.selectionGender.setMySelection(rb.getText().toString());
            String s = userModel.selectionGender.getMySelection();
        });

        initDatePicker();
        showSpinnerBloodTypeView();
        showSpinnerRhFactorView();
    }

    //region datePicker
    @NonNull
    private String getTodayDate() {
        Date currentDate = Calendar.getInstance().getTime();
        return userModel.dateFormatter.format(currentDate);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener datePickerDialogListener = userModel.datePickerDialogOnSetListener;
        Calendar calendar = Calendar.getInstance();
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


    private void showSpinnerBloodTypeView() {
        Spinner spinner = findViewById(R.id.spnrBloodType);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.blood_type_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(userModel.spnrBloodTypeOnItemSelectedListener);
    }

    private void showSpinnerRhFactorView() {
        Spinner spinner = findViewById(R.id.spnrRhFactor);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.rh_factor_array, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(userModel.spnrRhFacotorOnItemSelectedListener);
    }

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
                userModel.selectionGender.getMySelection(),
                userModel.btnDateOfBirth.getText().toString(),
                "none",
                userModel.selectionBloodType.getMySelection(),
                userModel.selectionRhFactor.getMySelection(),
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