package avivaviad.gifcamera.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import avivaviad.gifcamera.R;
import avivaviad.gifcamera.SharedPreferencesManager;
import avivaviad.gifcamera.presenter.BaseView;
import avivaviad.gifcamera.presenter.OnSettingCallBack;
import avivaviad.gifcamera.presenter.OnStartCallBack;
import avivaviad.gifcamera.presenter.Presenter;
import avivaviad.gifcamera.presenter.StartPresenter;
import avivaviad.gifcamera.view.BaseActivity;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * Created by DELL on 20/07/2017.
 */

public class StartActivity extends BaseActivity implements BaseView, OnStartCallBack, OnSettingCallBack {


    private int REQUEST_CODE_ASK_PERMISSIONS;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initDefault();
        ImageView startImageBtn = (ImageView) findViewById(R.id.start_btn);
        Button btnSetting = (Button) findViewById(R.id.btn_settings);
        startImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartPresenter) mPresenter).onStartPressed();
            }
        });


        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartPresenter) mPresenter).onSettingsPressed();
            }
        });


    }



    private void initDefault() {
        if (SharedPreferencesManager.isFirstInit(this)) {
            Log.d("firstinit", "first");
            SharedPreferencesManager.setDefaultSettings(this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.MANAGE_DOCUMENTS") == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(StartActivity.this, new String[]{"android.permission.MANAGE_DOCUMENTS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
        if (permsRequestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ((StartPresenter) mPresenter).onStartPressed();
            } else {
                Toast.makeText(StartActivity.this,
                        "Permission denied, You need to give permission to use this feature",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected Presenter getPresenter() {
        return new StartPresenter();
    }

    @Override
    protected void bind() {
        mPresenter.bind(this);
    }

    @Override
    protected void unbind() {
        mPresenter.unbind();
    }


    @Override
    public void onError(String message) {

    }

    @Override
    public void onStartPressed() {

        //((StartPresenter)mPresenter).onStartPressed();
    }

    @Override
    public void onSettingsPressed() {

    }
}
