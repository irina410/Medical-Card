package com.project.medicalcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientInformationActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private String userId;
    private DatabaseReference mMovieRef;
    public static final String APP_PREFERENCES = "mysettings";
    SharedPreferences mSettings;
    String doctorUID, ipn;
    TextView name, lastname, fathername, dateOfB, typeOfBl, allerg, diseases, pNumber, persjnalPhone, phone, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_information);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        doctorUID = mSettings.getString("docID", "");
        ipn = mSettings.getString("selectedItem", "");

        name = (TextView) findViewById(R.id.name);
        lastname = (TextView) findViewById(R.id.lastname);
        fathername = (TextView) findViewById(R.id.fathername);
        dateOfB = (TextView) findViewById(R.id.Date_of_Birth);
        typeOfBl = (TextView) findViewById(R.id.blood_type);
        allerg = (TextView) findViewById(R.id.textViewallergicreactions);
        diseases = (TextView) findViewById(R.id.textViewdiseases);
        persjnalPhone = (TextView) findViewById(R.id.textViewpersonalnumber);
        phone = (TextView) findViewById(R.id.textViewnumber);
        pNumber = (TextView) findViewById(R.id.insurance_policy_number);
        email = (TextView) findViewById(R.id.email);

        databaseReference.child(doctorUID).child("patient").child(ipn).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Patient patient = ds.getValue(Patient.class);
                    lastname.setText(patient.getLastname());
                    name.setText(patient.getName());
                    fathername.setText(patient.getFathername());
                    dateOfB.setText(patient.getDateOfB());
                    typeOfBl.setText(patient.getTypeOfBl());
                    allerg.setText("аллергические реакции:  "+ patient.getAllerg() );
                    diseases.setText(patient.getDiseases());
                    persjnalPhone.setText(patient.getPersonalPhone());
                    phone.setText(patient.getPhone());
                    pNumber.setText(patient.getPolicynumb());
                    email.setText(patient.getEmail());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        Button button_redact  = (Button) findViewById(R.id.buttonredact);
        button_redact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(PatientInformationActivity.this, RedactActivity.class);
                startActivity(intent);
            }
        });

        Button buttonb = (Button) findViewById(R.id.buttonb);
        buttonb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInformationActivity.this, DoctorActivity.class);
                startActivity(intent);
            }
        });


    }
}