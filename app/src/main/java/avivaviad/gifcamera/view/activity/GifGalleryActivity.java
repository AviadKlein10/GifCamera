package avivaviad.gifcamera.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avivaviad.gifcamera.R;
import avivaviad.gifcamera.RealmHelper;
import avivaviad.gifcamera.custom.RecyclerItemClickListener;
import avivaviad.gifcamera.custom.dialog.DialogFragmentDelete;
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

public class GifGalleryActivity extends BaseActivity implements GifGalleryPresenter.GifGallaryListener, AdapterGifGrid.ItemClickListener, AdapterGifGrid.ItemLongClickListener, BaseView, TextWatcher,DialogFragmentDelete.YesNoDialogListener,ActionMode.Callback {

    private AdapterGifGrid adapter;
    private List<GifObject> mArrGifs;
    private EditText search;
    private Spinner spinnerLastTag;
    private ActionMode actionMode;
    private boolean isMultiSelect = false;
    private ArrayList<Integer> selectedIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_grid);
        mArrGifs = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gif_grid);
        int numberOfColumns = 6;
        search = (EditText) findViewById(R.id.gallary_ed);
        spinnerLastTag = (Spinner) findViewById(R.id.spinner_last_tags);
        final String [] items = RealmHelper.loadLastTags(Realm.getDefaultInstance());
        if(items!=null){
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            spinnerLastTag.setAdapter(spinnerAdapter);
            spinnerLastTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((GifGalleryPresenter) mPresenter).searchGifsByTag(items[position], Realm.getDefaultInstance());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        }
        adapter = new AdapterGifGrid(this, mArrGifs);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect){
                    //if multiple selection is enabled then select item on single click else perform normal click on item.
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect){
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null){
                        actionMode = startSupportActionMode(GifGalleryActivity.this); //show ActionMode.
                    }
                }

                multiSelect(position);
            }
        }));

        search.addTextChangedListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinnerLastTag.performClick();
            }
        },1000);


    }

    private void multiSelect(int position) {
        GifObject data = adapter.getItem(position);
        if (data != null){
            if (actionMode != null) {
                if (selectedIds.contains(Integer.valueOf(data.getTimeStamp())))
                    selectedIds.remove(Integer.valueOf(data.getTimeStamp()));
                else
                    selectedIds.add(Integer.valueOf(data.getTimeStamp()));

                if (selectedIds.size() > 0)
                    actionMode.setTitle(String.valueOf(selectedIds.size())); //show selected item count on action mode.
                else{
                    actionMode.setTitle(""); //remove item count from action mode.
                    actionMode.finish(); //hide action mode.
                }
                adapter.setSelectedIds(selectedIds);

            }
        }
    }



    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_multi_selection, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_delete:
                //just to show selected items.
                buildAlertDialog(selectedIds);
              //  Toast.makeText(this, "Selected items are :" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedIds = new ArrayList<>();
        adapter.setSelectedIds(new ArrayList<Integer>());
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


    private void buildAlertDialog(ArrayList<Integer> positions) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragmentDelete yesnoDialog = new DialogFragmentDelete();
        yesnoDialog.setPositions(positions);
        yesnoDialog.setCancelable(false);
        yesnoDialog.setDialogTitle("Select One");
        yesnoDialog.show(fragmentManager, "Yes/No Dialog");

    }

    @Override
    public void onFinishYesNoDialog(boolean toDelete, ArrayList<Integer> positions) {
        // -- Finish dialog box show msg
        if(toDelete){
            for (int i = 0; i < positions.size(); i++) {
                Log.d("thisdelete",positions.get(i)+"");
                RealmHelper.removeGif(String.valueOf(positions.get(i)), Realm.getDefaultInstance());
            }
            adapter.setdata(mArrGifs);
            //adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        ((GifGalleryPresenter) mPresenter).onItemGifClick(mArrGifs.get(position).getTimeStamp(), String.valueOf(mArrGifs.get(position).getFrameDuration()),mArrGifs.get(position).getmGifSrc());
    }


    @Override
    public void onError(String message) {

    }

    @Override
    public void onLongItemClick(View view, int position) {
        //buildAlertDialog(position);
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
