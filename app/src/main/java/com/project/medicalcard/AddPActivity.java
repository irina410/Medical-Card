package com.project.medicalcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPActivity extends AppCompatActivity {
    EditText name, lastname, fathername, email, ipn, pphone, allerg, diseases, typeOfBl, phone, date;
    TextView tv;
    Button button_add;
    DatabaseReference reference;
    Patient patient;
    public static final String APP_PREFERENCES = "mysettings";
    SharedPreferences mSettings;
    String doctorUID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_p);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Intent intent = new Intent(AddPActivity.this, DoctorActivity.class);
        doctorUID = mSettings.getString("docID", "");
        name = (EditText) findViewById(R.id.editTextPersonName);
        lastname = (EditText) findViewById(R.id.editTextPersonLastName);
        fathername = (EditText) findViewById(R.id.editTextPersonFatherName);
        email = (EditText) findViewById(R.id.editTextEmailAddressadd);
        ipn = (EditText) findViewById(R.id.ipn);
        pphone = (EditText) findViewById(R.id.editTextPersonPhone);
        allerg = (EditText) findViewById(R.id.allerg);
        diseases = (EditText) findViewById(R.id.diseases);
        phone = (EditText)findViewById(R.id.editTextPhone);
        date =(EditText) findViewById(R.id.editTextDate);
        typeOfBl = (EditText) findViewById(R.id.editTexttypeOfBl);
        button_add = (Button) findViewById(R.id.button_add);
        patient = new Patient();
        reference = FirebaseDatabase.getInstance().getReference();
        tv = (TextView) findViewById(R.id.textView);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient.setName(name.getText().toString().trim());
                patient.setLastname(lastname.getText().toString());
                patient.setFathername(fathername.getText().toString());
                patient.setPersonalPhone(pphone.getText().toString());
                patient.setEmail(email.getText().toString());
                patient.setAllerg(allerg.getText().toString());
                patient.setDiseases(diseases.getText().toString());
                patient.setInsurance_policy_number(ipn.getText().toString());
                patient.setDateOfB(date.getText().toString());
                patient.setTypeOfBl(typeOfBl.getText().toString());
                patient.setPhone(phone.getText().toString());

                reference.child(doctorUID).child(ipn.getText().toString()).setValue(patient);

                startActivity(intent);


            }
        });






        




    }
}