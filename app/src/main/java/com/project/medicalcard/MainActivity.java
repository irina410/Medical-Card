package com.project.medicalcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    LinearLayout root;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    String userUID;
    public static final String APP_PREFERENCES = "mysettings";
    SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        root = findViewById(R.id.main);
        auth = FirebaseAuth.getInstance();

        Button button_signin_doctor = (Button) findViewById(R.id.doctor);

        button_signin_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindowDoctor();
            }
        });

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,InformationActivity.class);
                startActivity(intent);
            }
        });




    }


    private void showSignInWindowDoctor() {

        AlertDialog.Builder dilog = new AlertDialog.Builder(this);
        dilog.setTitle("Вход");
        dilog.setMessage("введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window_doctor = inflater.inflate(R.layout.signin_doctor, null);
        dilog.setView(sign_in_window_doctor);

        final EditText email = sign_in_window_doctor.findViewById(R.id.editTextTextEmailAddress);
        final EditText password = sign_in_window_doctor.findViewById(R.id.editTextTextPassword);

        dilog.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dilog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "введите вашу почту", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password.getText().toString())) {
                    Snackbar.make(root, "введите ваш пароль", Snackbar.LENGTH_LONG).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                userUID = auth.getUid();
                                mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString("docID", userUID);
                                editor.apply();

                                Intent intent = new Intent(MainActivity.this, DoctorActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "ошибка" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });


            }
        });
        dilog.show();

    }
}

//
//myRef.setValue("Hello, World!");

//// Read from the database
//myRef.addValueEventListener(new ValueEventListener() {
//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//        // This method is called once with the initial value and again
//        // whenever data at this location is updated.
//        String value = dataSnapshot.getValue(String.class);
//        Log.d(TAG, "Value is: " + value);
//    }
//
//    @Override
//    public void onCancelled(DatabaseError error) {
//        // Failed to read value
//        Log.w(TAG, "Failed to read value.", error.toException());
//    }
//});
