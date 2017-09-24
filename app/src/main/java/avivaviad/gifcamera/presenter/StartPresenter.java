package avivaviad.gifcamera.presenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import avivaviad.gifcamera.view.activity.GifCameraActivity;
import avivaviad.gifcamera.view.activity.SettingsActivity;
import avivaviad.gifcamera.view.activity.StartActivity;

/**
 * Created by DELL on 20/07/2017.
 */

public class StartPresenter extends Presenter<StartActivity> implements OnStartCallBack {

    private static final int CAPTURE_VIDEO_ACTIVITY = 200;
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    public void onStartPressed() {
        if(checkPermission()){
                Intent intent = new Intent(mView.getApplicationContext(),GifCameraActivity.class);
                mView.startActivity(intent);
                mView.finish();
        }
    }

    @Override
    public void onSettingsPressed() {
        Intent intent = new Intent(mView.getApplicationContext(),SettingsActivity.class);
        mView.startActivity(intent);
        mView.finish();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public boolean checkPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(mView.getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(mView,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    CAPTURE_VIDEO_ACTIVITY);
            return false;
        }
        return true;
    }
}
