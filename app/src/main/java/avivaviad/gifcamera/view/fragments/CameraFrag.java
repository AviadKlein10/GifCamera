package avivaviad.gifcamera.view.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import avivaviad.gifcamera.R;

/**
 * Created by DELL on 18/07/2017.
 */

public class CameraFrag extends Fragment implements TextureView.SurfaceTextureListener {


    public static int maxSize = 920;
    private TextureView myTexture;
    private Camera mCamera;
    private Handler handler;
    private TextView countDownNumber, frameCounter;
    private Button start;
    private ProgressBar progressBar;


    private CameraFragCallBack listener;
    private int camHeight, camWidth;



    private void setListener(CameraFragCallBack listener) {
        this.listener = listener;
    }

    public static CameraFrag newInstance(CameraFragCallBack cameraFragCallBack) {
        CameraFrag cameraFrag = new CameraFrag();
        cameraFrag.setListener(cameraFragCallBack);
        return cameraFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);

        frameCounter = (TextView) v.findViewById(R.id.frame_counter);
        start = (Button) v.findViewById(R.id.button_capture);
        countDownNumber = (TextView) v.findViewById(R.id.count_down_num);
        myTexture = (TextureView) v.findViewById(R.id.textureView1);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBarCamera);

        myTexture.setSurfaceTextureListener(this);


        countDownNumber.setVisibility(View.GONE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(false);
                listener.onStartClick();
            }
        });


        return v;


    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1, int arg2) {


        mCamera = Camera.open(findFrontFacingCamera());
      /*  try {
            Camera.Parameters parameters = mCamera.getParameters();

            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                // 640 480
                // 960 720
                // 1024 768
                // 1280 720
                // 1600 1200
                // 2560 1920
                // 3264 2448
                // 2048 1536
                // 3264 1836
                // 2048 1152
                // 3264 2176

                if (1600 <= size.width & size.width <= 1920) {
                    parameters.setPreviewSize(size.width, size.height);
                    parameters.setPictureSize(size.width, size.height);
                    Log.d("mymnq", size.height + "");
                    Log.d("mymnq", size.width + "");
                    break;
                }
                Log.d("mymn", size.height + "");
                Log.d("mymn", size.width + "");

            }
            // Set parameters for camera
            mCamera.setParameters(parameters);
            Log.d("mymnx", myTexture.getHeight() + "");
            Log.d("mymnx", myTexture.getWidth() + "");
            */
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        camHeight = size.height;
        camWidth = size.width;
        if (displayOnTablet(camHeight)) {
            camHeight = camHeight * 2;
            camWidth = camWidth * 2;
        }
        maxSize = camWidth;
        listener.setQuality(maxSize);
        Log.d("aniFrag",maxSize+"");
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(camHeight, camWidth);
        mCamera.getParameters().setPictureSize(camHeight,camWidth);
        mCamera.getParameters().setPreviewSize(camHeight,camWidth);
        myTexture.setLayoutParams(layoutParams);
      //  myTexture.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        Log.d("mymnxx", size.height + "");
        Log.d("mymnxx", size.width + "");



         /*
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();

        camHeight = previewSize.height*3;
        camWidth = previewSize.width*3;

        Log.d("mymn", mCamera.getParameters().getPreviewSize().height + "");
        Log.d("mymn", mCamera.getParameters().getPreviewSize().width + "");

        myTexture.setLayoutParams(new FrameLayout.LayoutParams(camHeight, camWidth, Gravity.CENTER));
        */
        mCamera.setDisplayOrientation(90);
        try {
           // progressBar.setVisibility(View.VISIBLE);//TODO can o lo
            mCamera.setPreviewTexture(arg0);
        } catch (IOException t) {
        }
        mCamera.startPreview();

    }

    private boolean displayOnTablet(int camHeight) {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        if (camHeight * 2 <= height && camWidth * 2 <= width) {
            Log.d("mymnxz","ma");
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
                                            int arg2) {
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
    }


    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("ss", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }


    public Bitmap getBitmapFromTextureView() {
        return myTexture.getBitmap(camWidth, camHeight);
    }


    public void showCountDownText(boolean enable) {
        if (enable) {

            countDownNumber.setVisibility(View.VISIBLE);
        } else {
            countDownNumber.setVisibility(View.GONE);

        }

    }

    public void changeCountDownText(String text) {
        countDownNumber.setText(String.valueOf(text));
    }

    public void changeFrameCounter(String text) {
        frameCounter.setVisibility(View.VISIBLE);
        frameCounter.setText(text);
    }


}
