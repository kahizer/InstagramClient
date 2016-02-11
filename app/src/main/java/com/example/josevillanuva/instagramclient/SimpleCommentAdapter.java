package com.example.josevillanuva.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
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
 * Created by josevillanuva on 2/7/16.
 */
public class SimpleCommentAdapter extends ArrayAdapter<PhotoComment> {
    public SimpleCommentAdapter(Context context, List<PhotoComment> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        try{

            PhotoComment comment = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
            }

            TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
            String combinedComment = String.format("<font color=\"#125688\"><B>" + comment.username + "<B></font> " + comment.text);
            tvComment.setText(Html.fromHtml(combinedComment));

            ImageView ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.BLACK)
                    .borderWidthDp(1)
                    .cornerRadiusDp(100)
                    .oval(false)
                    .build();

            Picasso.with(getContext())
                    .load(comment.userPictureurl)
                    .fit()
                    .transform(transformation)
                    .into(ivProfilePicture);


            TextView tvTimeElapsed = (TextView) convertView.findViewById(R.id.tvTimeElapsed);
            tvTimeElapsed.setText(this.GetSlapsedTime(comment.datePosted));

            return convertView;
        }
        catch (Exception ex){
            Log.v("", ""+ex);
        }

        return null;
    }

    public String GetSlapsedTime(java.util.Date postDate) {
        java.util.Date currentDateTime = new java.util.Date();
        long minutesElapsed = (currentDateTime.getTime() - postDate.getTime())/60000;
        int hours = (int)minutesElapsed/60;

        if(hours < 1){
            return ""+minutesElapsed + " minutes ago";
        }else if(hours < 24){
            return ""+hours+" hours ago";
        }else if(hours < 168){
            int days = hours/24;
            return ""+days+" days ago";
        }else{
            int weeks = hours/168;
            return ""+weeks+" weeks ago";
        }
    }
}
