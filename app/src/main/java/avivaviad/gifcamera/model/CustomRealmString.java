package avivaviad.gifcamera.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Aviad on 24/10/2017.
 */

public class CustomRealmString extends RealmObject {
    @PrimaryKey
    private String lastTag;
    private String timeStamp;

    public CustomRealmString(){}

    public CustomRealmString(String lastTag,String timeStamp) {
        this.lastTag = lastTag;
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLastTag() {
        return lastTag;
    }

    public void setLastTag(String lastTag) {
        this.lastTag = lastTag;
    }
}
