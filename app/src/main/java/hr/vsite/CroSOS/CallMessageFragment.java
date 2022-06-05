package hr.vsite.CroSOS;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CallMessageFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private Button btnCall112;
    private Button btnMsg112;
    private Button btnCall194;
    private Button btnCall192;
    private Button btnCall193;
    private Button btnCallConsulate;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallMessageFragment newInstance(String param1, String param2) {
        CallMessageFragment fragment = new CallMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CallMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_message, container, false);

        btnCall112 = view.findViewById(R.id.btnCall112);
        btnCall112.setOnClickListener(this);

        btnMsg112 = view.findViewById(R.id.btnMsg112);
        btnMsg112.setOnClickListener(this);

        btnCall194 = view.findViewById(R.id.btnCall194);
        btnCall194.setOnClickListener(this);

        btnCall193 = view.findViewById(R.id.btnCall193);
        btnCall193.setOnClickListener(this);

        btnCall192 = view.findViewById(R.id.btnCall192);
        btnCall192.setOnClickListener(this);

        btnCallConsulate = view.findViewById(R.id.btnCallConsulat);
        btnCallConsulate.setOnClickListener(this);
        return view;
    }

    public void call() {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:+385914614160"));

        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);

        }
        startActivity(phoneIntent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnCall112:
            case R.id.btnCall194:
            case R.id.btnCall193:
            case R.id.btnCall192:
                call();
                break;
            case R.id.btnCallConsulat:
                callConsulate();
                break;
            case R.id.btnMsg112:
                msg112();
                break;
        }
    }

    private void msg112() {
    }

    private void callConsulate() {
    }
}