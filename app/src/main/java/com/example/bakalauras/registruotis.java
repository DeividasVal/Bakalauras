package com.example.bakalauras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class registruotis extends AppCompatActivity {

    TextInputEditText usernameField, passwordField, emailField, fullnameField;
    TextView loginTextField;
    Button signup;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registruotis);

        usernameField = findViewById(R.id.vartotojoVardasRegister);
        passwordField = findViewById(R.id.passwordRegister);
        emailField = findViewById(R.id.emailRegisterField);
        fullnameField = findViewById(R.id.fullnameField);

        loginTextField = findViewById(R.id.prisijungtiText);

        progress = findViewById(R.id.progressBar);

        signup = findViewById(R.id.registruotisButton);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String username, password, email, fullname;
                username = String.valueOf(usernameField.getText());
                password = String.valueOf(passwordField.getText());
                email = String.valueOf(emailField.getText());
                fullname = String.valueOf(fullnameField.getText());
                progress.setVisibility(View.VISIBLE);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[4];
                        field[0] = "kliento_slaptazodis";
                        field[1] = "pilnas_vardas";
                        field[2] = "kliento_vartotojo_vardas";
                        field[3] = "kliento_pastas ";
                        String[] data = new String[4];
                        data[0] = password;
                        data[1] = fullname;
                        data[2] = username;
                        data[3] = email;
                        PutData putData = new PutData("http://192.168.1.150/PHPscriptai/signup.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                progress.setVisibility(View.GONE);
                                String result = putData.getResult();
                                if (result.equals("Sign up Success"))
                                {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), prisijungti.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                                Log.i("PutData", result);
                            }
                        }
                    }
                });
            }
        });
        loginTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), prisijungti.class);
                startActivity(intent);
                finish();
            }
        });
    }
}