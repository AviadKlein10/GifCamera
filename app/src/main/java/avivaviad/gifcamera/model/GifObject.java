package avivaviad.gifcamera.model;

import avivaviad.gifcamera.RealmString;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Aviad on 25/09/2017.
 */

public class GifObject extends RealmObject {

    @Required
    private String mGifSrc;
    @Required
    private String mGifTag;
    @PrimaryKey
    private String timeStamp;
    private RealmList<RealmString> bitmapPaths;
    private int frameDuration;

    public GifObject(String mGifSrc, String mGifTag) {
        this.mGifSrc = mGifSrc;
        this.mGifTag = mGifTag;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public GifObject() {
    }

    public String getmGifSrc() {
        return mGifSrc;
    }

    public String getmGifTag() {
        return mGifTag;
    }

    public void setmGifSrc(String mGifSrc) {
        this.mGifSrc = mGifSrc;
    }

    public void setmGifTag(String mGifTag) {
        this.mGifTag = mGifTag;
    }

    public RealmList<RealmString> getBitmapPaths() {
        return bitmapPaths;
    }

    public void setBitmapPaths(RealmList<RealmString> bitmapPaths) {
        this.bitmapPaths = bitmapPaths;
    }


    public int getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(int frameDuration) {
        this.frameDuration = frameDuration;
    }
}
