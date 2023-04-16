package com.yy.ekawaiishop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.yy.ekawaiishop.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterEmail, editTextRegisterName, editTextRegisterPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
        showHidePassword();

        Button ButtonRegister = findViewById(R.id.btnRegisterPageRegister);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextRegisterEmail.getText().toString();
                String textPwd = editTextRegisterPassword.getText().toString();
                String textName = editTextRegisterName.getText().toString();

                if (TextUtils.isEmpty(textName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    editTextRegisterName.setError("Name is required");
                    editTextRegisterName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Valid email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)){
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPassword.setError("password is required");
                    editTextRegisterPassword.requestFocus();
                } else if (textPwd.length() < 6){
                    Toast.makeText(RegisterActivity.this, "password too short!", Toast.LENGTH_SHORT).show();
                    editTextRegisterPassword.setError("Password should be at least 6 digits");
                    editTextRegisterPassword.requestFocus();
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textEmail, textName, textPwd);
                }
            }
        });
    }


    private void showHidePassword() {
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);

        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextRegisterPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                } else {
                    editTextRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });
    }

    private void findViews() {
        editTextRegisterEmail = findViewById(R.id.editTextRegisterPageEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPagePassword);
        editTextRegisterName = findViewById(R.id.editTextRegisterPageName);
        progressBar = findViewById(R.id.progressBar);
    }

    private void registerUser(String textEmail, String textName, String textPwd) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Check iff the user creation is succesful
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    if(firebaseUser != null){
                        //Update display name of the user
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                                setDisplayName(textName).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        //open userprofileacivity after user is created
                        Intent UserProfileActivity = new Intent(RegisterActivity.this, OnBoardingActivity.class);

                        //Stop user from going back to Register Activity on pressing back button
                        UserProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(UserProfileActivity);
                        finish();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}