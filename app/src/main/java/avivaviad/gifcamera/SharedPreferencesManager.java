package avivaviad.gifcamera;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Aviad on 14/08/2017.
 */

public class SharedPreferencesManager {

    public static final String KEY_CAPTURE_FRAME_RATE = "0";
    public static final String KEY_DURATION_FOR_EACH_FRAME ="1" ;
    public static final String KEY_FRAME_COUNT = "2";
    public static final String KEY_QUALITY = "3";
    public static final String KEY_TITLE = "4";
    public static final String KEY_FONT_SIZE = "5";
    public static final String KEY_FONT_TYPE = "6";
    public static final String KEY_FONT_COLOR = "7";
    public static final String KEY_CHECK_ADD_FRAME = "8";
    public static final String KEY_FRAME_SRC = "9";
    public static final String KEY_CHECK_ADD_IMAGE = "10";
    public static final String KEY_IMAGE_SRC = "11";
    public static final String KEY_DB_TAG = "12";
    public static final String KEY_FIRST_INITIALIZE = "13";


    public static void saveValue(Context context,String key,String value){
        SharedPreferences sharedPref = context.getSharedPreferences("AppPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String loadValue(Context context,String key){
        SharedPreferences sharedPref = context.getSharedPreferences("AppPreferences",Context.MODE_PRIVATE);
        return sharedPref.getString(key,"");
    }

    public static void setDefaultSettings(Context context){
        saveValue(context,KEY_CAPTURE_FRAME_RATE,"700");
        saveValue(context,KEY_DURATION_FOR_EACH_FRAME,"110");
        saveValue(context,KEY_FRAME_COUNT,"7");
        saveValue(context,KEY_QUALITY,"2");
        saveValue(context,KEY_TITLE,"");
        saveValue(context,KEY_FONT_SIZE,"2");
        saveValue(context,KEY_FONT_TYPE,"0");
        saveValue(context,KEY_FONT_COLOR,"#ffffff");
        saveValue(context,KEY_CHECK_ADD_FRAME,"0");
        saveValue(context,KEY_FRAME_SRC,"");
        saveValue(context,KEY_CHECK_ADD_IMAGE,"0");
        saveValue(context,KEY_IMAGE_SRC,"");
        saveValue(context,KEY_DB_TAG,"");
    }

    public static boolean isFirstInit(Context context){
        Log.d("firstfirst","enter");
        if(loadValue(context,KEY_FIRST_INITIALIZE).isEmpty()){
            Log.d("firstfirst","empty");
            saveValue(context,KEY_FIRST_INITIALIZE,"1");
            return true;
        }
        return false;
    }


}
