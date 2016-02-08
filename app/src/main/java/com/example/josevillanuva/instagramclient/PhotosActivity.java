package com.example.josevillanuva.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();
        aPhotos = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        //send out api request
        fetPopularPhotos();
    }

    public java.util.Date GetPostedDate(String value){
        long timePostMil = Long.parseLong(value);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timePostMil * 1000L);
        return calendar.getTime();
    }

    public String GetStringFromJsonObject(JSONObject jsonObject, String propertyName){
        try{
            return jsonObject.getString(propertyName);
        }
        catch (JSONException e){
            e.printStackTrace();
            return "";
        }
    }

    public int GetIntFromJsonObject(JSONObject jsonObject, String propertyName){
        try{
            return jsonObject.getInt(propertyName);
        }
        catch (JSONException e){
            e.printStackTrace();
            return -1;
        }
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
                    photosJSON = response.getJSONArray("data");
                    if(photosJSON != null){
                        for (int i = 0; i < photosJSON.length(); i++) {
                            JSONObject photoJSON = photosJSON.getJSONObject(i);
                            InstagramPhoto photo = new InstagramPhoto();
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
                                    //photo.imageUrl = GetStringFromJsonObject(stdResolution, "url");
                                    photo.imageUrl = stdResolution.getString("url");

                                    //photo.imageHeight = GetIntFromJsonObject(stdResolution, "height");
                                    photo.imageHeight = stdResolution.getInt("height");
                                }
                            }

                            JSONObject likesJson = photoJSON.getJSONObject("likes");
                            if(likesJson != null){
                                //photo.likesCount = GetIntFromJsonObject(likesJson, "count");
                                photo.likesCount = likesJson.getInt("count");
                            }

//                            String secondsString = GetStringFromJsonObject(photoJSON, "created_time");
//                            if(secondsString  != ""){
//                                photo.datePosted = GetPostedDate(photoJSON.getString(secondsString));
//                            }

                            photo.datePosted = GetPostedDate(photoJSON.getString("created_time"));

                            //photo.datePosted = GetPostedDate(photoJSON.getString("created_time"));
                            //photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                            //photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                            //photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                            //photo.datePosted = GetPostedDate(photoJSON.getString("created_time"));


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
}
