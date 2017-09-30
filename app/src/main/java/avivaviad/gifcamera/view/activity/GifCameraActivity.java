package avivaviad.gifcamera.view.activity;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;

import avivaviad.gifcamera.Constans;
import avivaviad.gifcamera.R;
import avivaviad.gifcamera.RealmHelper;
import avivaviad.gifcamera.SharedPreferencesManager;
import avivaviad.gifcamera.presenter.BaseView;
import avivaviad.gifcamera.presenter.GifCameraPresenter;
import avivaviad.gifcamera.presenter.Presenter;
import avivaviad.gifcamera.view.BaseActivity;
import avivaviad.gifcamera.view.fragments.CameraFrag;
import avivaviad.gifcamera.view.fragments.CameraFragCallBack;
import avivaviad.gifcamera.view.fragments.PreviewCallBack;
import avivaviad.gifcamera.view.fragments.PreviewFrag;
import io.realm.Realm;

public class GifCameraActivity extends BaseActivity implements BaseView, GifCameraPresenter.GifCameraPresenterCallback, CameraFragCallBack, PreviewCallBack {


    public static int DURATION_FOR_EACH_FRAME;

    private String gifUri;
    private CameraFrag cameraFrag;
    private Handler handler;
    private boolean inPreview;
    private PreviewFrag previewFrag;
    private int fromActivity;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle extras = getIntent().getExtras();
        handler = new Handler();
        if (extras != null) {
            if (extras.getInt("frag") == Constans.FRAG_PREVIEW) {
                if (extras.getString("time_stamp") != null && extras.getString("frame_duration") != null) {
                    showPreviewLayoutFromGallery(extras.getString("time_stamp"), extras.getInt("frame_duration"));
                } else {
                    showPreviewLayout();
                }
            } else {
                startCameraFrag();
            }
            fromActivity = extras.getInt("activity");
        }
        ((GifCameraPresenter)mPresenter).setFrameDuration(DURATION_FOR_EACH_FRAME);
    }


    private void startCameraFrag() {
        ((GifCameraPresenter) mPresenter).setFrameCount(SharedPreferencesManager.loadValue(getApplicationContext(), SharedPreferencesManager.KEY_FRAME_COUNT));
        ((GifCameraPresenter) mPresenter).setCaptureFrameRate(SharedPreferencesManager.loadValue(getApplicationContext(), SharedPreferencesManager.KEY_CAPTURE_FRAME_RATE));
        ((GifCameraPresenter) mPresenter).setTextParameters(SharedPreferencesManager.loadValue(getApplicationContext(), SharedPreferencesManager.KEY_TITLE), SharedPreferencesManager.loadValue(getApplicationContext(), SharedPreferencesManager.KEY_FONT_TYPE)
                , Integer.parseInt(SharedPreferencesManager.loadValue(getApplicationContext(), SharedPreferencesManager.KEY_FONT_SIZE)));
        ((GifCameraPresenter) mPresenter).setTag(SharedPreferencesManager.loadValue(getApplicationContext(), SharedPreferencesManager.KEY_DB_TAG));
        showCameraFragment();
    }

    @Override
    protected Presenter getPresenter() {
        return new GifCameraPresenter();
    }


    @Override
    protected void bind() {
        mPresenter.bind(this);
    }

    @Override
    protected void unbind() {
        mPresenter.unbind();
    }


    private void showCameraFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        cameraFrag = CameraFrag.newInstance(this);

        ft.replace(R.id.frame_counter, cameraFrag);
        ft.commit();
    }


    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onError(String message) {

    }

    public void changeCounterNum(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cameraFrag.showCountDownText(true);
                cameraFrag.changeCountDownText(text);


            }
        });
    }

    @Override
    public void takeFrontCameraPicture() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cameraFrag.showCountDownText(false);
                ((GifCameraPresenter) mPresenter).takePicture(cameraFrag.getBitmapFromTextureView(), getApplicationContext());
            }
        });
    }

    public void changeFrameCountNum(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cameraFrag.changeFrameCounter(text);
            }
        });
    }

    private void showPreviewLayoutFromGallery(final String time_stamp, int frame_duration) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DURATION_FOR_EACH_FRAME = frame_duration;
        previewFrag = PreviewFrag.newInstance(this, DURATION_FOR_EACH_FRAME);
        previewFrag.setFromActivity(fromActivity);
        ft.replace(R.id.frame_counter, previewFrag);
        ft.commit();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                ArrayList<Bitmap> arrBitmaps = ((GifCameraPresenter) mPresenter).convertPathsToBitmaps(RealmHelper.loadGif(time_stamp, realm));
                previewFrag.startImagesPreview(arrBitmaps);
            }
        }, 1000);
        inPreview = true;
    }

    public void showPreviewLayout() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        String lastDuration = SharedPreferencesManager.loadValue(this, SharedPreferencesManager.KEY_DURATION_FOR_EACH_FRAME);
        if (lastDuration.isEmpty()) {
            DURATION_FOR_EACH_FRAME = 110;//default duration
        } else {
            DURATION_FOR_EACH_FRAME = Integer.parseInt(lastDuration);
        }
        previewFrag = PreviewFrag.newInstance(this, DURATION_FOR_EACH_FRAME);
        previewFrag.setFromActivity(fromActivity);
        ft.replace(R.id.frame_counter, previewFrag);
        ft.commit();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                previewFrag.startImagesPreview(((GifCameraPresenter) mPresenter).getPreviewBitmaps(getApplicationContext()));
            }
        }, 1000);
        inPreview = true;
    }

    @Override
    public void onStartClick() {
        ((GifCameraPresenter) mPresenter).onStartClicked();
    }

    @Override
    public void setQuality(int maxSize) {
        Log.d("malamax", maxSize + "");
        ((GifCameraPresenter) mPresenter).setPreviewQuality(maxSize);
        String quality = SharedPreferencesManager.loadValue(this, SharedPreferencesManager.KEY_QUALITY);
        if (quality.isEmpty()) {
            quality = "2";
        }
        ((GifCameraPresenter) mPresenter).setQuality(quality);
    }


    @Override
    public void onSharePressed() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(gifUri));
        startActivity(Intent.createChooser(shareIntent, "Gif"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!inPreview) {
            showCameraFragment();
        }

    }

    @Override
    public void onBackPressed() {
        if (!inPreview) {
            startActivity(new Intent(this, StartActivity.class));
        } else {
            Log.d("backback", fromActivity + "");
            if (fromActivity == Constans.ACTIVITY_GALLERY) {
                onBackClicked(Constans.ACTIVITY_GALLERY);
            } else if (fromActivity == Constans.ACTIVITY_CAMERA) {
                onBackClicked(Constans.ACTIVITY_CAMERA);
            }
        }
    }

    @Override
    public void onBackClicked(int fromActivity) {
        if (fromActivity == Constans.ACTIVITY_CAMERA) {
            ((GifCameraPresenter) mPresenter).startCameraActivity(Constans.FRAG_CAMERA);
        } else {
            startActivity(new Intent(this, GifGalleryActivity.class));
        }
        finish();
    }


    public void handleGifReady(String uri) {
        gifUri = uri;
        previewFrag.makeGifSharable(uri);
        ((GifCameraPresenter) mPresenter).saveBitmapsLocally(this, uri);
    }
}
