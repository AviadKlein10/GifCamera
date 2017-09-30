package avivaviad.gifcamera.view.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import avivaviad.gifcamera.R;

/**
 * Created by DELL on 19/07/2017.
 */

public class PreviewFrag extends Fragment implements View.OnClickListener {
    ////
    private ImageView imageView;
    private AnimationDrawable animationDrawable;
    private ProgressBar progressBar;
    ///
    private ImageView shareImage, backImage;
    private PreviewCallBack listener;
    private int frame_rate;
    private float alpha;
    private Handler handler;
    private Runnable runnebleFade;
    private int fromActivity;

    public int getFromActivity() {
        return fromActivity;
    }

    public void setFromActivity(int fromActivity) {
        this.fromActivity = fromActivity;
    }

    private void setListener(PreviewCallBack listener) {
        this.listener = listener;
    }

    private void setFrame_rate(int frame_rate) {
        this.frame_rate = frame_rate;
    }

    public static PreviewFrag newInstance(PreviewCallBack cameraFragCallBack, int frame_rate) {
        PreviewFrag previewFrag = new PreviewFrag();
        previewFrag.setFrame_rate(frame_rate);
        previewFrag.setListener(cameraFragCallBack);
        return previewFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preview, container, false);

        backImage = (ImageView) v.findViewById(R.id.back);
        shareImage = (ImageView) v.findViewById(R.id.share);
        imageView = (ImageView) v.findViewById(R.id.preview_image);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        alpha = 0.5f;
        animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(false);
        shareImage.setOnClickListener(this);
        backImage.setOnClickListener(this);

        shareImage.setAlpha(alpha);
        handler = new Handler();
        runnebleFade = new Runnable() {
            @Override
            public void run() {
                shareImage.setAlpha(alpha);
                fadeInButtonShare();
            }
        };
        fadeInButtonShare();
        shareImage.setEnabled(false);

        return v;
    }


    public void startImagesPreview(ArrayList<Bitmap> bitmaps){
       createImageSet(bitmaps);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                listener.onSharePressed();
                break;

            case R.id.back:
                listener.onBackClicked(getFromActivity());
                break;


        }
    }



    private void fadeInButtonShare(){
        alpha +=0.1;
        if(alpha<1){
            handler.postDelayed(runnebleFade,1000);
        }
    }

    private void createImageSet(ArrayList<Bitmap> bmps) {
        BitmapDrawable bitmapDrawable;
        for (int i = 0; i < bmps.size(); i++) {
            bitmapDrawable = new BitmapDrawable(getResources(), bmps.get(i));
            animationDrawable.addFrame(bitmapDrawable, frame_rate);
        }
        imageView.setBackground(animationDrawable);
        /*android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = bmps.get(0).getWidth();
        layoutParams.height = bmps.get(0).getHeight();
        imageView.setLayoutParams(layoutParams);*/
        animationDrawable.start();
    }

    public void makeGifSharable(String uri) {
        handler.removeCallbacks(runnebleFade);
        progressBar.setVisibility(View.INVISIBLE);
    shareImage.setAlpha(1f);
    shareImage.setEnabled(true);
    }


}


/////////////




    /*private Bitmap downSizeBitmapFromUri(Uri imageUri) throws FileNotFoundException {
        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, false);
    }
*/


