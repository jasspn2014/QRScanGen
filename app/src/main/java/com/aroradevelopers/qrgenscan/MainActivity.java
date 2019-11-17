package com.aroradevelopers.qrgenscan;

import android.Manifest;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class MainActivity extends Activity {
    private TextView txt_result;
    private SurfaceView surfaceView;
    private QREader qrEader;
    Button editBtn;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setupCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this,"You Must Enable Camera Permission",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
                builder = new AlertDialog.Builder(MainActivity.this);
                editBtn = findViewById(R.id.editUrlBtn);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final EditText input = new EditText(MainActivity.this);
                        input.setText(txt_result.getText().toString());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                               );

                        input.setTextColor(Color.BLACK);
                        if(txt_result.getText().toString().matches("")) {
                            input.setText("Please Enter a URL");
                             input.setHintTextColor(Color.BLACK);}

                        input.setLayoutParams(lp);
                        builder.setView(input)

                                .setPositiveButton("Generate", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        try{
                                            Intent intent = new Intent(MainActivity.this,NewQRActivity.class);
                                            intent.putExtra(MyConstants.KEY_URL_STRING,input.getText().toString());
                                            startActivity(intent);
                                        }
                                        catch (Exception e)
                                        {
                                            android.widget.Toast.makeText(MainActivity.this, "URL String Can't Be Empty", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                        AlertDialog alert = builder.create();
                        alert.setTitle("Edit URL");
                        alert.setIcon(R.drawable.ic_edit_black_24dp);
                        alert.show();


                }});

    }

        private void setupCamera()
        {
            txt_result = findViewById(R.id.infoUrl);
            surfaceView = findViewById(R.id.camera_view);
            setupQREader();
        }

    private void setupQREader() {
        qrEader = new QREader.Builder(this,surfaceView,new QRDataListener(){
            @Override
            public void onDetected(final String data) {
                txt_result.post(new Runnable() {
                    @Override
                    public void run() {
                        txt_result.setText(data);
                    }
                });
            }
        }).facing(QREader.BACK_CAM)
          .enableAutofocus(true)
          .height(surfaceView.getHeight())
          .width(surfaceView.getWidth())
          .build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(qrEader != null)
                        {
                            qrEader.initAndStart(surfaceView);
                        }


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You Must Enable Camera Permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(qrEader!=null)
                        {
                            qrEader.releaseAndCleanup();
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

}
