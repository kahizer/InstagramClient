package com.example.josevillanuva.instagramclient;

/**
 * Created by josevillanuva on 2/7/16.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.Date;

public class PhotoComment implements Parcelable{
    public String username;
    public String userPictureurl;
    public Date datePosted;
    public String text;

    public PhotoComment(){
    }

    private PhotoComment(Parcel in) {
        this.username = in.readString();
        this.userPictureurl = in.readString();
        this.datePosted = new Date(in.readString());
        this.text = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("", "writingToParcel..." + flags);
        dest.writeString(username);
        dest.writeString(userPictureurl);
        dest.writeString(datePosted.toString());
        dest.writeString(text);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public PhotoComment createFromParcel(Parcel in){
            return new PhotoComment(in);
        }

        public PhotoComment[] newArray(int size){
            return new PhotoComment[size];
        }
    };
}
