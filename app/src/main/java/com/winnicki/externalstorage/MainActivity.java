package com.winnicki.externalstorage;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etData;
    TextView tvData;
    Button btnSaveEPrivate, btnLoadEPrivate, btnSaveEPublic, btnLoadEPublic;

    public static final int REQUEST_CODE = 1;
    String[] permissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);

        etData = (EditText)findViewById(R.id.etData);
        tvData = (TextView)findViewById(R.id.tvData);
        btnSaveEPrivate = (Button)findViewById(R.id.btnSaveEPrivate);
        btnLoadEPrivate = (Button)findViewById(R.id.btnLoadEPrivate);
        btnSaveEPublic = (Button)findViewById(R.id.btnSaveEPublic);
        btnLoadEPublic = (Button)findViewById(R.id.btnLoadEPublic);

        btnSaveEPrivate.setOnClickListener(this);
        btnLoadEPrivate.setOnClickListener(this);
        btnSaveEPublic.setOnClickListener(this);
        btnLoadEPublic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveEPrivate:
                saveExternalPrivate();
                break;
            case R.id.btnLoadEPrivate:
                loadExternalPrivate();
                break;
            case R.id.btnSaveEPublic:
                saveExternalPublic();
                break;
            case R.id.btnLoadEPublic:
                loadExternalPublic();
                break;
        }
    }

    private void saveExternalPrivate() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String data = etData.getText().toString();
            File folder = getExternalFilesDir("ExternalPrivateFolder");
            File fileId = new File(folder,"External_pr_file.txt");
            saveData(fileId,data);
            Log.d("PRIVATE",fileId.getAbsolutePath());
        }
    }

    private void loadExternalPrivate() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File folder = getExternalFilesDir("ExternalPrivateFolder");
            File fileId = new File(folder,"External_pr_file.txt");
            String data = loadData(fileId);
            tvData.setText(data);
            Log.d("PRIVATE",fileId.getAbsolutePath());
        }
    }

    private void saveExternalPublic() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String data = etData.getText().toString();
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File fileId = new File(folder,"External_public.txt");
            if(fileId.exists()) {
                fileId.delete();
            }
            saveData(fileId,data);
            Log.d("PUBLIC",fileId.getAbsolutePath());
        }
    }

    private void loadExternalPublic() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File fileId = new File(folder,"External_public.txt");
            String data = loadData(fileId);
            tvData.setText(data);
            Log.d("PUBLIC",fileId.getAbsolutePath());
        }
    }

    private void saveData(File fileId, String data) {
        try {
            FileOutputStream fos = new FileOutputStream(fileId);
            fos.write(data.getBytes());
            Toast.makeText(this, "Data saved successfully in " + fileId.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            fos.close();
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
    }

    private String loadData(File fileId) {
        try {
            FileInputStream fis = new FileInputStream(fileId);
            int read;
            StringBuilder sb = new StringBuilder();
            while ((read = fis.read()) != -1) {
                sb.append((char)read);
            }
            fis.close();
            return sb.toString();
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission accepted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
