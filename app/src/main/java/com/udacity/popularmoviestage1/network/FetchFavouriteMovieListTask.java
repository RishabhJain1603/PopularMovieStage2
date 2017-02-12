package com.udacity.popularmoviestage1.network;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.udacity.popularmoviestage1.BuildConfig;
import com.udacity.popularmoviestage1.adapter.ImageAdapter;
import com.udacity.popularmoviestage1.data.MovieContract;
import com.udacity.popularmoviestage1.model.ImageObject;
import com.udacity.popularmoviestage1.model.Results;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rj125e on 1/24/2017.
 */

public class FetchFavouriteMovieListTask extends AsyncTask<String, Void, ArrayList<ImageObject>> {

    private final String LOG_TAG = FetchFavouriteMovieListTask.class.getSimpleName();


    private Context mContext;
    private ImageAdapter imageAdapter;

    public FetchFavouriteMovieListTask(Context context, ImageAdapter imageAdapter) {
        this.mContext = context;
        this.imageAdapter = imageAdapter;
    }



    @Override
    protected ArrayList<ImageObject> doInBackground(String... params) {


        String[] projection = {MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE};
        int ID_INDEX = 0;
        int OVERVIEW_INDEX = 1;
        int ORIGINAL_TITLE_INDEX = 2;
        int POSTER_PATH_INDEX = 3;
        int VOTE_AVERAGE_INDEX = 4;
        int RELEASE_DATE_INDEX = 5;
        Cursor favMovies = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        ArrayList<ImageObject> imageList = null;
        if(favMovies.getCount()!=0) {
            imageList = new ArrayList<>(favMovies.getCount());
            int i = 0;
            while(favMovies.moveToNext()) {
                ImageObject imageObj = new ImageObject();
                imageObj.setId(favMovies.getString(ID_INDEX));
                imageObj.setOriginal_title(favMovies.getString(OVERVIEW_INDEX));
                imageObj.setOriginal_title(favMovies.getString(ORIGINAL_TITLE_INDEX));
                imageObj.setPoster_path(favMovies.getString(POSTER_PATH_INDEX));
                imageObj.setVote_average(favMovies.getString(VOTE_AVERAGE_INDEX));
                imageObj.setRelease_date(favMovies.getString(RELEASE_DATE_INDEX));

                imageList.add(imageObj);
                i++;
            }
            favMovies.close();
        }


        return imageList;

    }


    @Override
    protected void onPostExecute(ArrayList<ImageObject> imageObjects) {
        imageAdapter.clear();

        if(imageObjects!=null && imageObjects.size()>0){

            for(ImageObject a : imageObjects){
                imageAdapter.add(a);
            }
            imageAdapter.notifyDataSetChanged();
        }
    }
}
