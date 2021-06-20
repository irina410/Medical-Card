package com.project.medicalcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class RedactActivity extends AppCompatActivity {
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "name";
    String BloodType = "";
    String data = "";
    private ImageView imageView;
    private final int Pick_image = 1;
    String allerg = "";
    String doctorUID, ipn;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private String userId;
    private DatabaseReference mMovieRef;

    TextView currentDate;
    Calendar date = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redact);
        final EditText editTextName = (EditText) findViewById(R.id.editTextTextPersonName);
        final EditText editTextLastName = (EditText) findViewById(R.id.editTextTextPersonLastName);
        final EditText editTextFatherName = (EditText) findViewById(R.id.editTextTextFatherName);
        final EditText editTextdiseases = (EditText) findViewById(R.id.editTextdiseases);
        final EditText editTextphone = (EditText) findViewById(R.id.editTextPhone);
        final EditText diseases = (EditText) findViewById(R.id.diseases);
        final EditText all = (EditText) findViewById(R.id.editTextTextMultiLine);
        final EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        final TextView dateOfB = (TextView) findViewById(R.id.dateOfB);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        reference =  FirebaseDatabase.getInstance().getReference();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        doctorUID = mSettings.getString("docID", "");
        ipn = mSettings.getString("selectedItem", "");


        databaseReference.child(doctorUID).child(ipn).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    Patient patient = dataSnapshot.getValue(Patient.class);
                    editTextLastName.setText(patient.getLastname());
                    editTextName.setText(patient.getName());
                    editTextFatherName.setText(patient.getFathername());
                    editTextdiseases.setText(patient.getDiseases());
                   // personalPhone.setText(patient.getPersonalPhone());
                    editTextphone.setText(patient.getPhone());
                    email.setText(patient.getEmail());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        imageView = (ImageView) findViewById(R.id.imageView);

        //Связываемся с нашей кнопкой Button:
       // Button PickImage = (Button) findViewById(R.id.buttonfoto);
        //Настраиваем для нее обработчик нажатий OnClickListener:
       // PickImage.setOnClickListener(new View.OnClickListener() {

//            @Override
//            public void onClick(View view) {
//
//                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                //Тип получаемых объектов - image:
//                photoPickerIntent.setType("image/*");
//                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
//                startActivityForResult(photoPickerIntent, Pick_image);
//            }
//        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    allerg = "присутствуют";
                } else {
                    allerg = "отсутствуют";
                }
            }
        });


        SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);


        currentDate = (TextView) findViewById(R.id.dateOfB);

        Button buttonDate = (Button) findViewById(R.id.dateButton);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
                //data = currentDate.toString();

            }
        });


        String[] blood_t = {"группа крови", "O(I)", "A(II)", "B(III)", "AB(IV)"};
        String[] rhesus = {"резус фактор", "Rh+", "Rh-"};
        Spinner spinner_bl = (Spinner) findViewById(R.id.spinner_bl);
        Spinner spinner_rh = (Spinner) findViewById(R.id.spinner_rh);

        ArrayAdapter<String> adapter_bl = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, blood_t);
        ArrayAdapter<String> adapter_rh = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rhesus);


        adapter_bl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_bl.setAdapter(adapter_bl);

        adapter_rh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rh.setAdapter(adapter_rh);

        AdapterView.OnItemSelectedListener itemSelectedListener_bloodtype = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemblood = (String) parent.getItemAtPosition(position);
                BloodType = itemblood + " ";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner_bl.setOnItemSelectedListener(itemSelectedListener_bloodtype);


        AdapterView.OnItemSelectedListener itemSelectedListener_rh = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemrh = (String) parent.getItemAtPosition(position);
                BloodType = BloodType + itemrh;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinner_rh.setOnItemSelectedListener(itemSelectedListener_rh);


        Button button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            Patient patient ;
            @Override
            public void onClick(View v) {

                databaseReference.child(doctorUID).child(ipn).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                           patient = dataSnapshot.getValue(Patient.class);

                            patient.setName(editTextName.getText().toString());
                            patient.setLastname(editTextLastName.getText().toString());
                            patient.setFathername(editTextFatherName.getText().toString());
                            patient.setDateOfB(data);
                            patient.setTypeOfBl(BloodType);
                            patient.setAllerg(allerg);
                            patient.setDiseases(editTextdiseases.getText().toString());
                            patient.setPhone(editTextphone.getText().toString());
//                            patient.setPersonalPhone(personalPhone.getText().toString());





                        reference.child(doctorUID).child(mSettings.getString("selectedItem","")).setValue(patient);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


                Intent intent = new Intent(RedactActivity.this, DoctorActivity.class);

//                Bitmap bitmap = (Bitmap)intent.getExtras().get("imegeview");

                //             imageView.setImageBitmap(bitmap);

//                String[] myStrings = new String[]{editTextName.getText().toString(), editTextLastName.getText().toString(), editTextFatherName.getText().toString(), BloodType, data, editTextNumberSigned.getText().toString(),
//                        editTextdiseases.getText().toString(), editTextphone.getText().toString()};
//
//                intent.putExtra("strings", myStrings);


                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ed_name", editTextName.getText().toString());
                editor.putString("ed_lastname", editTextLastName.getText().toString());
                editor.putString("ed_fathername", editTextFatherName.getText().toString());
                editor.putString("blood_type", BloodType);
                editor.putString("date", data);
                editor.putString("diseases", editTextdiseases.getText().toString());
                editor.putString("phone", editTextphone.getText().toString());
                editor.putString("allerg", allerg);

                //editor.putString("imagePreferance", encodeTobase64(imageView));


                editor.commit();

                startActivity(intent);


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }


    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };


    private void setInitialDate() {

        currentDate.setText(DateUtils.formatDateTime(this,
                date.getTimeInMillis(),
                DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR));
        data = DateUtils.formatDateTime(this,
                date.getTimeInMillis(),
                DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
    }

    public void setDate() {
        new DatePickerDialog(RedactActivity.this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

}