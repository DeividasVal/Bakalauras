package com.example.bakalauras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Registruotis extends AppCompatActivity {

    private EditText usernameField, passwordField, emailField, fullnameField;
    private TextView loginTextField;
    private Button signup;
    private RadioButton radioMokinys, radioKorepetitorius;
    private ImageView profilioNuotrauka;
    private Uri filepath;

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
        profilioNuotrauka = findViewById(R.id.profilioNuotrauka);

        profilioNuotrauka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }
        });

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
                        new RegisterTask().execute(username, password, email, fullname, "registracijaMokinys", filepath);
                        Intent intent = new Intent(getApplicationContext(), Prisijungti.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Visi laukai turi buti užpildyti!", Toast.LENGTH_SHORT).show();
                    }
                } else if (radioKorepetitorius.isChecked()) {
                    String username, password, email, fullname;
                    username = String.valueOf(usernameField.getText());
                    password = String.valueOf(passwordField.getText());
                    email = String.valueOf(emailField.getText());
                    fullname = String.valueOf(fullnameField.getText());
                    if (!username.equals("") && !password.equals("") && !email.equals("") && !fullname.equals("")) {
                        if (filepath != null) {
                            new RegisterTask().execute(username, password, email, fullname, "registracijaKorepetitorius", filepath);
                            Intent intent = new Intent(getApplicationContext(), Prisijungti.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Pasirinkite nuotrauką!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Visi laukai turi buti užpildyti!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Pasirinkite: MOKINYS ar KOREPETITORIUS!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginTextField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getApplicationContext(), Prisijungti.class);
                startActivity(intentLogin);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int size = Math.min(width, height);
                Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);
                profilioNuotrauka.setImageBitmap(output);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private class RegisterTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            String username = (String) params[0];
            String password = (String) params[1];
            String email = (String) params[2];
            String fullname = (String) params[3];
            String filename = (String) params[4];
            Uri filepath = (Uri) params[5];
            URL url;
            try {
                if (filename == "registracijaMokinys") {
                    url = new URL("http://192.168.0.101/PHPscriptai/registracijaMokinys.php");
                } else {
                    url = new URL("http://192.168.0.101/PHPscriptai/registracijaKorepetitorius.php");
                }

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                if (filename == "registracijaMokinys") {
                    String data = "mokinio_el_pastas=" + email + "&mokinio_slaptazodis=" + password + "&pilnas_mokinio_vardas=" + fullname + "&mokinio_vartotojo_vardas=" + username;

                    InputStream inputStream = getContentResolver().openInputStream(filepath);
                    byte[] fileBytes = getBytes(inputStream);

                    String boundary = "*****";
                    String twoHyphens = "--";
                    String lineEnd = "\r\n";
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" +
                            getFileName(filepath) + "\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.write(fileBytes);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"mokinio_el_pastas\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(email);
                    outputStream.writeBytes(lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"mokinio_slaptazodis\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(password);
                    outputStream.writeBytes(lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"pilnas_mokinio_vardas\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(fullname);
                    outputStream.writeBytes(lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"mokinio_vartotojo_vardas\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(username);
                    outputStream.writeBytes(lineEnd);

                    // Set request data parameter
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"data\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(data);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    outputStream.flush();
                    outputStream.close();
                } else {
                    String data = "korepetitoriaus_el_pastas=" + email + "&korepetitoriaus_slaptazodis=" + password + "&pilnas_korepetitoriaus_vardas=" + fullname + "&korepetitoriaus_vartotojo_vardas=" + username;

                    InputStream inputStream = getContentResolver().openInputStream(filepath);
                    byte[] fileBytes = getBytes(inputStream);

                    String boundary = "*****";
                    String twoHyphens = "--";
                    String lineEnd = "\r\n";
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" +
                            getFileName(filepath) + "\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.write(fileBytes);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"korepetitoriaus_el_pastas\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(email);
                    outputStream.writeBytes(lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"korepetitoriaus_slaptazodis\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(password);
                    outputStream.writeBytes(lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"pilnas_korepetitoriaus_vardas\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(fullname);
                    outputStream.writeBytes(lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"korepetitoriaus_vartotojo_vardas\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(username);
                    outputStream.writeBytes(lineEnd);

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"data\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(data);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    outputStream.flush();
                    outputStream.close();
                }

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
            Log.d("asdasdasdasd", result);
            Toast.makeText(Registruotis.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}