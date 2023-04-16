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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.yy.ekawaiishop.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        showHidePassword();

        TextView textViewRegister = findViewById(R.id.textViewLoginSignUp);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });

        auth = FirebaseAuth.getInstance();

        //пользователь логиниться
        Button buttonLogin = findViewById(R.id.btnLoginLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPassword.getText().toString();

                //check if all the data are present before signin in user
                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("email is required");
                    editTextLoginEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(LoginActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid email is required");
                    editTextLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)){
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextLoginPassword.setError("password is required");
                    editTextLoginPassword.requestFocus();
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPwd);
                }
            }
        });
    }

    private void loginUser(String textEmail, String textPwd) {
        auth.signInWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    Intent UserProfileActivity = new Intent(LoginActivity.this, OnBoardingActivity.class);
                    startActivity(UserProfileActivity);
                } else{
                    try {
                        throw task.getException();
                    } catch (Exception e){
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showHidePassword() {
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);

        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextLoginPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                } else {
                    editTextLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                }
            }
        });
    }

    private void findViews() {
        editTextLoginEmail = findViewById(R.id.editTextLoginPageEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPagePassword);
        progressBar = findViewById(R.id.progressBar);
    }
}