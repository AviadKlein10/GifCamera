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
import android.widget.TextView;
import android.widget.Toast;

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

    private boolean cameFromGallary;
    private ImageView createGifBtn, backImage;
    private PreviewCallBack listener;
    private int frame_rate;
    private Handler handler;
    private Runnable runnebleFade;
    private int fromActivity;
    private TextView loadingTxtView;
    private TextView mainLoading;


    public void setCameFromGallary(boolean cameFromGallary) {
        this.cameFromGallary = cameFromGallary;
    }

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

    public static PreviewFrag newInstance(PreviewCallBack cameraFragCallBack, int frame_rate,boolean cameFromGallary) {
        PreviewFrag previewFrag = new PreviewFrag();
        previewFrag.setFrame_rate(frame_rate);
        previewFrag.setListener(cameraFragCallBack);
        previewFrag.setCameFromGallary(cameFromGallary);
        return previewFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preview, container, false);

        backImage = (ImageView) v.findViewById(R.id.back);
        createGifBtn = (ImageView) v.findViewById(R.id.share);
        imageView = (ImageView) v.findViewById(R.id.preview_image);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        mainLoading = (TextView) v.findViewById(R.id.main_loading);
        /*if(getFromActivity()!= Constans.ACTIVITY_GALLERY){
           // v.findViewById(R.id.recapture).setVisibility(View.VISIBLE);
        }*/
        loadingTxtView = (TextView)v.findViewById(R.id.loading);

        animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(false);
        createGifBtn.setOnClickListener(this);
        backImage.setOnClickListener(this);
        if(cameFromGallary){
            loadingTxtView.setVisibility(View.INVISIBLE);
            backImage.setImageResource(R.drawable.no_gallery_btn);
        }else{
            createGifBtn.setEnabled(false);
            backImage.setImageResource(R.drawable.no_btn);
        }
        return v;
    }


    public void startImagesPreview(ArrayList<Bitmap> bitmaps) {
        createImageSet(bitmaps);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
               // listener.onSharePressed(); // TODO add share when finish generate
                if(cameFromGallary){
                    listener.onSharePressed();
                }else{
                    listener.onCreateGifPressed();
                }
                break;

            case R.id.back:
                listener.onBackClicked(getFromActivity());
                break;

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
        mainLoading.setVisibility(View.INVISIBLE);
    }

    public void makeGifSharable() {

     //   handler.post(runnebleFade);
        loadingTxtView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        createGifBtn.setAlpha(1f);
       // createGifBtn.setEnabled(true);
    }
    public void makeButtonOKEnable() {
      //  handler.post(runnebleFade);
        loadingTxtView.setVisibility(View.INVISIBLE);
        mainLoading.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        createGifBtn.setAlpha(1f);
        createGifBtn.setEnabled(true);
    }


    public void startProgressDialog() {
        progressBar.setVisibility(View.VISIBLE);
        loadingTxtView.setVisibility(View.VISIBLE);
        createGifBtn.setEnabled(false);
    }

    public void stopProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        loadingTxtView.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity().getApplicationContext(), "Gif saved successfully", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onSharePressed();
            }
        },400);

    }
}


/////////////




    /*private Bitmap downSizeBitmapFromUri(Uri imageUri) throws FileNotFoundException {
        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, false);
    }
*/


