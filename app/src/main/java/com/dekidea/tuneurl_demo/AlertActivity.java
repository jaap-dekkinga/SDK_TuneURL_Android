package com.dekidea.tuneurl_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.dekidea.tuneurl.api.APIData;
import com.dekidea.tuneurl.util.Constants;
import com.dekidea.tuneurl.util.TuneURLManager;

public class AlertActivity extends AppCompatActivity implements Constants {

    private APIData apiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alert);

        try{

            apiData = getIntent().getParcelableExtra(APIDATA);
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {

        super.onResume();

        showAlert(apiData);
    }


    public void showAlert(final APIData apiData) {

        try {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setTitle("Are you interested?");

            String message = "Type: " + apiData.getType() + "\n";
            message = message + "Description: " + apiData.getDescription() + "\n";
            message = message + "Name: " + apiData.getName() + "\n";
            message = message + "Info: " + apiData.getInfo() + "\n";

            alertBuilder.setMessage(message);

            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    doAction("yes", apiData);
                }
            });

            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    doAction("no", apiData);
                }
            });

            AlertDialog alertDialog = alertBuilder.create();

            alertDialog.setCancelable(false);

            alertDialog.show();
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    public void doAction(String user_response, APIData apiData){

        if(apiData != null) {

            String action = apiData.getType();
            String date = apiData.getDate();

            if (ACTION_POLL.equals(action)) {

                TuneURLManager.postPollAnswer(this, user_response, apiData.getDescription(), apiData.getDate());

                TuneURLManager.startListening(this);
            }
            else {

                if (USER_RESPONSE_YES.equals(user_response)) {

                    if (ACTION_SAVE_PAGE.equals(action)) {

                        saveInfo(apiData);

                        TuneURLManager.startListening(this);
                    }
                    else if (ACTION_OPEN_PAGE.equals(action)) {

                        TuneURLManager.addRecordOfInterest(this, String.valueOf(apiData.getId()), INTEREST_ACTION_ACTED, date);

                        openPage( apiData);

                        TuneURLManager.startListening(this);
                    }
                    else if (ACTION_PHONE.equals(action)) {

                        TuneURLManager.addRecordOfInterest(this, String.valueOf(apiData.getId()), INTEREST_ACTION_ACTED, date);

                        callPhone(apiData);
                    }
                    else {

                        TuneURLManager.startListening(this);
                    }
                }
                else {

                    TuneURLManager.startListening(this);
                }
            }
        }

        this.finish();
    }


    private void openPage(APIData data){

        try{

            String url = data.getInfo();

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }


    private void saveInfo(APIData data){

        openPage(data);
    }


    private void callPhone(APIData data){

        try {

            String phone_number = data.getInfo();

            if (phone_number != null) {

                try {

                    Intent i = new Intent(Intent.ACTION_CALL);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    String uri = "tel:" + phone_number.trim();

                    i.setData(Uri.parse(uri));

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        TuneURLManager.stopListening(this);

                        startActivity(i);
                    }
                    else{

                        TuneURLManager.startListening(this);
                    }
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }
}