package org.fossasia.openevent.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Track extends RealmObject {

    @Expose
    private String description;

    @Expose
    private RealmList<Session> sessions;

    @Expose
    private String color;

    @SerializedName("track_image_url")
    @Expose
    private String trackImageUrl;

    @Expose
    private String location;

    @Expose
    @PrimaryKey
    private int id;

    @Expose
    @Index
    private String name;

    public RealmList<Session> getSessions() {
        return sessions;
    }

    public String getColor() {
        return color;
    }

    public String getTrackImageUrl() {
        return trackImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setSessions(RealmList<Session> sessions) {
        this.sessions = sessions;
    }
}