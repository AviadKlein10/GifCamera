package avivaviad.gifcamera;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Aviad on 21/08/2017.
 */

public class BitmapEditing {


    public static Bitmap drawTextToBitmap(Context context,
                                          Bitmap bitmap,
                                          String gText, String fontResource, int fontSize, int fontColor) {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(fontColor);
        paint.setTextSize((int) (fontSize * scale));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Typeface plain = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontResource);
        paint.setTypeface(plain);
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() - bounds.height()) ;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    public static Bitmap addFrame(Context context,Bitmap bitmap,String srcFrame){

        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

      //  Bitmap border = BitmapFactory.decodeResource(context.getResources(), rscFrame);
       // Bitmap border = BitmapFactory.decodeStream(context.getResources(), rscFrame);
        Bitmap border = null;
        InputStream image_stream = null;
        Uri uri = Uri.parse(srcFrame);
        try {
            image_stream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
       options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        border= BitmapFactory.decodeStream(image_stream,null,options ); // TODO option to insert png
       /* FileOutputStream out = null;
            try {
             out = new FileOutputStream(srcFrame);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        border.setHasAlpha(true);
        border.compress(Bitmap.CompressFormat.PNG,100,baos);

        border = border.copy(bitmapConfig, true);


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //Bitmap change = Bitmap.createScaledBitmap(bitmap, width+100, height+100, false);
        Bitmap change = Bitmap.createScaledBitmap(bitmap, width, height, false);
        Canvas canvas = new Canvas(change);
        Bitmap scaledBorder = Bitmap.createScaledBitmap(border,width,height, false);
        //canvas.drawBitmap(scaledBorder, 50, 50 ,null);
        canvas.drawBitmap(scaledBorder, 0, 0 ,null);
        return makeTransparent(change,0);
    }

    public static Bitmap addSmallImg(Context context,Bitmap bitmap,String srcFrame){

        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Bitmap smallImg = null;
        InputStream image_stream = null;
        Uri uri = Uri.parse(srcFrame);
        try {
            image_stream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        smallImg= BitmapFactory.decodeStream(image_stream,null,options ); // TODO option to insert png
       /* FileOutputStream out = null;
            try {
             out = new FileOutputStream(srcFrame);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
     //  ByteArrayOutputStream baos = new ByteArrayOutputStream();

     //  smallImg.compress(Bitmap.CompressFormat.PNG,100,baos);

     //  smallImg = smallImg.copy(bitmapConfig, true);



        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        Matrix matrix = new Matrix();
        matrix.postScale(0,height- smallImg.getHeight()/4);

        Bitmap change = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Canvas canvas = new Canvas(change);
        Bitmap scaledBorder = Bitmap.createScaledBitmap(smallImg, (int) (smallImg.getWidth()/1.5), (int) (smallImg.getHeight()/1.5), true);
        canvas.drawBitmap(scaledBorder, 0, (float) (height-(smallImg.getHeight()/1.5)),paint);
        return change;
    }

    private static Bitmap makeTransparent(Bitmap src, int transparentColor) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int pixel;

        // iteration through pixels
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                int index = y * width + x;
                pixel = pixels[index];
                if(pixel == Color.WHITE){
                    //change A-RGB individually
                  /*  A = Color.alpha(pixel);
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    pixels[index] = Color.argb(A,R,G,B); */

                    pixels[index] = Color.TRANSPARENT;
                }
            }
        }
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

}
