package avivaviad.gifcamera.view.fragments;

/**
 * Created by DELL on 19/07/2017.
 */

public interface PreviewCallBack {
    void onSharePressed();
    void onCreateGifPressed();
    void onBackClicked(int fromActivity);
    void startGifCreation();
}
