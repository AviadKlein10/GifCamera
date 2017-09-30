package avivaviad.gifcamera.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import avivaviad.gifcamera.R;
import avivaviad.gifcamera.RealmHelper;
import avivaviad.gifcamera.model.AdapterGifGrid;
import avivaviad.gifcamera.model.GifObject;
import avivaviad.gifcamera.presenter.BaseView;
import avivaviad.gifcamera.presenter.GifGalleryPresenter;
import avivaviad.gifcamera.presenter.Presenter;
import avivaviad.gifcamera.view.BaseActivity;
import io.realm.Realm;

/**
 * Created by Aviad on 25/09/2017.
 */

public class GifGalleryActivity extends BaseActivity implements AdapterGifGrid.ItemClickListener,AdapterGifGrid.ItemLongClickListener,BaseView {

    private AdapterGifGrid adapter;
    private   List<GifObject> mArrGifs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_grid);
        // data to populate the RecyclerView with

         mArrGifs = ((GifGalleryPresenter)mPresenter).onGifListReady();
        // set up the RecyclerView
       // Log.d("fckckck",mArrGifs.get(0).getBitmapPaths().get(1)+"");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gif_grid);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new AdapterGifGrid(this, mArrGifs);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected Presenter getPresenter() {
        return new GifGalleryPresenter();
    }

    @Override
    protected void bind() {
        mPresenter.bind(this);
    }

    @Override
    protected void unbind() {
        mPresenter.unbind();
    }


    private void buildAlertDialog(final int position) {
        new AlertDialog.Builder(GifGalleryActivity.this)
                .setTitle("מחיקת פריט")
                .setMessage("האם אתה בטוח שברצונך למחוק את קובץ הGIF?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        RealmHelper.removeGif(mArrGifs.get(position).getmGifSrc(), Realm.getDefaultInstance());
                        adapter.notifyDataSetChanged();
                        // do the acknowledged action, beware, this is run on UI thread
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onItemClick(View view, int position) {
        ((GifGalleryPresenter) mPresenter).onItemGifClick(mArrGifs.get(position).getTimeStamp());
    }


    @Override
    public void onError(String message) {

    }

    @Override
    public void onLongItemClick(View view, int position) {
        buildAlertDialog(position);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StartActivity.class));
        super.onBackPressed();
    }
}
