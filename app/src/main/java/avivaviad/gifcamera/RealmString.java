package avivaviad.gifcamera;

import io.realm.RealmObject;

/**
 * Created by Aviad on 28/09/2017.
 */

public class RealmString extends RealmObject {

    private String imgPath;

    public RealmString(String imgPath) {
        this.imgPath = imgPath;
    }
    public RealmString(){}

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
