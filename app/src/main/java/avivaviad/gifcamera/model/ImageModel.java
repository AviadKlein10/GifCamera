package avivaviad.gifcamera.model;

import io.realm.RealmObject;

/**
 * Created by DELL on 18/07/2017.
 */

public class ImageModel extends RealmObject {

    private String imagePath;

    public ImageModel() {
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
