package com.example.eioms;

import android.os.Parcel;
import android.os.Parcelable;

public class Bean implements Parcelable {

    private String id;
    private String userid;
    private String username;
    private String title;
    private String content;
    private String time;
    private String reply;

    public Bean(){

    }

    protected Bean(Parcel in) {
        id = in.readString();
        userid = in.readString();
        username = in.readString();
        title = in.readString();
        content = in.readString();
        time = in.readString();
        reply = in.readString();
    }

    public static final Creator<Bean> CREATOR = new Creator<Bean>() {
        @Override
        public Bean createFromParcel(Parcel in) {
            return new Bean(in);
        }

        @Override
        public Bean[] newArray(int size) {
            return new Bean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userid);
        parcel.writeString(username);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(time);
        parcel.writeString(reply);

    }
}
