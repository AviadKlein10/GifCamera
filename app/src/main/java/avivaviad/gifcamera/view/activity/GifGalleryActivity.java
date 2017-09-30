package avivaviad.gifcamera.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
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
import io.realm.RealmResults;

/**
 * Created by Aviad on 25/09/2017.
 */

public class GifGalleryActivity extends BaseActivity implements GifGalleryPresenter.GifGallaryListener, AdapterGifGrid.ItemClickListener, AdapterGifGrid.ItemLongClickListener, BaseView, TextWatcher {

    private AdapterGifGrid adapter;
    private List<GifObject> mArrGifs;
    private EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_grid);
        // data to populate the RecyclerView with

        mArrGifs = new ArrayList<>();

        // set up the RecyclerView
        // Log.d("fckckck",mArrGifs.get(0).getBitmapPaths().get(1)+"");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gif_grid);
        int numberOfColumns = 3;
        search = (EditText) findViewById(R.id.gallary_ed);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new AdapterGifGrid(this, mArrGifs);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setAdapter(adapter);

        search.addTextChangedListener(this);

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
     AlertDialog  alertDialog =  new AlertDialog.Builder(GifGalleryActivity.this)
                .setTitle("מחיקת פריט")
                .setMessage("האם אתה בטוח שברצונך למחוק את קובץ הGIF?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RealmHelper.removeGif(mArrGifs.get(position).getmGifSrc(), Realm.getDefaultInstance());
                        adapter.notifyDataSetChanged();
                        // do the acknowledged action, beware, this is run on UI thread
                    }
                })
                .create();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        ((GifGalleryPresenter) mPresenter).onItemGifClick(mArrGifs.get(position).getTimeStamp(),mArrGifs.get(position).getFrameDuration());
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ((GifGalleryPresenter) mPresenter).searchGifsByTag(s.toString(), Realm.getDefaultInstance());

    }

    @Override
    public void afterTextChanged(Editable s) {


    }

    @Override
    public void onSearchArrived(RealmResults<GifObject> gifsOjects) {
        mArrGifs = gifsOjects;
        adapter.setdata(mArrGifs);

        if (mArrGifs.size() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_results), Toast.LENGTH_LONG).show();
        }
    }
}
