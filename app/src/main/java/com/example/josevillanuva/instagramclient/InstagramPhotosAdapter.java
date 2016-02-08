package com.example.josevillanuva.instagramclient;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by josevillanuva on 2/5/16.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        InstagramPhoto photo = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvTimeElapsed = (TextView) convertView.findViewById(R.id.tvTimeElapsed);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView ivUserPicture = (ImageView) convertView.findViewById(R.id.ivUserPicture);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);

        //tvCaption.setText(photo.caption);
        String fullCaptions = String.format("<font color=\"#125688\"><B>"+ photo.username+"<B></font> " + photo.caption);
        tvCaption.setText(Html.fromHtml(fullCaptions));

        tvUserName.setText(photo.username);

        tvTimeElapsed.setText(GetSlapsedTime(photo.datePosted));
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        ivUserPicture.setImageResource(0);
        Picasso.with(getContext()).load(photo.profilePictureUrl).into(ivUserPicture);

        tvLikesCount.setText("❤ " +photo.likesCount + " Likes");

        return convertView;
    }

    public String GetSlapsedTime(java.util.Date postDate) {
        java.util.Date currentDateTime = new java.util.Date();
        long minutesElapsed = (currentDateTime.getTime() - postDate.getTime())/60000;
        int hours = (int)minutesElapsed/60;

        if(hours < 1){
            return ""+minutesElapsed + "m";
        }else{
            return ""+hours+"h";
        }
    }
}
