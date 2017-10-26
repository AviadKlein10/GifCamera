package avivaviad.gifcamera;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import avivaviad.gifcamera.model.CustomRealmString;
import avivaviad.gifcamera.model.GifObject;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Aviad on 27/09/2017.
 */

public class RealmHelper {

    private static final String GIF_EVENT_TAG = "mGifTag";

    public static List<GifObject> getAllGifs(Realm mRealm) {
        return mRealm.where(GifObject.class).findAll();
    }

    public static List<GifObject> getAllGifsWithTag(Realm mRealm, String searchWord) {
        return mRealm.where(GifObject.class).contains("Tag", searchWord).findAll();
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

    public static GifObject loadGif(String timeStamp, Realm mRealm) {
        return mRealm.where(GifObject.class).contains("timeStamp", timeStamp).findFirst();
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

    public static RealmResults<GifObject> loadGifsByTag(String tag, Realm realm) {
        return realm.where(GifObject.class).equalTo(GIF_EVENT_TAG, tag).findAll();
    }

    public static void saveLastTag(String lastTag,Realm realm) {
        final CustomRealmString realmString = new CustomRealmString();
        realmString.setLastTag(lastTag);
      // Long tsLong = System.currentTimeMillis() / 1000;
      // String tsStr = tsLong.toString();
      // realmString.setTimeStamp(tsStr);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(realmString);
            }
        });
    }

    public static String[] loadLastTags(Realm realm){
        RealmResults<CustomRealmString>results =
                realm.where(CustomRealmString.class).findAll();
                   //     .findAllSorted("timestamp", Sort.DESCENDING);
        String[] arrLastTags = new String[results.size()];
        int resultsCount = results.size();
        if (resultsCount>20){
            resultsCount=20;
        }
        for (int i = 0; i < resultsCount; i++) {
            arrLastTags[i] = results.get(i).getLastTag();
        }
        Collections.reverse(Arrays.asList(arrLastTags));

        return arrLastTags;
    }
}
