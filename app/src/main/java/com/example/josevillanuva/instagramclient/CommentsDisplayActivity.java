package com.example.josevillanuva.instagramclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class CommentsDisplayActivity extends AppCompatActivity {

    private ArrayList<PhotoComment> comments;
    private SimpleCommentAdapter aComment;
    ListView lvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_display);

        comments = new ArrayList<PhotoComment>();
        aComment = new SimpleCommentAdapter(this, comments);
        lvComments = (ListView) findViewById(R.id.lvComments);
        lvComments.setAdapter(aComment);

        Bundle b = this.getIntent().getExtras();
        if(b != null){

            try{
                InstagramPhoto currentPhoto = b.getParcelable("InstagramPhoto");

                if(currentPhoto != null){
                    this.setUserProfilePictureCaption(currentPhoto);
                    //comments = currentPhoto.comments;
                    for (int i = 0; i < currentPhoto.comments.size(); i++){
                        comments.add(currentPhoto.comments.get(i));
                    }

                    aComment.notifyDataSetChanged();
                }
                //Toast.makeText(this, "parsed correctly", Toast.LENGTH_SHORT).show();

            }
            catch (Exception ex){
                Log.v("", ""+ex);
                Toast.makeText(this, "failed to parse", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void setUserProfilePictureCaption(InstagramPhoto photo){
        TextView tvOriginalcaption = (TextView)findViewById(R.id.tvOriginalCaption);
        //tvOriginalcaption.setText(photo.caption);

        String fullCaptions = String.format("<font color=\"#125688\"><B>"+ photo.username+"<B></font> " + photo.caption);
        tvOriginalcaption.setText(Html.fromHtml(fullCaptions));

        ImageView ivOriginalUser = (ImageView) findViewById(R.id.ivOriginalUser);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(100)
                .oval(false)
                .build();

        Picasso.with(this)
                .load(photo.profilePictureUrl)
                .fit()
                .transform(transformation)
                .into(ivOriginalUser);
    }

}
