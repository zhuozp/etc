package com.gibbon.etc.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gibbon.etc.annotation.RequestPermissions;
import com.gibbon.etc.annotation.UiThread;
import com.gibbon.etc.callback.IPermissionCallback;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

        takPhoto(new IPermissionCallback() {
            @Override
            public void permissionGrantedState() {
                Toast.makeText(MainActivity.this, "permission is not granted", Toast.LENGTH_LONG).show();
            }
        });
    }

    @RequestPermissions({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void takPhoto(IPermissionCallback callback) {
        Toast.makeText(this, "permission is granted", Toast.LENGTH_LONG).show();
    }

    @UiThread(delay = 1000)
    private void click() {
        textView.setText(String.valueOf(new Random().nextInt()));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
