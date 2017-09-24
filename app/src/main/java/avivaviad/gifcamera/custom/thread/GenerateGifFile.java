package avivaviad.gifcamera.custom.thread;

import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import avivaviad.gifcamera.Utils;
import avivaviad.gifcamera.presenter.GifCameraPresenter;

/**
 * Created by DELL on 17/07/2017.
 */

public class GenerateGifFile extends Thread {


    private final int frameRate;
    private ArrayList<Bitmap> bitmaps;
    GifCreationCallback gifCreationCallback;


    public GenerateGifFile(ArrayList<Bitmap> bitmaps, int frameRate, GifCreationCallback gifCreationCallback) {
        this.bitmaps = bitmaps;
        this.frameRate = frameRate;
        this.gifCreationCallback = gifCreationCallback;
    }

    @Override
    public void run() {
        try {
            final File file = Utils.getFileForGif();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(Utils.generateGIF(bitmaps, frameRate));
            bos.flush();
            bos.close();
            gifCreationCallback.onGifFileReady(file.toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
