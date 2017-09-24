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
        int y = (bitmap.getHeight() + bounds.height()) / 2;

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

        border.compress(Bitmap.CompressFormat.PNG,100,baos);

        border = border.copy(bitmapConfig, true);
        border.setHasAlpha(true);


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap change = Bitmap.createScaledBitmap(bitmap, width, height, false);
        Canvas canvas = new Canvas(change);
        Bitmap scaledBorder = Bitmap.createScaledBitmap(border,width,height, false);
        canvas.drawBitmap(scaledBorder, 0, 0,null);
        return change;
    }

    private static Bitmap makeTransparent(Bitmap image, int transparentColor) {
        Bitmap imageWithBG = Bitmap.createBitmap(image.getWidth(), image.getHeight(),image.getConfig());  // Create another image the same size
        imageWithBG.eraseColor(Color.WHITE);  // set its background to white, or whatever color you want
        Canvas canvas = new Canvas(imageWithBG);  // create a canvas to draw on the new image
        canvas.drawBitmap(image, 0f, 0f, null); // draw old image on the background
        image.recycle();
       return imageWithBG;
    }

}
