package avivaviad.gifcamera.presenter;

import android.content.Intent;

import java.util.List;

import avivaviad.gifcamera.Constans;
import avivaviad.gifcamera.RealmHelper;
import avivaviad.gifcamera.model.GifObject;
import avivaviad.gifcamera.view.activity.GifCameraActivity;
import avivaviad.gifcamera.view.activity.GifGalleryActivity;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Aviad on 27/09/2017.
 */

public class GifGalleryPresenter extends Presenter<GifGalleryActivity> {


    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    public List<GifObject> onGifListReady() {
        return RealmHelper.getAllGifs(mRealm);
    }


    public void onItemGifClick(String timeStamp, String frameDuration,String uri) {
        Intent intent = new Intent(mView.getApplicationContext(),GifCameraActivity.class);
        intent.putExtra("frag", Constans.FRAG_PREVIEW);
        intent.putExtra("activity",Constans.ACTIVITY_GALLERY);
        intent.putExtra("time_stamp",timeStamp);
        intent.putExtra("frame_duration",frameDuration);
        intent.putExtra("gif_uri",uri);
        mView.startActivity(intent);

    }

    public void searchGifsByTag(String tag, Realm realm) {
        RealmResults<GifObject> gifsOjects = RealmHelper.loadGifsByTag(tag, realm);
        mView.onSearchArrived(gifsOjects);

    }


    public interface GifGallaryListener {
        void onSearchArrived(RealmResults<GifObject> gifsOjects);
    }


}
