package avivaviad.gifcamera;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import avivaviad.gifcamera.custom.AnimatedGifEncoder;

/**
 * Created by DELL on 17/07/2017.
 */

public class Utils {

    private static final String IMAGE_DIRECTORY_NAME = "video_gif";

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {

            width = maxSize;
            height = (int) (width / bitmapRatio);

        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);

        }

        return Bitmap.createScaledBitmap(image, height, width, true);
    }


    public static File getFileForGif() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        mediaStorageDir.mkdirs();

        return new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".gif");
    }


    public static byte[] generateGIF(ArrayList<Bitmap> bitmaps, int framesDelay) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();

        //encoder.setQuality(1);
        /*Sets the delay time between each frame, or changes it for subsequent
          frames (applies to last frame added).*/
        encoder.setDelay(framesDelay);

        /*Sets the number of times the set of GIF frames should be played. Default
         *is 1; 0 means play indefinitely. Must be invoked before the first image
         *is added.*/

        encoder.setRepeat(0);

        /*Initiates GIF file creation on the given stream. The stream is not closed
         *automatically.*/
        encoder.start(bos);
        for (int i = 1; i < bitmaps.size(); i++) {
            //collecting frames at a given time

            //Bitmap bmp = mediaMetadataRetriever.getFrameAtTime(i*1000000);

            /**
             * Adds next GIF frame. the size of the first image is used
             * for all subsequent frames.
             */
            encoder.addFrame(bitmaps.get(i));
        }

        encoder.finish();
        return bos.toByteArray();

    }


    public static String reforrmatColorToNum(String loadedValue) {
        return loadedValue.replace("#", "");
    }
}



