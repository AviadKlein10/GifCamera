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
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

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
        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);
        Typeface plain = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontResource);
        paint.setTypeface(plain);

        ArrayList<String> lines = sperateLines(gText);

        int lineDowner = 0;

        for (int i = 0; i < lines.size(); i++) {

            Rect bounds = new Rect();
            paint.getTextBounds(lines.get(i), 0, lines.get(i).length(), bounds);

            int originalImageWidth = bitmap.getWidth();
            int originalImageHeight = bitmap.getHeight();

          //  int textX = ((originalImageWidth / 100) * 100) - bounds.width();
            int textY = lineDowner + ((originalImageHeight / 100) * 94);
            Log.d("wiwiwi",canvas.getWidth()+"");
            float textWidth = paint.measureText(lines.get(i));
            int  centerWidth= (int) (textWidth/2);
            canvas.drawText(lines.get(i), originalImageWidth/2 - centerWidth, textY, paint);
            Log.d("wiwiwi",canvas.getWidth()+"");
            lineDowner = lineDowner + 100;
        }

        return bitmap;
    }

    private static ArrayList<String> sperateLines(String gText) {
        ArrayList<String> lines = new ArrayList<>();
        boolean linesTOCut = true;
        while (linesTOCut) {

            int indexToDownLine = gText.indexOf("#");
            if (indexToDownLine == -1) {
                linesTOCut = false;
                lines.add(gText);
            } else {
                String beforLineText = gText.substring(0, indexToDownLine);
                beforLineText.replace("#", "");
                lines.add(beforLineText);

                if (gText.length() - 1 != indexToDownLine) {
                    gText = gText.substring(indexToDownLine + 1);
                }
            }
        }
        return lines;
    }

    public static Bitmap addFrame(Context context, Bitmap bitmap, String srcFrame, boolean shouldUseSmallFrame) {

        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
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
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        border = BitmapFactory.decodeStream(image_stream, null, options); // TODO option to insert png
       /* FileOutputStream out = null;
            try {
             out = new FileOutputStream(srcFrame);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        border.setHasAlpha(true);
        border.compress(Bitmap.CompressFormat.PNG, 100, baos);

        border = border.copy(bitmapConfig, true);


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap image,bmOverlay;
        if(shouldUseSmallFrame){
            image = Bitmap.createScaledBitmap(bitmap,border.getWidth(), border.getHeight(), false);
            bmOverlay= Bitmap.createScaledBitmap(border,border.getWidth(), border.getHeight(), false);
        }else{
            image = Bitmap.createScaledBitmap(bitmap, border.getWidth(), 9 * (border.getHeight()/10), false);
            bmOverlay = Bitmap.createBitmap(border.getWidth(), border.getHeight(), border.getConfig());
        }
        //Bitmap change = Bitmap.createScaledBitmap(bitmap, width+100, height+100, false);
        //Bitmap frame = Bitmap.createScaledBitmap(border,width,height, false);

        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(image, 0, 0, null);

        //canvas.drawBitmap(scaledBorder, 50, 50 ,null);
        canvas.drawBitmap(border, 0, 0, null);
        return /*makeTransparent(change,0)*/bmOverlay;
    }

    public static Bitmap addSmallImg(Context context, Bitmap originalImage, String srcImage) {

        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        android.graphics.Bitmap.Config bitmapConfig =
                originalImage.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        originalImage = originalImage.copy(bitmapConfig, true);

        Bitmap smallImg = null;
        InputStream image_stream = null;
        Uri uri = Uri.parse(srcImage);
        try {
            image_stream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        smallImg = BitmapFactory.decodeStream(image_stream, null, options); // TODO option to insert png
        int originalImageWidth = originalImage.getWidth();
        int originalImageHeight = originalImage.getHeight();
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        //int smallImageX = (originalImageWidth / 100) * 25;
        int smallImageY = (originalImageHeight / 100) * 89;
        int centerSmallImagX = smallImg.getWidth()/2;

        Canvas canvas = new Canvas(originalImage);
        canvas.drawBitmap(smallImg,originalImageWidth/2-centerSmallImagX, smallImageY, paint);
        return originalImage;
    }
}
