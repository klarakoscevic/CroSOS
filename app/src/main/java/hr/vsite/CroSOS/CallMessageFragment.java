package hr.vsite.CroSOS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import hr.vsite.myapplication.dbHelper;

public class CallMessageFragment extends Fragment implements View.OnClickListener {

    //region VARIABLES
    private Context context;
    private Button btnCall112;
    private Button btnCall194;
    private Button btnCall192;
    private Button btnCall193;
    private Button btnCallConsulate;

    private ExtendedFloatingActionButton btnMsgPerson1;
    private ExtendedFloatingActionButton btnMsgPerson2;
    private ExtendedFloatingActionButton btnMsgPerson3;
    private ExtendedFloatingActionButton btnMsgPerson4;
    private ExtendedFloatingActionButton btnMsgPerson5;
    private ExtendedFloatingActionButton btnMsgPerson6;
    private ExtendedFloatingActionButton btnMsgPerson7;
    private ExtendedFloatingActionButton btnMsgPerson8;

    private String msg = "";


    protected String[] mDataset;
    protected String[] idDataset;
    dbHelper db;
    //endregion

    public static CallMessageFragment newInstance() {
        CallMessageFragment fragment = new CallMessageFragment();
        return fragment;
    }

    public CallMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        db = new dbHelper(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_message, container, false);

        //region CALL
        btnCall112 = view.findViewById(R.id.btnCall112);
        btnCall112.setOnClickListener(this);

        btnCall194 = view.findViewById(R.id.btnCall194);
        btnCall194.setOnClickListener(this);

        btnCall193 = view.findViewById(R.id.btnCall193);
        btnCall193.setOnClickListener(this);

        btnCall192 = view.findViewById(R.id.btnCall192);
        btnCall192.setOnClickListener(this);

        btnCallConsulate = view.findViewById(R.id.btnCallConsulat);
        btnCallConsulate.setOnClickListener(this);

        //endregion
        //region MESSAGE
        btnMsgPerson1 = view.findViewById(R.id.btnMsgPerson1);
        btnMsgPerson1.setOnClickListener(this);
        btnMsgPerson1.hide();

        btnMsgPerson2 = view.findViewById(R.id.btnMsgPerson2);
        btnMsgPerson2.setOnClickListener(this);
        btnMsgPerson2.hide();

        btnMsgPerson3 = view.findViewById(R.id.btnMsgPerson3);
        btnMsgPerson3.setOnClickListener(this);
        btnMsgPerson3.hide();

        btnMsgPerson4 = view.findViewById(R.id.btnMsgPerson4);
        btnMsgPerson4.setOnClickListener(this);
        btnMsgPerson4.hide();

        btnMsgPerson5 = view.findViewById(R.id.btnMsgPerson5);
        btnMsgPerson5.setOnClickListener(this);
        btnMsgPerson5.hide();

        btnMsgPerson6 = view.findViewById(R.id.btnMsgPerson6);
        btnMsgPerson6.setOnClickListener(this);
        btnMsgPerson6.hide();

        btnMsgPerson7 = view.findViewById(R.id.btnMsgPerson7);
        btnMsgPerson7.setOnClickListener(this);
        btnMsgPerson7.hide();

        btnMsgPerson8 = view.findViewById(R.id.btnMsgPerson8);
        btnMsgPerson8.setOnClickListener(this);
        btnMsgPerson8.hide();
        //endregion

        setActionButtonText();
        return view;
    }

    @SuppressLint("Range")
    private void setActionButtonText() {
        int i = 0;
        Cursor users = db.readAllPersonData();
        mDataset = new String[users.getCount()];
        idDataset = new String[users.getCount()];

        if (users.moveToFirst()) {
            do {
                mDataset[i] = users.getString(users.getColumnIndex("person_name"));
                idDataset[i] = users.getString(users.getColumnIndex("_id_person"));
                i++;
            } while (users.moveToNext());
        }
        users.close();

        switch (mDataset.length) {
            case 8:
                btnMsgPerson8.show();
                btnMsgPerson8.setText(mDataset[7]);
            case 7:
                btnMsgPerson7.show();
                btnMsgPerson7.setText(mDataset[6]);
            case 6:
                btnMsgPerson6.show();
                btnMsgPerson6.setText(mDataset[5]);
            case 5:
                btnMsgPerson5.show();
                btnMsgPerson5.setText(mDataset[4]);
            case 4:
                btnMsgPerson4.show();
                btnMsgPerson4.setText(mDataset[3]);
            case 3:
                btnMsgPerson3.show();
                btnMsgPerson3.setText(mDataset[2]);
            case 2:
                btnMsgPerson2.show();
                btnMsgPerson2.setText(mDataset[1]);
            case 1:
                btnMsgPerson1.show();
                btnMsgPerson1.setText(mDataset[0]);
                break;
        }
    }

    public void call() {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:+385914614160"));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        startActivity(phoneIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCall112:
            case R.id.btnCall194:
            case R.id.btnCall193:
            case R.id.btnCall192:
                call();
                break;
            case R.id.btnCallConsulat:
                callConsulate();
                break;
            case R.id.btnMsgPerson1:
                msg112(idDataset[0]);
                break;
            case R.id.btnMsgPerson2:
                msg112(idDataset[1]);
                break;
            case R.id.btnMsgPerson3:
                msg112(idDataset[2]);
                break;
            case R.id.btnMsgPerson4:
                msg112(idDataset[3]);
                break;
            case R.id.btnMsgPerson5:
                msg112(idDataset[4]);
                break;
            case R.id.btnMsgPerson6:
                msg112(idDataset[6]);
                break;
            case R.id.btnMsgPerson7:
                msg112(idDataset[7]);
                break;
            case R.id.btnMsgPerson8:
                msg112(idDataset[8]);
                break;
        }
    }

    @SuppressLint("Range")
    private void getData(String id) {
        Cursor user = db.readPersonDataByID(id);

        String firstName = "";
        String lastName = "";
        String allergies = "";
        String medicalConditions = "";
        String gender = "";
        String bloodType = "";
        String rhFactor = "";
        String dateOfBirth = "";

        if (user.moveToFirst()) {
            firstName = user.getString(user.getColumnIndex("person_name"));
            lastName = user.getString(user.getColumnIndex("person_surname"));
            allergies = user.getString(user.getColumnIndex("person_allergies"));
            medicalConditions = user.getString(user.getColumnIndex("person_medical_condition"));
            gender = user.getString(user.getColumnIndex("person_gender"));
            bloodType = user.getString(user.getColumnIndex("person_blood_type"));
            rhFactor = user.getString(user.getColumnIndex("person_rh_factor"));
            dateOfBirth = user.getString(user.getColumnIndex("person_date_of_birth"));
        }
        user.close();

        msg = firstName + " " + lastName +
                "\nDatum rođenja: " + dateOfBirth; //treba dodati plus akon lokacije
        //"Lokacija: " +

        if (gender.equals(getString(R.string.male))) {
            msg += "\nSpol: muško";
        } else {
            msg += "\nSpol: žensko";
        }

        if (!allergies.equals("")) {
            msg += "\nAlergije: " + allergies;
        }

        if (!medicalConditions.equals("")) {
            msg += "\nTrenutna medicinska stanja: " + medicalConditions;
        }

        if (!rhFactor.equals(getString(R.string.unknown)) && !bloodType.equals(getString(R.string.unknown))) {
            msg += "\nKrvna grupa: " + bloodType + " " + rhFactor;
        } else if (!bloodType.equals(getString(R.string.unknown))) {
            msg += "\nKrvna grupa: " + bloodType;
        }

    }

    private void msg112(String id) {
        getData(id);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent sentPI;
            String SENT = "SMS_SENT";

            //smsManager.sendTextMessage("+385112", null, msg, null, null);
            smsManager.sendTextMessage("+385955914303", null, msg, null, null);
            Toast.makeText(context, getString(R.string.sms_sent), Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void callConsulate() {
    }
}