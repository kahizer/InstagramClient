package com.example.josevillanuva.instagramclient;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by josevillanuva on 2/5/16.
 */
public class InstagramPhoto implements Parcelable{
    public String username;
    public String caption;
    public String imageUrl;
    public String profilePictureUrl;
    public Date datePosted;
    public int imageHeight;
    public int likesCount;

    //public ArrayList<PhotoComment> comments = new ArrayList<>();
    //public PhotoComment[] comments;

    public List<PhotoComment> comments;

    public InstagramPhoto() {
    }

    protected InstagramPhoto(Parcel in) {
        try{
            username = in.readString();
            caption = in.readString();
            imageUrl = in.readString();
            profilePictureUrl = in.readString();
            datePosted = new Date(in.readString());
            imageHeight = in.readInt();
            likesCount = in.readInt();
            comments = new ArrayList<PhotoComment>();
            in.readList(comments, getClass().getClassLoader());
        }
        catch (Exception ex){
            Log.v("",""+ex);
        }

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public InstagramPhoto createFromParcel(Parcel in) { return new InstagramPhoto(in);}

        public InstagramPhoto[] newArray(int size) { return new InstagramPhoto[size];}
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("", "writeToParcel..." + flags);
        try{
            dest.writeString(username);
            dest.writeString(caption);
            dest.writeString(imageUrl);
            dest.writeString(profilePictureUrl);
            dest.writeString(datePosted.toString());
            dest.writeInt(imageHeight);
            dest.writeInt(likesCount);
            dest.writeList(comments);
            //dest.writeTypedArray(comments, 0);
        }
        catch (Exception ex){
            Log.v("", "" + ex);
        }
    }
}
