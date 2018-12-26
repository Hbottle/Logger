package com.bottle.logger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bottle.log.Log;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnAddLog).setOnClickListener(this);
        checkRedStoragePermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddLog:
                Log.v(TAG, "这是一个v级日记");
                Log.d(TAG, "这是一个d级日记");
                Log.i(TAG, "这是一个i级日记");
                Log.w(TAG, "这是一个w级日记");
                Log.e(TAG, "这是一个e级日记");
                Log.d(TAG, "文件日志的目录：" + "/Android/data/" + getPackageName() + "/cache/log");
                break;
            default:
                break;
        }
    }

    private void checkRedStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x1001);
            }
        }
    }
}
