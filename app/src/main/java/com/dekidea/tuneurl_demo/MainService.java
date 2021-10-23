package com.dekidea.tuneurl_demo;

import static android.app.PendingIntent.*;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.dekidea.tuneurl.api.APIData;
import com.dekidea.tuneurl.util.Constants;
import com.dekidea.tuneurl.util.TimeUtils;
import com.dekidea.tuneurl.util.TuneURLManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class MainService extends Service implements Constants {

	private static final int NOTIFICATION_ID = 1500;

	private final int FINGERPRINT_MATCHING_THRESHOLD = 40;

	private Context mContext;

	private TuneURLReceiver mTuneURLReceiver;

	@Override
	public void onCreate() {

		super.onCreate();

		mContext = this;
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub

		return null;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId){

		super.onStartCommand(intent, flags, startId);

		runAsForeground();

		mTuneURLReceiver = new TuneURLReceiver();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SEARCH_FINGERPRINT_RESULT_RECEIVED);
		intentFilter.addAction(SEARCH_FINGERPRINT_RESULT_ERROR);
		intentFilter.addAction(ADD_RECORD_OF_INTEREST_RESULT_RECEIVED);
		intentFilter.addAction(ADD_RECORD_OF_INTEREST_RESULT_ERROR);
		intentFilter.addAction(POST_POLL_ANSWER_RESULT_RECEIVED);
		intentFilter.addAction(POST_POLL_ANSWER_RESULT_ERROR);

		registerReceiver(mTuneURLReceiver, intentFilter);

		return Service.START_STICKY;
	}


	@Override
	public void onDestroy(){

		super.onDestroy();

		unregisterReceiver(mTuneURLReceiver);
	}


	private void runAsForeground(){

		Intent i = new Intent(this, MainActivity.class);

		PendingIntent pendingIntent = getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

		// Create the Foreground Service
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
		Notification notification = notificationBuilder
				.setContentIntent(pendingIntent)
				.setOngoing(true)
				.setSmallIcon(R.drawable.ic_launcher_small)
				.setContentText(getString(R.string.main_service_label))
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setCategory(NotificationCompat.CATEGORY_SERVICE)
				.build();

		startForeground(NOTIFICATION_ID, notification);
	}


	@RequiresApi(Build.VERSION_CODES.O)
	private String createNotificationChannel(NotificationManager notificationManager){
		String channelId = "tune_url_demo_service";
		String channelName = "TuneURLDemoService";
		NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

		channel.setImportance(NotificationManager.IMPORTANCE_NONE);
		channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
		notificationManager.createNotificationChannel(channel);
		return channelId;
	}


	class TuneURLReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			try {

				if (intent != null) {

					String action = intent.getAction();

					if(action.equals(SEARCH_FINGERPRINT_RESULT_RECEIVED)){

						String result = intent.getStringExtra(TUNEURL_RESULT);

						try {

							JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();

							processResult(context, jsonObject);
						}
						catch (Exception e){

							e.printStackTrace();
						}
					}
					else if(action.equals(SEARCH_FINGERPRINT_RESULT_ERROR)){

						String error = intent.getStringExtra(TUNEURL_RESULT);

						//Do something

						TuneURLManager.startListening(mContext);
					}
					else if(action.equals(ADD_RECORD_OF_INTEREST_RESULT_RECEIVED)){

						String result = intent.getStringExtra(TUNEURL_RESULT);

						//Do something

						TuneURLManager.startListening(mContext);
					}
					else if(action.equals(ADD_RECORD_OF_INTEREST_RESULT_ERROR)){

						String error = intent.getStringExtra(TUNEURL_RESULT);

						//Do something

						TuneURLManager.startListening(mContext);
					}
					else if(action.equals(POST_POLL_ANSWER_RESULT_RECEIVED)){

						String result = intent.getStringExtra(TUNEURL_RESULT);

						//Do something

						TuneURLManager.startListening(mContext);
					}
					else if(action.equals(POST_POLL_ANSWER_RESULT_ERROR)){

						String error = intent.getStringExtra(TUNEURL_RESULT);

						//Do something

						TuneURLManager.startListening(mContext);
					}
				}
			}
			catch (Exception e){

				e.printStackTrace();
			}
		}
	}

	private void processResult(Context context, JsonObject jsonObject){

		try {

			JsonArray result_array = jsonObject.getAsJsonArray("result");

			if (result_array != null && result_array.size() > 0) {

				JsonObject closest_match = null;

				for (int i = 0; i < result_array.size(); i++) {

					try {

						JsonObject current_match = result_array.get(i).getAsJsonObject();

						if (closest_match == null) {

							closest_match = current_match;
						} else {

							int closest_matchPercentage = closest_match.get("matchPercentage").getAsInt();
							int current_matchPercentage = current_match.get("matchPercentage").getAsInt();

							if (closest_matchPercentage < current_matchPercentage) {

								closest_match = current_match;
							}
						}
					} catch (Exception e) {

						e.printStackTrace();
					}
				}

				if (closest_match != null) {

					int matchPercentage = closest_match.get("matchPercentage").getAsInt();

					if (matchPercentage >= FINGERPRINT_MATCHING_THRESHOLD) {

						long id = closest_match.get("id").getAsLong();
						String type = closest_match.get("type").getAsString();

						String name = "";
						try {

							name = closest_match.get("name").getAsString();
						} catch (Exception e) {

							e.printStackTrace();
						}

						String info = closest_match.get("info").getAsString();

						String description = "";
						try {

							description = closest_match.get("description").getAsString();
						} catch (Exception e) {

							e.printStackTrace();
						}

						APIData apiData = new APIData(id, name, description, type, info, matchPercentage);

						String date = TimeUtils.getCurrentTimeAsFormattedString();
						apiData.setDate(date);
						apiData.setDateAbsolute(TimeUtils.getCurrentTimeInMillis());

						startAlertActivity(context, apiData);
					}
					else {

						TuneURLManager.startListening(context);
					}
				}
				else {

					TuneURLManager.startListening(context);
				}
			}
			else {

				TuneURLManager.startListening(context);
			}
		}
		catch (Exception e){

			e.printStackTrace();

			TuneURLManager.startListening(context);
		}
	}


	private void startAlertActivity(Context context, APIData apiData){

		Intent intent = new Intent(context, AlertActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(APIDATA, apiData);

		context.startActivity(intent);
	}
}