package avivaviad.gifcamera;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import avivaviad.gifcamera.model.GifObject;
import io.realm.Realm;

/**
 * Created by Aviad on 27/09/2017.
 */

public class RealmHelper {

    public static List<GifObject> getAllGifs(Realm mRealm) {
        return mRealm.where(GifObject.class).findAll();
    }
 public static List<GifObject> getAllGifsWithTag(Realm mRealm,String searchWord) {
        return mRealm.where(GifObject.class).contains("Tag",searchWord).findAll();
    }

    public static GifObject saveGif(final GifObject realmGif, Realm mRealm) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realmGif);
            }
        });
        return realmGif;
    }

    public static GifObject loadGif(String timeStamp,Realm mRealm){
        return mRealm.where(GifObject.class).contains("timeStamp",timeStamp).findFirst();
    }

    public static void removeGif(final String gifSrc, Realm mRealm) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                GifObject result = realm.where(GifObject.class).equalTo("mGifSrc", gifSrc).findFirst();
                result.deleteFromRealm();
            }
        });
    }

    public static void saveGifPreviewBitmaps(final ArrayList<Bitmap> previewBitmaps, Realm mRealm) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
               // realm.copyToRealmOrUpdate(previewBitmaps);
            }
        });

    }
}
