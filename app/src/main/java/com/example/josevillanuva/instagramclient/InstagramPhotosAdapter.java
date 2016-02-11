package com.example.josevillanuva.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
        TextView tvComment01 = (TextView) convertView.findViewById(R.id.tvComment01);
        TextView tvComment02 = (TextView) convertView.findViewById(R.id.tvComment02);
        TextView tvComment03 = (TextView) convertView.findViewById(R.id.tvComment03);

        String fullCaptions = String.format("<font color=\"#125688\"><B>"+ photo.username+"<B></font> " + photo.caption);
        tvCaption.setText(Html.fromHtml(fullCaptions));

        int commentsCount = photo.comments.size();
        if(commentsCount > 3){
            tvComment01.setText("View all " + commentsCount + " comments");

            String combinedComment02 = String.format("<font color=\"#125688\"><B>"+ photo.comments.get(commentsCount-2).username+"<B></font> " + photo.comments.get(commentsCount-2).text);
            tvComment02.setText(Html.fromHtml(combinedComment02));

            String combinedComment03 = String.format("<font color=\"#125688\"><B>"+ photo.comments.get(commentsCount-1).username+"<B></font> " + photo.comments.get(commentsCount-1).text);
            tvComment03.setText(Html.fromHtml(combinedComment03));
        }

        tvUserName.setText(photo.username);

        tvTimeElapsed.setText(GetSlapsedTime(photo.datePosted));
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);


        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(100)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(photo.profilePictureUrl)
                .fit()
                .transform(transformation)
                .into(ivUserPicture);

        tvLikesCount.setText("❤ " +photo.likesCount + " Likes");

        return convertView;
    }

    public String GetSlapsedTime(java.util.Date postDate) {
        java.util.Date currentDateTime = new java.util.Date();
        long minutesElapsed = (currentDateTime.getTime() - postDate.getTime())/60000;
        int hours = (int)minutesElapsed/60;

        if(hours < 1){
            return ""+minutesElapsed + "m";
        }else if(hours < 24){
            return ""+hours+"h";
        }else if(hours < 168){
            int days = hours/24;
            return ""+days+"d";
        }else{
            int weeks = hours/168;
            return ""+weeks+"w";
        }
    }
}
