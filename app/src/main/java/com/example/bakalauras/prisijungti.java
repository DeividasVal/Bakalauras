package com.example.bakalauras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakalauras.ui.korepetitorius.KorKurtiProfili;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Model.Korepetitorius;
import Model.Mokinys;

public class prisijungti extends AppCompatActivity {

    public TextView registerText;
    public Button login;
    public RadioButton radioMokinys, radioKorepetitorius;
    public EditText usernameField, passwordField;
    public static Mokinys currentMokinys;
    public static Korepetitorius currentKorepetitorius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisijungti);
        registerText = findViewById(R.id.registerText);
        login = findViewById(R.id.prisijungtiButton);
        radioMokinys = findViewById(R.id.kvalAukst);
        radioKorepetitorius = findViewById(R.id.kvalVid);
        usernameField = findViewById(R.id.prisijungtiVardas);
        passwordField = findViewById(R.id.slaptazodisPrisijungti);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioMokinys.isChecked()) {
                    String username, password;
                    username = String.valueOf(usernameField.getText());
                    password = String.valueOf(passwordField.getText());
                    if (!username.equals("") && !password.equals("")) {
                        LoginTask task = new LoginTask(username, password, "mokinys");
                        task.execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Visi laukai turi buti uzpildyti!", Toast.LENGTH_SHORT).show();
                    }
                } else if (radioKorepetitorius.isChecked()) {
                    String username, password;
                    username = String.valueOf(usernameField.getText());
                    password = String.valueOf(passwordField.getText());
                    if (!username.equals("") && !password.equals("")) {
                        LoginTask task = new LoginTask(username, password, "korepetitorius");
                        task.execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Visi laukai turi buti uzpildyti!", Toast.LENGTH_SHORT).show();
                    }
                } else {
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

    public static boolean validateLogin(String username, String password, String type) {
        try {
            URL url;
            String requestBody;
            if (type == "mokinys") {
                url = new URL("http://192.168.1.150/PHPscriptai/loginMokinys.php");
                requestBody = "mokinio_vartotojo_vardas=" + username + "&mokinio_slaptazodis=" + password;
            } else {
                url = new URL("http://192.168.1.150/PHPscriptai/loginKorepetitorius.php");
                requestBody = "korepetitoriaus_vartotojo_vardas=" + username + "&korepetitoriaus_slaptazodis=" + password;
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(requestBody);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            reader.close();

            return response.equals("true");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private String username;
        private String password;
        private String type;

        public LoginTask(String username, String password, String type) {
            this.username = username;
            this.password = password;
            this.type = type;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return validateLogin(username, password, type);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(getApplicationContext(), "Prisijungta sėkmingai!", Toast.LENGTH_SHORT).show();
                // Switch to the main window
                if (type.equals("mokinys")) {
                    new GetMokinysDataTask().execute(username);
                } else {
                    new GetKorepetitoriusDataTask().execute(username);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Klaida prisijungiant", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetMokinysDataTask extends AsyncTask<String, Void, Mokinys> {

        @Override
        protected Mokinys doInBackground(String... params) {
            String username = params[0];
            try {
                URL url = new URL("http://192.168.1.150/PHPscriptai/gautiMokini.php?mokinio_vartotojo_vardas=" + username);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
                    JSONObject obj = new JSONObject(output);
                    String column1 = obj.getString("mokinio_id");
                    String column2 = obj.getString("pilnas_mokinio_vardas");
                    String column3 = obj.getString("mokinio_vartotojo_vardas");
                    String column4 = obj.getString("mokinio_slaptazodis");
                    String column5 = obj.getString("mokinio_el_pastas");
                    currentMokinys = new Mokinys(Integer.parseInt(column1), column3, column4, column5, column2);
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return currentMokinys;
        }

        @Override
        protected void onPostExecute(Mokinys result) {
            Intent intent = new Intent(getApplicationContext(), pagrindinis_langas.class);
            intent.putExtra("mokinys", result);
            startActivity(intent);
        }
    }
    private class GetKorepetitoriusDataTask extends AsyncTask<String, Void, Korepetitorius> {

        @Override
        protected Korepetitorius doInBackground(String... params) {
            String username = params[0];
            try {
                URL url = new URL("http://192.168.1.150/PHPscriptai/gautiKorepetitoriu.php?korepetitoriaus_vartotojo_vardas=" + username);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
                    JSONObject obj = new JSONObject(output);
                    Log.d("response", output);
                    String column1 = obj.getString("korepetitoriaus_id");
                    String column2 = obj.getString("pilnas_korepetitoriaus_vardas");
                    String column3 = obj.getString("korepetitoriaus_vartotojo_vardas");
                    String column4 = obj.getString("korepetitoriaus_slaptazodis");
                    String column5 = obj.getString("korepetitoriaus_el_pastas");
                    currentKorepetitorius = new Korepetitorius(Integer.parseInt(column1), column3, column4, column5, column2);
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return currentKorepetitorius;
        }

        @Override
        protected void onPostExecute(Korepetitorius result) {
            Intent intent = new Intent(getApplicationContext(), pagrindinis_langas.class);
            intent.putExtra("korepetitorius", result);
            startActivity(intent);
        }
    }
}