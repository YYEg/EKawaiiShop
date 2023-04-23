package com.yy.ekawaiishop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yy.ekawaiishop.R;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    EditText name, address, city, postalCode, phoneNumber;
    Toolbar toolbar;
    Button addAdressButton;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        name = findViewById(R.id.ad_name);
        address = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        postalCode = findViewById(R.id.ad_code);
        phoneNumber = findViewById(R.id.ad_phone);

        addAdressButton = findViewById(R.id.ad_add_address);
        addAdressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userCity = city.getText().toString();
                String userCode = postalCode.getText().toString();
                String userPhone = phoneNumber.getText().toString();
                String userAddres = address.getText().toString();

                String final_addres = "";

                if (!userName.isEmpty()){
                    final_addres+=userName;
                    final_addres+=", ";
                }
                if (!userCity.isEmpty()){
                    final_addres+=userCity;
                    final_addres+=", ";
                }
                if (!userAddres.isEmpty()){
                    final_addres+=userAddres;
                    final_addres+=", ";
                }
                if (!userCode.isEmpty()){
                    final_addres+=userCode;
                    final_addres+=", ";
                }
                if (!userPhone.isEmpty()){
                    final_addres+=userPhone;
                }
                if(!userName.isEmpty() && !userCity.isEmpty() && !userAddres.isEmpty() && !userCode.isEmpty() && !userPhone.isEmpty()){

                    Map<String, String> map = new HashMap<>();
                    map.put("userAddress", final_addres);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(AddAddressActivity.this, "", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddAddressActivity.this, DetailedActivity.class));
                                        finish();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(AddAddressActivity.this, "Fill all fields, please!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}