package hr.vsite.CroSOS;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.List;

public class UserModel {
    public final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
    public Selection selectionBloodType = new Selection();
    public Selection selectionRhFactor = new Selection();
    public Selection selectionGender = new Selection();
    public Selection selectionCountry = new Selection();
    public DatePickerDialog datePickerDialog;
    public Button btnDateOfBirth;
    private String[] blood_type_array;
    private String[] rh_factor_array;
    private List<String> country_array;

    public DatePickerDialog.OnDateSetListener datePickerDialogOnSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            month = month + 1;
            btnDateOfBirth.setText(day + "." + month + "." + year);
        }
    };
    public final AdapterView.OnItemSelectedListener spnrRhFacotorOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectionRhFactor.setMySelection(rh_factor_array[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    public final AdapterView.OnItemSelectedListener spnrCountryOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            selectionCountry.setMySelection(country_array.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    public final AdapterView.OnItemSelectedListener spnrBloodTypeOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectionBloodType.setMySelection(blood_type_array[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public void setBloodTypeArray(String[] bloodTypeArray) {
        blood_type_array = bloodTypeArray;
    }
    public void setRhFactorArray(String[] rhFactorArray) {
        rh_factor_array = rhFactorArray;
    }
    public void setCountryArray(List<String> countryArray){ country_array = countryArray;
    }
}
