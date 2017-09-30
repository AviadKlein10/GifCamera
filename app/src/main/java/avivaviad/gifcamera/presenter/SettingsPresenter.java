package avivaviad.gifcamera.presenter;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import avivaviad.gifcamera.SharedPreferencesManager;
import avivaviad.gifcamera.view.activity.GifGalleryActivity;
import avivaviad.gifcamera.view.activity.SettingsActivity;

/**
 * Created by Aviad on 14/08/2017.
 */
public class SettingsPresenter extends Presenter<SettingsActivity> implements SettingCallBack {
    @Override
    public void onResume() {
        
    }

    @Override
    public void onPause() {

    }

    public HashMap<String,String> getLastValues(Context context) {
        HashMap<String,String> hmLastValues = new HashMap<>();
        hmLastValues.put(SharedPreferencesManager.KEY_CAPTURE_FRAME_RATE,getValueOf(context,SharedPreferencesManager.KEY_CAPTURE_FRAME_RATE));
        hmLastValues.put(SharedPreferencesManager.KEY_DURATION_FOR_EACH_FRAME,getValueOf(context,SharedPreferencesManager.KEY_DURATION_FOR_EACH_FRAME));
        hmLastValues.put(SharedPreferencesManager.KEY_FRAME_COUNT,getValueOf(context,SharedPreferencesManager.KEY_FRAME_COUNT));
        hmLastValues.put(SharedPreferencesManager.KEY_QUALITY,getValueOf(context,SharedPreferencesManager.KEY_QUALITY));
        hmLastValues.put(SharedPreferencesManager.KEY_TITLE,getValueOf(context,SharedPreferencesManager.KEY_TITLE));
        hmLastValues.put(SharedPreferencesManager.KEY_FONT_SIZE,getValueOf(context,SharedPreferencesManager.KEY_FONT_SIZE));
        hmLastValues.put(SharedPreferencesManager.KEY_FONT_TYPE,getValueOf(context,SharedPreferencesManager.KEY_FONT_TYPE));
        hmLastValues.put(SharedPreferencesManager.KEY_FONT_COLOR,getValueOf(context,SharedPreferencesManager.KEY_FONT_COLOR));
        hmLastValues.put(SharedPreferencesManager.KEY_CHECK_ADD_FRAME,getValueOf(context,SharedPreferencesManager.KEY_CHECK_ADD_FRAME));
        hmLastValues.put(SharedPreferencesManager.KEY_FRAME_SRC,getValueOf(context,SharedPreferencesManager.KEY_FRAME_SRC));
        hmLastValues.put(SharedPreferencesManager.KEY_CHECK_ADD_IMAGE,getValueOf(context,SharedPreferencesManager.KEY_CHECK_ADD_IMAGE));
        hmLastValues.put(SharedPreferencesManager.KEY_IMAGE_SRC,getValueOf(context,SharedPreferencesManager.KEY_IMAGE_SRC));
        hmLastValues.put(SharedPreferencesManager.KEY_DB_TAG,getValueOf(context,SharedPreferencesManager.KEY_DB_TAG));
        return hmLastValues;
    }

    private String getValueOf(Context context,String key) {
       return SharedPreferencesManager.loadValue(context, key);
    }

    @Override
    public void onGifGalleryPressed() {
        Intent intent = new Intent(mView.getApplicationContext(),GifGalleryActivity.class);
        mView.startActivity(intent);
        mView.finish();
    }

    public void chooseColor(Context applicationContext) {
    }


    public interface SettingsPresenterCallBack extends BaseView {
    }



   /* @Override
    public void onSettingsPressed() {
        Intent intent = new Intent(mView.getApplicationContext(),SettingsActivity.class);
        mView.startActivity(intent);
        mView.finish();
    }*/


}
