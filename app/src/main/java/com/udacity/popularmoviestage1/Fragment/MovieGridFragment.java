package com.udacity.popularmoviestage1.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.popularmoviestage1.BuildConfig;
import com.udacity.popularmoviestage1.MovieDetailActivity;
import com.udacity.popularmoviestage1.R;
import com.udacity.popularmoviestage1.adapter.ImageAdapter;
import com.udacity.popularmoviestage1.model.ImageObject;
import com.udacity.popularmoviestage1.model.Results;
import com.udacity.popularmoviestage1.network.FetchFavouriteMovieListTask;
import com.udacity.popularmoviestage1.network.FetchMovieListTask;
import com.udacity.popularmoviestage1.network.FetchTrailerListTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rj125e on 8/25/2016.
 */
public class MovieGridFragment extends Fragment{

   View rootView;
   private GridView gridView= null;
   private ArrayList<ImageObject> mListImgObj = null;
   private final String LOG_TAG = MovieGridFragment.class.getSimpleName();
   ImageAdapter imageAdapter;

   /**
    * A callback interface that all activities containing this fragment must
    * implement. This mechanism allows activities to be notified of item
    * selections.
    */
   public interface Callback {
      /**
       * DetailFragmentCallback for when an item has been selected.
       */
      public void onItemSelected(ImageObject imgObj);
   }

   public MovieGridFragment(){
       // super();
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //setHasOptionsMenu(true);
   }


   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      rootView = inflater.inflate(R.layout.fragment_main,container,false);
      initView();
      return rootView;
   }

   public void initView(){

      mListImgObj = new ArrayList<ImageObject>();

      gridView = (GridView) rootView.findViewById(R.id.gridMovie);
      imageAdapter = new ImageAdapter(getActivity(),R.layout.fragment_main, mListImgObj);
     // imageAdapter = new ImageAdapter(getActivity(), mListImgObj);
      gridView.setAdapter(imageAdapter);
      gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         Toast.makeText(getActivity(), "Grid View Click", Toast.LENGTH_SHORT).show();
            ImageObject imgObj = mListImgObj.get(position);
            /*   Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                    .putExtra("image_details", imgObj);
            startActivity(intent);*/
            ((Callback) getActivity())
                    .onItemSelected(imgObj);

         }
      });
   }


   @Override
   public void onStart() {
      super.onStart();
      updateMovieGrid();
   }

   private void updateMovieGrid(){

      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
      String sortOrder = prefs.getString(getString(R.string.pref_popular_key),
              getString(R.string.pref_popular_default));
       Log.i(LOG_TAG,"Sort order is "+sortOrder);
       if(sortOrder.equalsIgnoreCase("favourite")){
           new FetchFavouriteMovieListTask(getActivity(),imageAdapter).execute();
       }else{

           new FetchMovieListTask(getActivity(),imageAdapter).execute(sortOrder);
       }

   }


}
