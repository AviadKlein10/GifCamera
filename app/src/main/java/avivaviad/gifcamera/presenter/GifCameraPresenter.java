package avivaviad.gifcamera.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;

import avivaviad.gifcamera.BitmapEditing;
import avivaviad.gifcamera.SharedPreferencesManager;
import avivaviad.gifcamera.Utils;
import avivaviad.gifcamera.custom.thread.GenerateGifFile;
import avivaviad.gifcamera.custom.thread.GifCreationCallback;
import avivaviad.gifcamera.view.activity.GifCameraActivity;
import avivaviad.gifcamera.view.fragments.CameraFrag;

/**
 * Created by DELL on 17/07/2017.
 */

public class GifCameraPresenter extends Presenter<GifCameraActivity> implements GifCreationCallback {
    private int captureFrameRate = 700;
    private String textOnBitmap = "";
    private String fontResource = "a.ttf";
    private int fontSize = 26;
    private int framesCount = 7;
    private static final int COUNT_DOWN_TIME = 3;

    private ArrayList<Bitmap> previewBitmaps;
    private ArrayList<Bitmap> gifBitmaps;
    private int previewQuality = 920, gifQuality = 600;

    public ArrayList<Bitmap> getPreviewBitmaps(Context context) {
        addFrameToPreview(context);
        return previewBitmaps;
    }

    private void addFrameToPreview(Context context) {
        String srcFrame =SharedPreferencesManager.loadValue(context,SharedPreferencesManager.KEY_FRAME_SRC);
        for (int i = 0; i < previewBitmaps.size(); i++) {
            previewBitmaps.set(i,BitmapEditing.addFrame(context,previewBitmaps.get(i), srcFrame));
        }
    }

    public GifCameraPresenter() {
        previewBitmaps = new ArrayList<>();
        gifBitmaps = new ArrayList<>();
    }

    public void onStartClicked() {
        new CountDownThread().start();

    }


    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public void takePicture(Bitmap bitmap,Context context) {

       // previewBitmaps.add(Utils.getResizedBitmap(bitmap, CameraFrag.maxSize));
        // gifBitmaps.add(Utils.getResizedBitmap(bitmap, 600));
        bitmap = BitmapEditing.drawTextToBitmap(context,bitmap,"shalom olam","a.ttf",20, Color.GREEN);
    //  bitmap =  BitmapEditing.addFrame(context,bitmap, R.drawable.green_frame);
        previewBitmaps.add(Utils.getResizedBitmap(bitmap, previewQuality));
        Log.d("aniPres",CameraFrag.maxSize+"");
        gifBitmaps.add(Utils.getResizedBitmap(bitmap, gifQuality));
    }

    @Override
    public void onGifFileReady(final String uri) {
        mView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                mView.handleGifReady(uri);
            }
        });
    }



    public void setFrameCount(String newValue) {
        if(newValue.isEmpty()){
            newValue="7";
        }
        framesCount = Integer.parseInt(newValue);
    }

    public void setCaptureFrameRate(String newValue) {
        if(newValue.isEmpty()){
            newValue="700";
        }
        captureFrameRate = Integer.parseInt(newValue);
    }

    public void setQuality(String quality) {

        Log.d("malaX",previewQuality+"");
        switch (quality) {
            case "1":
                previewQuality = (int)previewQuality/2;
                Log.d("mala",previewQuality+"");
                gifQuality = 300;
                break;
            case "2":
                previewQuality = previewQuality;
                Log.d("mala",previewQuality+"");
                gifQuality = 600;
                break;
            case "3":
                previewQuality = (int) (previewQuality *1.5);
                Log.d("mala",previewQuality+"");
                gifQuality = 900;
                break;
        }

    }

    public void setPreviewQuality(int previewQuality) {
        Log.d("malaXbefore",previewQuality+"");
// not before
        this.previewQuality = previewQuality;
    }

    public void setTextParameters(String textOnBitmap,String fontResource,int fontSize) {
        this.textOnBitmap = textOnBitmap;
        this.fontResource = fontResource;
        this.fontSize = fontSize;
    }

    public interface GifCameraPresenterCallback extends BaseView {
        void takeFrontCameraPicture();
    }

    private void generateGifFromBitmaps() {
        new GenerateGifFile(gifBitmaps, GifCameraActivity.DURATION_FOR_EACH_FRAME, this).start();
    }


    private class CaptureBitmapsThread extends Thread {

        @Override
        public void run() {

            for (int i = 1; i <= framesCount; i++) {
                mView.takeFrontCameraPicture();
                mView.changeFrameCountNum(String.valueOf(i));

                try {
                    Thread.sleep(captureFrameRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mView.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    showPreview();
                    generateGifFromBitmaps();
                }
            });


        }
    }

    private void showPreview() {
        mView.showPreviewLayout();
    }


    class CountDownThread extends Thread {

        @Override
        public void run() {
            for (int i = COUNT_DOWN_TIME; i >= 0; i--) {
                if (i != 0) {
                    mView.changeCounterNum(String.valueOf(i));
                } else {
                    mView.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mView.changeCounterNum("Shot!");
                        }
                    });
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 0) {
                    startCaptureBitmaps();
                }
            }
        }
    }

    private void startCaptureBitmaps() {
        new CaptureBitmapsThread().start();
    }
}








