package com.udacity.popularmoviestage1.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.udacity.popularmoviestage1.BuildConfig;
import com.udacity.popularmoviestage1.adapter.ImageAdapter;
import com.udacity.popularmoviestage1.adapter.ReveiewAdapter;
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

public class FetchMovieListTask extends AsyncTask<String, Void, ArrayList<ImageObject>> {

    private final String LOG_TAG = FetchMovieListTask.class.getSimpleName();
    private final String API_KEY_STRING = "api_key";
    private final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private final String HTTP = "https";
    private final String URL = "api.themoviedb.org";
    private final String DATA ="3";
    private final String MOVIE = "movie";
    private final String SORT = "sort_by";

    private Context mContext;
    private ImageAdapter imageAdapter;

    public FetchMovieListTask(Context context, ImageAdapter imageAdapter) {
        this.mContext = context;
        this.imageAdapter = imageAdapter;
    }



    @Override
    protected ArrayList<ImageObject> doInBackground(String... params) {



        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String MovieJsonStr = null;

        int numDays = 7;

        try {


            Uri.Builder uri = new Uri.Builder();
            uri.scheme(HTTP)
                    .authority(URL)
                    .appendPath(DATA)
                    .appendPath(MOVIE)
                    .appendPath(params[0])
                    .appendQueryParameter(API_KEY_STRING,API_KEY);
            String baseURL = uri.build().toString();

            java.net.URL url = new URL(baseURL);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            MovieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return parseMovieJson(MovieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }
    private ArrayList<ImageObject> parseMovieJson(String movieDBJsonStr) throws JSONException {

        final Gson gson = new Gson();

        Results serverResult = gson.fromJson(movieDBJsonStr,Results.class);
        return serverResult.getmImgList();


    }


    @Override
    protected void onPostExecute(ArrayList<ImageObject> imageObjects) {
        imageAdapter.clear();

        if(imageObjects!=null){

            for(ImageObject a : imageObjects){
                imageAdapter.add(a);
            }
            imageAdapter.notifyDataSetChanged();
        }
    }
}
