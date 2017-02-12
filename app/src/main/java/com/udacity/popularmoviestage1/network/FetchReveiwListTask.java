package com.udacity.popularmoviestage1.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.udacity.popularmoviestage1.BuildConfig;
import com.udacity.popularmoviestage1.adapter.ReveiewAdapter;
/*import com.udacity.popularmoviestage1.adapter.TrailerAdapter;*/
import com.udacity.popularmoviestage1.model.ImageObject;
import com.udacity.popularmoviestage1.model.Results;
import com.udacity.popularmoviestage1.model.ReveiewBean;
import com.udacity.popularmoviestage1.model.ReveiwResults;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rj125e on 1/21/2017.
 */
public class FetchReveiwListTask extends AsyncTask<String, Void, ArrayList<ReveiewBean>> {

    private final String LOG_TAG = FetchReveiwListTask.class.getSimpleName();
    private final String API_KEY_STRING = "api_key";
    private final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private final String HTTP = "https";
    private final String URL = "api.themoviedb.org";
    private final String DATA ="3";
    private final String MOVIE = "movie";
    private final String SORT = "sort_by";
    private final String REVEIWS = "reviews";

    private Context mContext;
    private ReveiewAdapter mReviewAdapter;

    public FetchReveiwListTask(Context context, ReveiewAdapter trailerAdapter) {
        this.mContext = context;
        this.mReviewAdapter = trailerAdapter;
    }



    @Override
    protected ArrayList<ReveiewBean> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String reveiewJsonStr = null;

        try {


            Uri.Builder uri = new Uri.Builder();
            uri.scheme(HTTP)
                    .authority(URL)
                    .appendPath(DATA)
                    .appendPath(MOVIE)
                    .appendPath(params[0])
                    .appendPath(REVEIWS)
                    .appendQueryParameter(API_KEY_STRING, API_KEY);
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
            reveiewJsonStr = buffer.toString();
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
            return parseReveiewMovieJson(reveiewJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }
    private ArrayList<ReveiewBean> parseReveiewMovieJson(String reveiewDBJsonStr) throws JSONException {

        final Gson gson = new Gson();

        ReveiwResults reveiwResult = gson.fromJson(reveiewDBJsonStr,ReveiwResults.class);
        return reveiwResult.getResults();


    }


    @Override
    protected void onPostExecute(ArrayList<ReveiewBean> reveiewbjects) {
        mReviewAdapter.clear();

        if(reveiewbjects!=null){

            for(ReveiewBean reveiewObj : reveiewbjects){
                mReviewAdapter.add(reveiewObj);
            }
            mReviewAdapter.notifyDataSetChanged();
        }
    }
}

