package avivaviad.gifcamera.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import avivaviad.gifcamera.custom.dialog.DialogLoader;
import avivaviad.gifcamera.presenter.Presenter;


/**
 * Created by Ofer Dan-On on 1/20/2017.
 */

public abstract class BaseActivity<P extends Presenter> extends AppCompatActivity {
    public P mPresenter;
    private DialogLoader dialogLoader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        bind();

    }

    @Override
    protected void onResume() {
        super.onResume();
       // App.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // App.activityPaused();
    }



    public void showLoader(boolean enable) {
        if (enable) {
            dialogLoader = new DialogLoader();
            dialogLoader.show(getFragmentManager(), "loader");
        } else if (dialogLoader != null) {
            try {
                dialogLoader.dismiss();
            } catch (Exception e) {

            }

        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called BEFORE the view is created and binds the presenter
     * to lifecycle events (onResume and onPause)
     *
     * @return The Presenter instance connected to this fragment
     */
    protected abstract P getPresenter();

    protected abstract void bind();

    protected abstract void unbind();

}
