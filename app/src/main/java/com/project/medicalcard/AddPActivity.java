package com.project.medicalcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    EditText name, lastname, email, ipn;
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

        doctorUID = mSettings.getString("docID", "");
        name = (EditText) findViewById(R.id.editTextTextPersonName);
        lastname = (EditText) findViewById(R.id.editTextTextPersonLastName);
        email = (EditText) findViewById(R.id.editTextTextEmailAddressadd);
        ipn = (EditText) findViewById(R.id.ipn);
        button_add = (Button) findViewById(R.id.button_add);
        patient = new Patient();
        reference = FirebaseDatabase.getInstance().getReference();
        tv = (TextView)findViewById(R.id.textView);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient.setName(name.getText().toString().trim());
                patient.setLastname(lastname.getText().toString());
                patient.setEmail(email.getText().toString());
                patient.setInsurance_policy_number(ipn.getText().toString());

                reference.child(doctorUID).child("patient").child(lastname.getText().toString()).setValue(patient);



                EAN13CodeBuilder bb = new EAN13CodeBuilder(ipn.getText().toString());
                bb.parse();
                tv.setText(bb.getCode());

            }
        });


    }
}