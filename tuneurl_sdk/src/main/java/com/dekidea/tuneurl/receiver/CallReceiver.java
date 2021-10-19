package com.dekidea.tuneurl.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.dekidea.tuneurl.util.Constants;
import com.dekidea.tuneurl.util.TuneURLManager;

public class CallReceiver extends BroadcastReceiver implements Constants {

    private Context mContext;
    private TelephonyManager mTelephonyManager;
    private MyCallListener mMyCallListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        mMyCallListener = new MyCallListener();

        mTelephonyManager.listen(mMyCallListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    class MyCallListener extends PhoneStateListener implements Constants{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {

                case TelephonyManager.CALL_STATE_OFFHOOK:

                    TuneURLManager.stopListening(mContext);

                    break;

                case TelephonyManager.CALL_STATE_RINGING:

                    break;

                case TelephonyManager.CALL_STATE_IDLE:

                    int running_state = TuneURLManager.fetchIntSetting(mContext, Constants.SETTING_RUNNING_STATE, Constants.SETTING_RUNNING_STATE_STOPPED);

                    if(running_state == SETTING_RUNNING_STATE_STARTED){

                        TuneURLManager.startListening(mContext);
                    }

                    break;

                default:

                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    }
}