package com.example.eioms;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

//全局变量保存
public class Data extends Application implements Parcelable {
    private String username;
    private String id;
    private String name;
    private String authority;

    public Data(){}

    protected Data(Parcel in) {
        username = in.readString();
        id = in.readString();
        name = in.readString();
        authority = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getAuthority() {
        return authority;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(authority);
    }
}
