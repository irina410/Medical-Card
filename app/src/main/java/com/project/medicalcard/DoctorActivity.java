package com.project.medicalcard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DoctorActivity extends AppCompatActivity {
    ConstraintLayout root;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private String userId;
    private DatabaseReference mMovieRef;
    public static final String APP_PREFERENCES = "mysettings";
    SharedPreferences mSettings;
    String doctorUID = "";
    Intent intent;
    List<String> patients = new ArrayList<String>();
    private static final String TAG = "myLogs";
    ListView listView;
    ArrayAdapter<String> adapter;
    TextView name, lastname, fathername, dateOfB, typeOfBl, allerg, diseases, pNumber, persjnalPhone, phone, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        root = findViewById(R.id.root);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        doctorUID = mSettings.getString("docID", "");
        reference = databaseReference.child(doctorUID);
        auth = FirebaseAuth.getInstance();
        intent = new Intent(DoctorActivity.this, AddPActivity.class);
        listView = findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patients);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    Patient patient = ds.getValue(Patient.class);

                    String ipn = patient.getInsurance_policy_number() + "   " +
                            "" + patient.getLastname() + " " + patient.getName();

                    patients.add(ipn);
                }
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView textView = (TextView) view;
                        String[] si = ((String) textView.getText()).split(" ");
                        String selectedItem = si[0];

                        Log.d(TAG, selectedItem);
                        editor.remove("selectedItem");
                        editor.putString("selectedItem", selectedItem);

                        Log.d(TAG, mSettings.getString("selectedItem", "") + "  ......сохранение ");

                        Intent pInfo = new Intent(DoctorActivity.this, PatientInformationActivity.class);
                        startActivity(pInfo);
                        editor.commit();

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        Button addpat = (Button) findViewById(R.id.addpatient);
        addpat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);

            }
        });

        Button button_lout = (Button) findViewById(R.id.button_logout);
        button_lout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });


    }

    public void logout(View view) {
        SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        auth.signOut();
        Intent intent = new Intent(DoctorActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}

