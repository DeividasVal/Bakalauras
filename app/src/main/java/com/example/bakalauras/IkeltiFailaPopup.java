package com.example.bakalauras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IkeltiFailaPopup extends AppCompatActivity {

    private Button select, uzdaryti, ikelti;
    private Uri filepath;
    private TextView failoPav, pavadinimas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_pasirinkti_faila);

        select = findViewById(R.id.pasirinktiFailaButton);
        uzdaryti = findViewById(R.id.uzdarytiIkeltiLanga);
        failoPav = findViewById(R.id.failoPavadinimas);
        pavadinimas = findViewById(R.id.pavadinimasPopup);
        ikelti = findViewById(R.id.ikeltiFaila);

        Intent intent = getIntent();
        int korepetitorius_id = (int) intent.getSerializableExtra("korepetitorius_id");
        int mokinioId = (int) intent.getSerializableExtra("mokinio_id");

        uzdaryti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
            }
        });

        ikelti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath != null) { // check if a file has been selected
                    new IkeltiFaila().execute(mokinioId, korepetitorius_id, filepath, pavadinimas.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Pasirinkite failą!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                String fileName = getFileName(filepath);
                failoPav.setText("Pasirinktas failas: " + fileName);
                Log.d("failas", fileName);
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

    public class IkeltiFaila extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            int mokinioId = (int) params[0];
            int korepetitorius_id = (int) params[1];
            Uri filepath = (Uri) params[2];
            String pavadinimas = (String) params[3];
            try {
                URL url = new URL("http://192.168.0.108/PHPscriptai/ikeltiFaila.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                long currentTimeMillis = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedTime = sdf.format(new Date(currentTimeMillis));
                // Set request parameters
                String data = "mokinio_id=" + mokinioId + "&korepetitoriaus_id=" + korepetitorius_id +
                        "&pavadinimas=" + pavadinimas + "&laikas_issiusta=" + formattedTime;

                InputStream inputStream = getContentResolver().openInputStream(filepath);
                byte[] fileBytes = getBytes(inputStream);

                // Set file parameter
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
                outputStream.writeBytes("Content-Disposition: form-data; name=\"korepetitoriaus_id\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(String.valueOf(korepetitorius_id));
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"mokinio_id\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(String.valueOf(mokinioId));
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"pavadinimas\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(pavadinimas);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"laikas_issiusta\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(formattedTime);
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
            Log.d("IkeltiFaila", result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}