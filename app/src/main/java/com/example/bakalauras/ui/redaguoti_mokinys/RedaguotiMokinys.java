package com.example.bakalauras.ui.redaguoti_mokinys;

import androidx.appcompat.app.ActionBar;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bakalauras.R;
import com.example.bakalauras.Prisijungti;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RedaguotiMokinys extends AppCompatActivity {

    private EditText vardasText, emailText, vartotojoVardasText, slaptazodisText, patvirtintiPassText;
    private Button pakeisti;
    private String vardas, email, vartotojoVardas, slaptazodis, patvirtinti;
    private ImageView pfp;
    private Uri filepath;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setTitle("Redaguoti profilį");
        setContentView(R.layout.activity_redaguoti_mokinys);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        vardasText = findViewById(R.id.vardasMokinys);
        emailText = findViewById(R.id.emailMokinysRedaguoti);
        vartotojoVardasText = findViewById(R.id.vartotojoMokinys);
        slaptazodisText = findViewById(R.id.slaptazodisRedaguotiMokinys);
        patvirtintiPassText = findViewById(R.id.slaptazodisPatvirtintiRedaguotiMokinys);
        pakeisti = findViewById(R.id.redaguotiMokinysButton);
        pfp = findViewById(R.id.redaguotiMokinysPFP);

        if (Prisijungti.currentMokinys.getMokinioNuotrauka().isEmpty())
        {
            pfp.setBackgroundResource(R.drawable.ic_baseline_account_circle_24);
        }
        else
        {
            Picasso.get()
                    .load("http://192.168.0.108/PHPscriptai/" + Prisijungti.currentMokinys.getMokinioNuotrauka())
                    .transform(new CircleTransform())
                    .into(pfp);
        }

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }
        });

        pakeisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vardas = String.valueOf(vardasText.getText());
                email = String.valueOf(emailText.getText());
                vartotojoVardas = String.valueOf(vartotojoVardasText.getText());
                slaptazodis = String.valueOf(slaptazodisText.getText());
                patvirtinti = String.valueOf(patvirtintiPassText.getText());

                if (vardas.isEmpty() || email.isEmpty() || vartotojoVardas.isEmpty() || slaptazodis.isEmpty() || patvirtinti.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "uzpildykite visus laukus", Toast.LENGTH_SHORT).show();
                } else if (!slaptazodis.isEmpty() && !patvirtinti.isEmpty() && !slaptazodis.equals(patvirtinti)) {
                    Toast.makeText(getApplicationContext(), "Slaptazodziai nesutampa", Toast.LENGTH_SHORT).show();
                } else {
                    if (filepath == null)
                    {
                        PakeistiMokinioDuomenisBeNuotraukos task = new PakeistiMokinioDuomenisBeNuotraukos(Prisijungti.currentMokinys.getId(), vardas, email, vartotojoVardas, slaptazodis);
                        task.execute();
                        Prisijungti.currentKorepetitorius = null;
                        Prisijungti.currentMokinys = null;
                        Intent intent = new Intent(getApplicationContext(), Prisijungti.class);
                        startActivity(intent);
                    }
                    else
                    {
                        PakeistiMokinioDuomenis task = new PakeistiMokinioDuomenis(Prisijungti.currentMokinys.getId(), vardas, email, vartotojoVardas, slaptazodis, filepath);
                        task.execute();
                        Prisijungti.currentKorepetitorius = null;
                        Prisijungti.currentMokinys = null;
                        Intent intent = new Intent(getApplicationContext(), Prisijungti.class);
                        startActivity(intent);
                    }
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
                pfp.setImageBitmap(output);
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

    private class PakeistiMokinioDuomenisBeNuotraukos extends AsyncTask<Object, Void, String> {

        private int mokinioId;
        private String fullname;
        private String email;
        private String username;
        private String password;

        public PakeistiMokinioDuomenisBeNuotraukos(int mokinioId, String fullname, String email, String username, String password) {
            this.mokinioId = mokinioId;
            this.fullname = fullname;
            this.email = email;
            this.username = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(Object... params) {
            URL url;
            try {
                url = new URL("http://192.168.0.108/PHPscriptai/mokinysAtnaujintiDuomenisBeNuotraukos.php");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String data = "mokinio_id=" + mokinioId + "&mokinio_slaptazodis=" + password + "&pilnas_mokinio_vardas=" + fullname + "&mokinio_vartotojo_vardas=" + username + "&mokinio_el_pastas=" + email;
                Log.d("result", data);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

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
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    private class PakeistiMokinioDuomenis extends AsyncTask<String, Void, String> {

        private int mokinioId;
        private String vardas;
        private String email;
        private String vartotojoVardas;
        private String slaptazodis;
        private Uri filePath;

        public PakeistiMokinioDuomenis(int mokinioId, String vardas, String email, String vartotojoVardas, String slaptazodis, Uri filePath) {
            this.mokinioId = mokinioId;
            this.vardas = vardas;
            this.email = email;
            this.vartotojoVardas = vartotojoVardas;
            this.slaptazodis = slaptazodis;
            this.filePath = filePath;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://192.168.0.108/PHPscriptai/mokinysAtnaujintiDuomenis.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                String data = "mokinio_id=" + mokinioId + "&pilnas_mokinio_vardas=" + vardas + "&mokinio_vartotojo_vardas=" + vartotojoVardas + "&mokinio_slaptazodis=" + slaptazodis
                        + "&mokinio_el_pastas=" + email;
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
                outputStream.writeBytes("Content-Disposition: form-data; name=\"mokinio_id\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(String.valueOf(mokinioId));
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"pilnas_mokinio_vardas\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(vardas);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"mokinio_vartotojo_vardas\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(vartotojoVardas);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"mokinio_slaptazodis\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(slaptazodis);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"mokinio_el_pastas\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(email);
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
        protected void onPostExecute(String response) {
            Log.d("update ", response);
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
        }
    }

    public class CircleTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}