package com.example.bakalauras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class registruotis extends AppCompatActivity {

    private EditText usernameField, passwordField, emailField, fullnameField;
    private TextView loginTextField;
    private Button signup;
    private ProgressBar progress;
    private RadioButton radioMokinys, radioKorepetitorius;

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
        signup = findViewById(R.id.registruotisButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioMokinys.isChecked()) {
                    String username, password, email, fullname;
                    username = String.valueOf(usernameField.getText());
                    password = String.valueOf(passwordField.getText());
                    email = String.valueOf(emailField.getText());
                    fullname = String.valueOf(fullnameField.getText());
                    if (!username.equals("") && !password.equals("") && !email.equals("") && !fullname.equals("")) {
                        new RegisterTask().execute(username, password, email, fullname, "registracijaMokinys");
                        Intent intent = new Intent(getApplicationContext(), prisijungti.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Visi laukai turi buti uzpildyti!", Toast.LENGTH_SHORT).show();
                    }
                } else if (radioKorepetitorius.isChecked()) {
                    String username, password, email, fullname;
                    username = String.valueOf(usernameField.getText());
                    password = String.valueOf(passwordField.getText());
                    email = String.valueOf(emailField.getText());
                    fullname = String.valueOf(fullnameField.getText());
                    if (!username.equals("") && !password.equals("") && !email.equals("") && !fullname.equals("")) {
                        new RegisterTask().execute(username, password, email, fullname, "registracijaKorepetitorius");
                        Intent intent = new Intent(getApplicationContext(), prisijungti.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Visi laukai turi buti uzpildyti!", Toast.LENGTH_SHORT).show();
                    }
                } else {
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

    private class RegisterTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            String fullname = params[3];
            String filename = params[4];
            URL url;
            try {
                if (filename == "registracijaMokinys") {
                    url = new URL("http://192.168.0.102/PHPscriptai/registracijaMokinys.php");
                } else {
                    url = new URL("http://192.168.0.102/PHPscriptai/registracijaKorepetitorius.php");
                }

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data;
                if (filename == "registracijaMokinys") {
                    data = "mokinio_el_pastas=" + email + "&mokinio_slaptazodis=" + password + "&pilnas_mokinio_vardas=" + fullname + "&mokinio_vartotojo_vardas=" + username;
                } else {
                    data = "korepetitoriaus_el_pastas=" + email + "&korepetitoriaus_slaptazodis=" + password + "&pilnas_korepetitoriaus_vardas=" + fullname + "&korepetitoriaus_vartotojo_vardas=" + username;
                }
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                // Read the response from the PHP script
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(registruotis.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}