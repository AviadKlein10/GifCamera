package avivaviad.gifcamera.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import avivaviad.gifcamera.BitmapEditing;
import avivaviad.gifcamera.Constans;
import avivaviad.gifcamera.RealmHelper;
import avivaviad.gifcamera.RealmString;
import avivaviad.gifcamera.SharedPreferencesManager;
import avivaviad.gifcamera.Utils;
import avivaviad.gifcamera.custom.thread.GenerateGifFile;
import avivaviad.gifcamera.custom.thread.GifCreationCallback;
import avivaviad.gifcamera.model.GifObject;
import avivaviad.gifcamera.view.activity.GifCameraActivity;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by DELL on 17/07/2017.
 */

public class GifCameraPresenter extends Presenter<GifCameraActivity> implements GifCreationCallback {
    private final boolean cbAddImage;
    private int captureFrameRate = 700;
    private String textOnBitmap = "";
    private String fontResource = "a.ttf";
    private int fontSize = 26;
    private int framesCount = 7;
    private static final int COUNT_DOWN_TIME = 3;

    private ArrayList<Bitmap> previewBitmaps;
    //private ArrayList<Bitmap> gifBitmaps;
    private int previewQuality = 920, gifQuality = 600;
    private String gitTag;
    private int frameDuration;
    private boolean cbAddFrame;

    public ArrayList<Bitmap> getPreviewBitmaps(Context context) {
        addFrameToPreview(context);
        return previewBitmaps;
    }

    private void addFrameToPreview(Context context) {
        boolean cbAddText = false;
        if (!SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_TITLE).trim().isEmpty()) {
            cbAddText = true;
        }

        for (int i = 0; i < previewBitmaps.size(); i++) {
            if (cbAddFrame) {
                addFrameToBitmaps(context, i);
                if (cbAddImage) {
                    addSmallImageToBitmaps(context, i);
                }
            }
            if (cbAddText) {
                addTextToBitmaps(context, i);
            }

        }
        // Realm realm = Realm.getDefaultInstance();
        // RealmHelper.saveGifPreviewBitmaps(previewBitmaps,realm);
    }

    private void addTextToBitmaps(Context context, int i) {
        String[] arrFontType = {"varelaRound.ttf", "anka.ttf", "dorian.ttf", "makabi.ttf", "noot.otf"};
        int[] arrFontSize = {18, 22, 26, 30, 34, 38, 42};

        String title = SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_TITLE);
        String fontType = arrFontType[Integer.parseInt(SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_FONT_TYPE))];
        int fontSize = arrFontSize[Integer.parseInt(SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_FONT_SIZE))];
        String fontColor = SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_FONT_COLOR);//TODO fix load font color
        Bitmap bitmap = BitmapEditing.drawTextToBitmap(context, previewBitmaps.get(i), title, fontType, fontSize, Integer.parseInt(fontColor));
        previewBitmaps.set(i, bitmap);
        //gifBitmaps.set(i, Utils.getResizedBitmap(previewBitmaps.get(i), gifQuality));

    }

    private void addSmallImageToBitmaps(Context context, int i) {
        String srcImage = SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_IMAGE_SRC);
        Bitmap bitmap = BitmapEditing.addSmallImg(context, previewBitmaps.get(i), srcImage);
        previewBitmaps.set(i, bitmap);
        // gifBitmaps.set(i, Utils.getResizedBitmap(previewBitmaps.get(i), gifQuality));


    }


    private void addFrameToBitmaps(Context context, int i) {
        String srcFrame = SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_FRAME_SRC);
        Bitmap bitmap = BitmapEditing.addFrame(context, previewBitmaps.get(i), srcFrame);
        previewBitmaps.set(i, bitmap);
        //gifBitmaps.set(i, Utils.getResizedBitmap(previewBitmaps.get(i), gifQuality));
    }

    public GifCameraPresenter(Context context) {
        previewBitmaps = new ArrayList<>();
        if((SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_CHECK_ADD_FRAME)).contains("true")
                && !SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_FRAME_SRC).trim().isEmpty()) {
            cbAddFrame = true ;
        }else{
            cbAddFrame = false;
        }
        if(cbAddFrame && (SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_CHECK_ADD_IMAGE)).contains("true")
                && !SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_IMAGE_SRC).trim().isEmpty()) {
            cbAddImage = true ;
        }else{
            cbAddImage = false;
        }

        //  gifBitmaps = new ArrayList<>();

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

    public void takePicture(Bitmap bitmap, Context context) {
        //  int[] arrFontSize = {18, 22, 26, 30, 34, 38, 42};
        //  String[] arrFontType = {"varelaRound.ttf", "anka.ttf", "dorian.ttf", "makabi.ttf", "noot.otf"};
        // // previewBitmaps.add(Utils.getResizedBitmap(bitmap, CameraFrag.maxSize));
        //  // gifBitmaps.add(Utils.getResizedBitmap(bitmap, 600));
        //  String title = SharedPreferencesManager.loadValue(context,SharedPreferencesManager.KEY_TITLE);
        //  String fontType = arrFontType[Integer.parseInt(SharedPreferencesManager.loadValue(context,SharedPreferencesManager.KEY_FONT_TYPE))];
        //  int fontSize = arrFontSize[Integer.parseInt(SharedPreferencesManager.loadValue(context,SharedPreferencesManager.KEY_FONT_SIZE))];
        //  String fontColor = SharedPreferencesManager.loadValue(context,SharedPreferencesManager.KEY_FONT_COLOR);//TODO fix load font color
        //TODO האם יש טקסט בלי מסגרת
        //  bitmap = BitmapEditing.drawTextToBitmap(context,bitmap,title,fontType,fontSize, Color.parseColor(fontColor));
        // bitmap =  BitmapEditing.addFrame(context,bitmap, SharedPreferencesManager.loadValue(context,SharedPreferencesManager.KEY_FRAME_SRC));
        if(cbAddFrame){
            previewBitmaps.add(/*Utils.getResizedBitmap(bitmap, previewQuality)*/bitmap);
        }else{
            previewBitmaps.add(Utils.getResizedBitmap(bitmap, previewQuality,true));
            //   gifBitmaps.add(Utils.getResizedBitmap(bitmap, gifQuality));
        }
    }

    @Override
    public void onGifFileReady(final String uri) {
        mView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                mView.handleGifReady(uri);
                Log.d("myuri", uri + "");
            }
        });
        //  saveGifRealm(uri);
    }

    private void saveGifRealm(String uri) {
        if (!gitTag.isEmpty()) {
            //  convertBitmapsToPath(context);
            final GifObject gifObject = new GifObject();
            gifObject.setmGifTag(gitTag);
            gifObject.setmGifSrc(uri);
            Realm realm = Realm.getDefaultInstance();
            RealmHelper.saveGif(new GifObject(uri, gitTag), realm);
        }
    }

    private void convertBitmapsToPath(Context context, String uri) {


    }

    private void saveUsingRealm(RealmList<RealmString> bitmapsPath, String uri, Context context) {
        if (!gitTag.isEmpty()) {

            Long tsLong = System.currentTimeMillis() / 1000;
            String tsStr = tsLong.toString();
            final GifObject gifObject = new GifObject();
            gifObject.setmGifTag(gitTag);
            gifObject.setmGifSrc(uri);
            gifObject.setBitmapPaths(bitmapsPath);
            gifObject.setTimeStamp(tsStr);
            int frameDurationShared = Integer.parseInt(SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_DURATION_FOR_EACH_FRAME));
            gifObject.setFrameDuration(frameDurationShared);
            Realm realm = Realm.getDefaultInstance();
            RealmHelper.saveGif(gifObject, realm);
        }
    }

    private RealmString saveBitmapLocally(Bitmap bitmap, String ts, Context context) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, ts + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        final RealmString realmString = new RealmString();
        realmString.setImgPath(imageFile.getPath());
        return realmString;
    }


    public void setTag(String tagValue) {
        this.gitTag = tagValue;
    }


    public void setFrameCount(String newValue) {
        if (newValue.isEmpty()) {
            newValue = "7";
        }
        framesCount = Integer.parseInt(newValue);
    }

    public void setCaptureFrameRate(String newValue) {
        if (newValue.isEmpty()) {
            newValue = "700";
        }
        captureFrameRate = Integer.parseInt(newValue);
    }

    public void setQuality(String quality) {

        Log.d("malaX", previewQuality + "");
        switch (quality) {
            case "1":
                previewQuality = (int) previewQuality / 2;
                Log.d("mala", previewQuality + "");
                gifQuality = 300;
                break;
            case "2":
                previewQuality = previewQuality;
                Log.d("mala", previewQuality + "");
                gifQuality = 600;
                break;
            case "3":
                previewQuality = (int) (previewQuality * 1.5);
                Log.d("mala", previewQuality + "");
                gifQuality = 900;
                break;
        }

    }

    public void setPreviewQuality(int previewQuality) {
        Log.d("malaXbefore", previewQuality + "");
// not before
        this.previewQuality = previewQuality;
    }

    public void setTextParameters(String textOnBitmap, String fontResource, int fontSize) {
        this.textOnBitmap = textOnBitmap;
        this.fontResource = fontResource;
        this.fontSize = fontSize;
    }

    public void startCameraActivity(int fragCamera) {
        Intent intent = new Intent(mView.getApplicationContext(), GifCameraActivity.class);
        intent.putExtra("frag", Constans.FRAG_CAMERA);
        intent.putExtra("activity", Constans.ACTIVITY_CAMERA);
        mView.startActivity(intent);
        mView.finish();
    }

    public void saveBitmapsLocally(final Context context, final String uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Long tsLong;
                String ts;
                RealmList<RealmString> pathList = new RealmList<>();
                if (!SharedPreferencesManager.loadValue(context, SharedPreferencesManager.KEY_DB_TAG).isEmpty()) {

                    for (int i = 0; i < previewBitmaps.size(); i++) {
                        tsLong = (System.currentTimeMillis() / 1000);
                        ts = tsLong.toString();
                        pathList.add(saveBitmapLocally(previewBitmaps.get(i), i+ts, context));
                        Log.d("pathpath", pathList.get(i).getImgPath());
                        if (i == previewBitmaps.size() - 1) {
                            saveUsingRealm(pathList, uri, context);
                        }

                        try {
                            Thread.sleep(70);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }



            }
        }).start();
    }

    public ArrayList<Bitmap> convertPathsToBitmaps(GifObject gifObject) {
        RealmList<RealmString> realmList = gifObject.getBitmapPaths();
        ArrayList<Bitmap> arrBitmaps = new ArrayList<>();
        for (int i = 0; i < realmList.size(); i++) {
            arrBitmaps.add(i, decodeFile(realmList.get(i).getImgPath()));
        }
        return arrBitmaps;
    }

    private Bitmap decodeFile(String photoPath) {
        //TODO maybe not converting good
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(photoPath, options);
    }

    public void setFrameDuration(int frameDuration) {
        this.frameDuration = frameDuration;
    }

    public interface GifCameraPresenterCallback extends BaseView {
        void takeFrontCameraPicture(boolean lastOne);
    }


    public void generateGifFromBitmaps() {
        //   gifBitmaps.addAll(previewBitmaps) ; //resizeImagesToGifSize(previewBitmaps);
        resizeImagesToGifSize(previewBitmaps);
        //  new GenerateGifFile(resizedBitmaps, GifCameraActivity.DURATION_FOR_EACH_FRAME, this).start();
    }

    private void resizeImagesToGifSize(ArrayList<Bitmap> images) {
        ArrayList<Bitmap> gifsBitmaps = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            gifsBitmaps.add(Utils.getResizedBitmap(images.get(i), gifQuality,false));
        }
        new GenerateGifFile(gifsBitmaps, GifCameraActivity.DURATION_FOR_EACH_FRAME, this).start();
    }


    private class CaptureBitmapsThread extends Thread {

        @Override
        public void run() {

            boolean lastFrame = false;
            for (int i = 1; i <= framesCount; i++) {

                if (i == framesCount) {
                    lastFrame = true;
                }
                mView.takeFrontCameraPicture(lastFrame);
                mView.changeFrameCountNum(String.valueOf(i));

                try {
                    Thread.sleep(captureFrameRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

         /*   mView.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    showPreview();
                    generateGifFromBitmaps();
                }
            });*/
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








