package com.example.bakalauras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class prisijungti extends AppCompatActivity {

    TextView registerText;
    Button login;
    RadioButton radioMokinys, radioKorepetitorius;
    TextInputEditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisijungti);
        registerText = findViewById(R.id.registerText);
        login = findViewById(R.id.prisijungtiButton);
        radioMokinys = findViewById(R.id.mokinioPasirinkimasLogin);
        radioKorepetitorius = findViewById(R.id.korepetitoriausPasirinkimasLogin);
        usernameField = findViewById(R.id.prisijungtiVardas);
        passwordField = findViewById(R.id.slaptazodisPrisijungti);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioMokinys.isChecked())
                {
                    String username, password;
                    username = String.valueOf(usernameField.getText());
                    password = String.valueOf(passwordField.getText());
                    if (!username.equals("") && !password.equals(""))
                    {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[2];
                                field[0] = "mokinio_vartotojo_vardas";
                                field[1] = "mokinio_slaptazodis";
                                String[] data = new String[2];
                                data[0] = username;
                                data[1] = password;
                                PutData putData = new PutData("http://192.168.1.150/PHPscriptai/loginMokinys.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if (result.equals("Prisijungimas sekmingas"))
                                        {
                                            Toast.makeText(getApplicationContext(), "Sveiki atvyke, " + username, Toast.LENGTH_SHORT).show();
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
                    String username, password;
                    username = String.valueOf(usernameField.getText());
                    password = String.valueOf(passwordField.getText());
                    if (!username.equals("") && !password.equals(""))
                    {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[2];
                                field[0] = "korepetitoriaus_vartotojo_vardas";
                                field[1] = "korepetitoriaus_slaptazodis";
                                String[] data = new String[2];
                                data[0] = username;
                                data[1] = password;
                                PutData putData = new PutData("http://192.168.1.150/PHPscriptai/loginKorepetitorius.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if (result.equals("Prisijungimas sekmingas"))
                                        {
                                            Toast.makeText(getApplicationContext(), "Sveiki atvyke, " + username, Toast.LENGTH_SHORT).show();
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
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registruotis.class);
                startActivity(intent);
                finish();
            }
        });
    }
}