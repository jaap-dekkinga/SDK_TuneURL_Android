package com.dekidea.tuneurl.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;

import com.dekidea.tuneurl.api.APIData;
import com.dekidea.tuneurl.service.APIService;
import com.dekidea.tuneurl.service.SoundListenerService;

public class TuneURLManager implements Constants{

    private static APIData currentAPIData;

    public static void setCurrentAPIData(APIData apiData){

        currentAPIData = apiData;
    }


    public static APIData getCurrentAPIData(){

        return currentAPIData;
    }


    public static boolean isWiredHeadsetOn(Context context){

        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        return  audioManager.isWiredHeadsetOn();
    }


    public static void startListeningService(Context context) {

        TuneURLManager.updateIntSetting(context, SETTING_LISTENING_STATE, SETTING_LISTENING_STATE_STARTED);

        try {

            Intent i = new Intent(context, SoundListenerService.class);

            if (Build.VERSION.SDK_INT > 25) {

                context.startForegroundService(i);
            }
            else{

                context.startService(i);
            }
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }


    public static void stopListeningService(Context context) {

        TuneURLManager.updateIntSetting(context, SETTING_LISTENING_STATE, SETTING_LISTENING_STATE_STOPPED);

        try {

            Intent i = new Intent(context, SoundListenerService.class);

            context.stopService(i);
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    public static void startListening(Context context) {

        try {

            Intent i = new Intent();
            i.setAction(LISTENING_ACTION);
            i.putExtra(TUNEURL_ACTION, ACTION_START_LISTENING);

            context.sendBroadcast(i);
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    public static void stopListening(Context context) {

        try {

            Intent i = new Intent();
            i.setAction(LISTENING_ACTION);
            i.putExtra(TUNEURL_ACTION, ACTION_STOP_LISTENING);

            context.sendBroadcast(i);
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    public static void stopRecorder(Context context) {

        try {

            Intent i = new Intent();
            i.setAction(LISTENING_ACTION);
            i.putExtra(TUNEURL_ACTION, ACTION_STOP_RECORDER);

            context.sendBroadcast(i);
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    public static void addRecordOfInterest(Context context, String TuneURL_ID, String interest_action, String date) {

        try {

            Intent i = new Intent(context, APIService.class);

            i.putExtra(TUNEURL_ACTION, ACTION_ADD_RECORD_OF_INTEREST);

            i.putExtra(ID, TuneURL_ID);
            i.putExtra(INTEREST_ACTION, interest_action);
            i.putExtra(DATE, date);

            context.startService(i);
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    public static void postPollAnswer(Context context, String poll_name, String user_response, String timestamp) {

        try {

            Intent i = new Intent(context, APIService.class);

            i.putExtra(TUNEURL_ACTION, ACTION_POST_POLL_ANSWER);

            i.putExtra(POLL_NAME, poll_name);
            i.putExtra(USER_RESPONSE, user_response);
            i.putExtra(TIMESTAMP, timestamp);

            context.startService(i);
        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    public static int fetchIntSetting(Context context, String setting, int default_value){

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        return (sp.getInt(setting, default_value));
    }


    public static void updateIntSetting(Context context, String setting, int value){

        try {

            SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putInt(setting, value);

            editor.commit();
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }

    public static String fetchStringSetting(Context context, String setting, String default_value){

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        return (sp.getString(setting, default_value));
    }

    public static void updateStringSetting(Context context, String setting, String value){

        try {

            SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString(setting, value);

            editor.commit();
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }


    private static boolean isHeadsetConnected(Context context){

        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.isWiredHeadsetOn()){

            return true;
        }

        return false;
    }
}
