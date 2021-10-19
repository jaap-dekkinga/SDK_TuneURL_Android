package com.dekidea.tuneurl_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dekidea.tuneurl.util.Constants;
import com.dekidea.tuneurl.util.TuneURLManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements Constants {

    private final String DEFAULT_TUNEURL_API_BASE_URL = "http://ec2-54-213-252-225.us-west-2.compute.amazonaws.com";
    private final String DEFAULT_SEARCH_FINGERPRINT_URL = "https://pnz3vadc52.execute-api.us-east-2.amazonaws.com/dev/search-fingerprint";
    private final String DEFAULT_POLL_API_URL = "http://pollapiwebservice.us-east-2.elasticbeanstalk.com/api/pollapi";
    private final String DEFAULT_INTERESTS_API_URL = "https://65neejq3c9.execute-api.us-east-2.amazonaws.com/interests";

    private static final int PERMISSIONS_REQUEST = 1337;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST = 1338;
    private static final int IGNORE_BATTERY_OPTIMIZATIONS_REQUEST = 1339;

    private Context mContext;

    private Button mStartStopButton;

    private boolean mPermissionsChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = this;

        initializeResources();

        setContentView(R.layout.activity_main);

        mStartStopButton = (Button) findViewById(R.id.start);
        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int state = TuneURLManager.fetchIntSetting(mContext, SETTING_LISTENING_STATE, SETTING_LISTENING_STATE_STOPPED);

                if(state == SETTING_LISTENING_STATE_STOPPED){

                    TuneURLManager.startListeningService(mContext);

                    startMainService();
                }
                else{

                    TuneURLManager.stopListeningService(mContext);

                    stopMainService();
                }

                setStartStopButtonState();
            }
        });
    }


    private void initializeResources(){

        String reference_file_path = TuneURLManager.fetchStringSetting(this, SETTING_TRIGGER_FILE_PATH, null);

        if(reference_file_path == null || reference_file_path.isEmpty()) {

            TuneURLManager.updateStringSetting(this, SETTING_TUNEURL_API_BASE_URL, DEFAULT_TUNEURL_API_BASE_URL);
            TuneURLManager.updateStringSetting(this, SETTING_SEARCH_FINGERPRINT_URL, DEFAULT_SEARCH_FINGERPRINT_URL);
            TuneURLManager.updateStringSetting(this, SETTING_POLL_API_URL, DEFAULT_POLL_API_URL);
            TuneURLManager.updateStringSetting(this, SETTING_INTERESTS_API_URL, DEFAULT_INTERESTS_API_URL);

            reference_file_path = installReferenceWavFile(this, R.raw.trigger_audio, "trigger_audio.raw");

            TuneURLManager.updateStringSetting(this, SETTING_TRIGGER_FILE_PATH, reference_file_path);
        }
    }


    private void setStartStopButtonState(){

        int state = TuneURLManager.fetchIntSetting(mContext, SETTING_LISTENING_STATE, SETTING_LISTENING_STATE_STOPPED);

        if(state == SETTING_LISTENING_STATE_STOPPED){

            mStartStopButton.setText(R.string.start);
        }
        else{

            mStartStopButton.setText(R.string.stop);
        }
    }


    @Override
    public void onResume() {

        super.onResume();

        if(!mPermissionsChecked){

            checkPermissions();
        }

        setStartStopButtonState();
    }


    @Override
    public void onPause() {

        super.onPause();
    }


    private void startMainService() {

        Intent intent = new Intent(mContext, MainService.class);

        startForegroundService(intent);
    }


    private void stopMainService() {

        Intent intent = new Intent(mContext, MainService.class);

        stopService(intent);
    }


    private String installReferenceWavFile(Context context, int raw_resource, String file_name){

        String output_file_path = null;

        InputStream input_stream = null;

        try {

            input_stream = context.getApplicationContext().getResources().openRawResource(raw_resource);

            File output_folder = context.getFilesDir();

            String file_path = output_folder.getAbsolutePath() + "/" + file_name;

            boolean success =  writeFile(input_stream, file_path);

            if(success){

                output_file_path = file_path;
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        finally {

            try {

                input_stream.close();
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }

        return output_file_path;
    }


    private boolean writeFile(InputStream input_stream, String output_file_path){

        try {

            File out_file = new File(output_file_path);
            if (!out_file.exists()) {

                OutputStream output_stream = new FileOutputStream(output_file_path);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = input_stream.read(buffer)) != -1) {

                    output_stream.write(buffer, 0, length);
                }

                output_stream.flush();
                output_stream.close();

                return true;
            }
        }
        catch(Exception e){

            e.printStackTrace();
        }

        return false;
    }


    @SuppressLint("NewApi")
    private void checkPermissions() {

        mPermissionsChecked = true;

        if (Permissions.hasAllPermissions(this)) {

            resume();
        }
        else {

            alertNeedsPermissions();
        }
    }


    private void resume() {

        if (Permissions.needsIgnoreBatteryOptimizations(this)) {

            Permissions.requestIgnoreBatteryOptimizations(this);
        }
        else if (!android.provider.Settings.canDrawOverlays(this)) {

            alertNeedsOverlayPermission();
        }
    }


    @SuppressLint("NewApi")
    private void requestPermissions() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            String[] needed_permissions = Permissions.getNeededPermissions(this);

            if (needed_permissions != null) {

                requestPermissions(needed_permissions, PERMISSIONS_REQUEST);
            }
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSIONS_REQUEST:

                if (Permissions.hasAllPermissions(this)) {

                    resume();
                }
                else {

                    alertPermissionsDenied();
                }

                break;
        }
    }


    public void alertNeedsPermissions() {

        requestPermissions();
    }


    private AlertDialog mOverlayPermissionAlert;

    private void alertNeedsOverlayPermission() {

        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));

        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST);
    }


    private AlertDialog mPermissionsDeniedAlert;

    private void alertPermissionsDenied() {


    }
}