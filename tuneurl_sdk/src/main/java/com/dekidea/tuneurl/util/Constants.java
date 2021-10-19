package com.dekidea.tuneurl.util;

public interface Constants {

    public static final String SETTING_TRIGGER_FILE_PATH = "com.dekidea.tuneurl.SETTING_TRIGGER_FILE_PATH";
    public static final String SETTING_SOUND_THRESHOLD = "com.dekidea.tuneurl.SETTING_SOUND_THRESHOLD";
    public static final String SETTING_RUNNING_STATE= "com.dekidea.tuneurl.SETTING_RUNNING_STATE";
    public static final int SETTING_RUNNING_STATE_STOPPED = 0;
    public static final int SETTING_RUNNING_STATE_STARTED = 1;
    public static final String SETTING_LISTENING_STATE= "com.dekidea.tuneurl.SETTING_LISTENING_STATE";
    public static final int SETTING_LISTENING_STATE_STOPPED = 0;
    public static final int SETTING_LISTENING_STATE_STARTED = 1;


    public static final String SHARED_PREFERENCES = "com.dekidea.tuneurl.SHARED_PREFERENCES";

    public static final String SETTING_TUNEURL_API_BASE_URL = "com.dekidea.tuneurl.SETTING_TUNEURL_API_BASE_URL";
    public static final String SETTING_SEARCH_FINGERPRINT_URL = "com.dekidea.tuneurl.SETTING_SEARCH_FINGERPRINT_URL";
    public static final String SETTING_POLL_API_URL = "com.dekidea.tuneurl.SETTING_POLL_API_URL";
    public static final String SETTING_INTERESTS_API_URL = "com.dekidea.tuneurl.SETTING_INTERESTS_API_URL";

    public static final String DEFAULT_TUNEURL_API_BASE_URL = "http://ec2-54-213-252-225.us-west-2.compute.amazonaws.com";
    public static final String DEFAULT_SEARCH_FINGERPRINT_URL = "https://pnz3vadc52.execute-api.us-east-2.amazonaws.com/dev/search-fingerprint";
    public static final String DEFAULT_POLL_API_URL = "http://pollapiwebservice.us-east-2.elasticbeanstalk.com/api/pollapi";
    public static final String DEFAULT_INTERESTS_API_URL = "https://65neejq3c9.execute-api.us-east-2.amazonaws.com/interests";

    public static final String TUNEURL_ACTION = "com.dekidea.tuneurl.TUNEURL_ACTION";
    public static final String LISTENING_ACTION = "com.dekidea.tuneurl.LISTENING_ACTION";
    public static final String SEARCH_FINGERPRINT_RESULT_RECEIVED = "com.dekidea.tuneurl.SEARCH_FINGERPRINT_RESULT_RECEIVED";
    public static final String SEARCH_FINGERPRINT_RESULT_ERROR = "com.dekidea.tuneurl.SEARCH_FINGERPRINT_RESULT_ERROR";
    public static final String POST_POLL_ANSWER_RESULT_RECEIVED = "com.dekidea.tuneurl.POST_POLL_ANSWER_RESULT_RECEIVED";
    public static final String POST_POLL_ANSWER_RESULT_ERROR = "com.dekidea.tuneurl.POST_POLL_ANSWER_RESULT_ERROR";
    public static final String ADD_RECORD_OF_INTEREST_RESULT_RECEIVED = "com.dekidea.tuneurl.ADD_RECORD_OF_INTEREST_RESULT_RECEIVED";
    public static final String ADD_RECORD_OF_INTEREST_RESULT_ERROR = "com.dekidea.tuneurl.ADD_RECORD_OF_INTEREST_RESULT_ERROR";
    public static final String TUNEURL_RESULT = "com.dekidea.tuneurl.TUNEURL_RESULT";

    public static final int ACTION_STOP_LISTENING = 1;
    public static final int ACTION_START_LISTENING = 2;
    public static final int ACTION_STOP_RECORDER = 3;
    public static final int ACTION_SEARCH_FINGERPRINT = 4;
    public static final int ACTION_ADD_RECORD_OF_INTEREST = 5;
    public static final int ACTION_POST_POLL_ANSWER = 6;


    public static final String FINGERPRINT = "com.dekidea.tuneurl.FINGERPRINT";
    public static final String ID = "com.dekidea.tuneurl.ID";
    public static final String INTEREST_ACTION = "com.dekidea.tuneurl.INTEREST_ACTION";
    public static final String DATE = "com.dekidea.tuneurl.DATE";
    public static final String USER_RESPONSE = "com.dekidea.tuneurl.USER_RESPONSE";
    public static final String POLL_NAME = "com.dekidea.tuneurl.POLL_NAME";
    public static final String TIMESTAMP = "com.dekidea.tuneurl.TIMESTAMP";

    public static final String APIDATA = "com.dekidea.tuneurl.APIDATA";

    public static final String USER_RESPONSE_YES = "yes";
    public static final String USER_RESPONSE_NO = "no";

    public static final String ACTION_SAVE_PAGE = "save_page";
    public static final String ACTION_OPEN_PAGE = "open_page";
    public static final String ACTION_PHONE = "phone";
    public static final String ACTION_POLL = "poll";
    public static final String ACTION_COUPON = "coupon";
    public static final String ACTION_MAP = "map";


    public static final String INTEREST_ACTION_HEARD = "heard";
    public static final String INTEREST_ACTION_INTERESTED = "interested";
    public static final String INTEREST_ACTION_ACTED = "acted";
    public static final String INTEREST_ACTION_SHARED = "shared";


    public static final int TYPE_SAVED_PAGE = 1;
    public static final int TYPE_COUPON_NEW = 2;
    public static final int TYPE_COUPON_USED = 3;
}
