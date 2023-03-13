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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class registruotis extends AppCompatActivity {

    TextInputEditText usernameField, passwordField, emailField, fullnameField;
    TextView loginTextField;
    Button signup;
    ProgressBar progress;
    RadioButton radioMokinys, radioKorepetitorius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registruotis);
        radioMokinys = findViewById(R.id.mokinioPasirinkimas);
        radioKorepetitorius = findViewById(R.id.korepetitoriausPasirinkimas);
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
                    if (radioMokinys.isChecked())
                    {
                        String username, password, email, fullname;
                        username = String.valueOf(usernameField.getText());
                        password = String.valueOf(passwordField.getText());
                        email = String.valueOf(emailField.getText());
                        fullname = String.valueOf(fullnameField.getText());
                        progress.setVisibility(View.VISIBLE);
                        if (!username.equals("") && !password.equals("") && !email.equals("") && !fullname.equals(""))
                        {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[4];
                                    field[0] = "pilnas_mokinio_vardas";
                                    field[1] = "mokinio_vartotojo_vardas";
                                    field[2] = "mokinio_slaptazodis";
                                    field[3] = "mokinio_el_pastas";
                                    String[] data = new String[4];
                                    data[0] = fullname;
                                    data[1] = username;
                                    data[2] = password;
                                    data[3] = email;
                                    PutData putData = new PutData("http://192.168.1.150/PHPscriptai/signupMokinys.php", "POST", field, data);
                                    if (putData.startPut()) {
                                        if (putData.onComplete()) {
                                            progress.setVisibility(View.GONE);
                                            String result = putData.getResult();
                                            if (result.equals("Registracija sekminga"))
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
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Visi laukai turi buti uzpildyti!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (radioKorepetitorius.isChecked())
                    {
                        String username, password, email, fullname;
                        username = String.valueOf(usernameField.getText());
                        password = String.valueOf(passwordField.getText());
                        email = String.valueOf(emailField.getText());
                        fullname = String.valueOf(fullnameField.getText());
                        progress.setVisibility(View.VISIBLE);
                        if (!username.equals("") && !password.equals("") && !email.equals("") && !fullname.equals(""))
                        {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String[] field = new String[4];
                                    field[0] = "pilnas_korepetitoriaus_vardas";
                                    field[1] = "korepetitoriaus_vartotojo_vardas";
                                    field[2] = "korepetitoriaus_slaptazodis";
                                    field[3] = "korepetitoriaus_el_pastas";
                                    String[] data = new String[4];
                                    data[0] = fullname;
                                    data[1] = username;
                                    data[2] = password;
                                    data[3] = email;
                                    PutData putData = new PutData("http://192.168.1.150/PHPscriptai/signupKorepetitorius.php", "POST", field, data);
                                    if (putData.startPut()) {
                                        if (putData.onComplete()) {
                                            progress.setVisibility(View.GONE);
                                            String result = putData.getResult();
                                            if (result.equals("Registracija sekminga"))
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
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Visi laukai turi buti uzpildyti!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Pasirinkite: MOKINYS ar KOREPETITORIUS!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            loginTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getApplicationContext(), prisijungti.class);
                startActivity(intentLogin);
                finish();
            }
        });
        }
    }