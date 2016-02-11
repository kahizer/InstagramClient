package com.example.josevillanuva.instagramclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    ListView lvPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetPopularPhotos();

                fetchTimelineAsync(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, photos);
        lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        setViewComments();
        fetPopularPhotos();
    }

    public java.util.Date GetPostedDate(String value){
        long timePostMil = Long.parseLong(value);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timePostMil * 1000L);
        return calendar.getTime();
    }

    public void fetPopularPhotos(){
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // expecting a dictionary JSON object

                //Log.i("DEBUG", response.toString());
                JSONArray photosJSON = null;
                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("data");
                    if(photosJSON != null){
                        for (int i = 0; i < photosJSON.length(); i++) {
                            JSONObject photoJSON = photosJSON.getJSONObject(i);
                            InstagramPhoto photo = new InstagramPhoto();
                            photo.comments = new ArrayList<PhotoComment>();

                            JSONObject userJson = photoJSON.getJSONObject("user");
                            if(userJson != null){
                                photo.username = userJson.getString("username") == null ? "" : userJson.getString("username");
                                photo.profilePictureUrl = userJson.getString("profile_picture") == null ? "" : userJson.getString("profile_picture");
                            }

                            JSONObject captionJson = photoJSON.getJSONObject("caption");
                            if(captionJson != null){
                                photo.caption = captionJson.getString("text") == null ? "" : captionJson.getString("text");
                            }

                            JSONObject imageJson = photoJSON.getJSONObject("images");
                            if(imageJson != null){
                                JSONObject stdResolution = imageJson.getJSONObject("standard_resolution");
                                if(stdResolution != null){
                                    photo.imageUrl = stdResolution.getString("url");
                                    photo.imageHeight = stdResolution.getInt("height");
                                }
                            }

                            JSONObject likesJson = photoJSON.getJSONObject("likes");
                            if(likesJson != null){
                                photo.likesCount = likesJson.getInt("count");
                            }

                            photo.datePosted = GetPostedDate(photoJSON.getString("created_time"));

                            JSONObject jsonComment = photoJSON.getJSONObject("comments");
                            if(jsonComment != null){
                                JSONArray commentsJsonArray = jsonComment.getJSONArray("data");
                                for(int j = 0; j < commentsJsonArray.length(); j++){
                                    JSONObject commentJSON = commentsJsonArray.getJSONObject(j);

                                    PhotoComment comment = new PhotoComment();

                                    comment.datePosted = GetPostedDate(commentJSON.getString("created_time"));
                                    comment.text = commentJSON.getString("text");
                                    JSONObject fromJSON = commentJSON.getJSONObject("from");
                                    if(fromJSON != null){
                                        comment.username = fromJSON.getString("username");
                                        comment.userPictureurl = fromJSON.getString("profile_picture");
                                    }

                                    photo.comments.add(comment);
                                }
                            }

                            photos.add(photo);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aPhotos.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // do something
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
//        client.getHomeTimeline(0, new JsonHttpResponseHandler() {
//            public void onSuccess(JSONArray json) {
//                // Remember to CLEAR OUT old items before appending in the new ones
//                adapter.clear();
//                // ...the data has come back, add new items to your adapter...
//                adapter.addAll(...);
//                // Now we call setRefreshing(false) to signal refresh has finished
//                swipeContainer.setRefreshing(false);
//            }
//
//
//
//            public void onFailure(Throwable e) {
//                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
//            }
//        });

        swipeContainer.setRefreshing(false);
    }


    private void setViewComments(){
        lvPhotos.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        InstagramPhoto currentPhoto = photos.get(pos);

                        Intent i = new Intent(PhotosActivity.this, CommentsDisplayActivity.class);
                        Bundle b = new Bundle();
                        b.putParcelable("InstagramPhoto", currentPhoto);
                        i.putExtras(b);
                        startActivity(i);
                    }
                }
        );
    }
}
