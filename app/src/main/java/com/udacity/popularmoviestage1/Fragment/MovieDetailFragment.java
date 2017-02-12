package com.udacity.popularmoviestage1.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmoviestage1.R;
import com.udacity.popularmoviestage1.adapter.ReveiewAdapter;
import com.udacity.popularmoviestage1.adapter.TrailerAdapter;
import com.udacity.popularmoviestage1.data.MovieContract;
import com.udacity.popularmoviestage1.model.ImageObject;
import com.udacity.popularmoviestage1.model.ReveiewBean;
import com.udacity.popularmoviestage1.model.VideoBean;
import com.udacity.popularmoviestage1.network.FetchReveiwListTask;
import com.udacity.popularmoviestage1.network.FetchTrailerListTask;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by rj125e on 8/27/2016.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener{

    private View view;
    private TextView mMovieTitle,mOverview,mVote,mReleaseDate;
    private ImageView mMovieImageThumnail;
    private TrailerAdapter mTrailerAdapter = null;
    private ReveiewAdapter mReveiwAdapter = null;
    private String movieId;
    private Button favBtn = null;
    private Button unFavBtn = null;
    private final String TAG = MovieDetailFragment.class.getSimpleName();
    private static Context mContext = null;
    private ImageObject imgObj = null;




    public MovieDetailFragment(){
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.movie_detail_fragment,container,false);
        initView();
        setupRecyclerViewForTrailerAndReveiew();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(movieId!=null && !movieId.equalsIgnoreCase("")){
            new FetchTrailerListTask(getActivity(),mTrailerAdapter).execute(movieId);
        new FetchReveiwListTask(getActivity(),mReveiwAdapter).execute(movieId);
        }
    }

    private void initView(){

        mContext = getActivity();
        Bundle arguments = getArguments();
        if (arguments != null) {
            imgObj = arguments.getParcelable("image_details");
            movieId = imgObj.getId();
        }else{
            view.setVisibility(View.INVISIBLE);
            return;
        }


       /* Intent intent = getActivity().getIntent();
        imgObj = intent.getExtras().getParcelable("image_details");
        movieId = imgObj.getId();*/

        mMovieTitle = (TextView) view.findViewById(R.id.movieTitle);
        mOverview = (TextView) view.findViewById(R.id.overview);
        mVote = (TextView) view.findViewById(R.id.vote);
        mReleaseDate = (TextView) view.findViewById(R.id.releaseDate);
        favBtn = (Button) view.findViewById(R.id.fav_btn);
        unFavBtn = (Button) view.findViewById(R.id.unfav_btn);
        favBtn.setOnClickListener(this);
        unFavBtn.setOnClickListener(this);
        if(imgObj !=null){
            if(isFavMovie()){
                favBtn.setVisibility(View.GONE);
                unFavBtn.setVisibility(View.VISIBLE);
            }else{
                favBtn.setVisibility(View.VISIBLE);
                unFavBtn.setVisibility(View.GONE);
            }

       /* String voteHeader = getResources().getString(R.string.average_rating);
        String releaseDateHeader = getResources().getString(R.string.release_date);*/

            mMovieTitle.setText(imgObj.getOriginal_title());
            mOverview.setText(imgObj.getOverview());
            mVote.setText(imgObj.getVote_average()+"/"+"10");
            mReleaseDate.setText(imgObj.getRelease_date());

            mMovieImageThumnail = (ImageView) view.findViewById(R.id.movieImageThumnail);
            //String baseurl = "http://image.tmdb.org/t/p/w500";
            String baseurl = "http://image.tmdb.org/t/p/w185";
            Picasso.with(getActivity()).load(baseurl.concat(imgObj.getPoster_path())).into(mMovieImageThumnail);
        }

    }

    private void setupRecyclerViewForTrailerAndReveiew() {

        RecyclerView rvTrailers = (RecyclerView) view.findViewById(R.id.trailers_recyclerview);
        rvTrailers.setLayoutManager(new LinearLayoutManager(rvTrailers.getContext()));
        mTrailerAdapter = new TrailerAdapter(getActivity(),new ArrayList<VideoBean>());
        rvTrailers.setAdapter(mTrailerAdapter);

        RecyclerView rvReveiws = (RecyclerView) view.findViewById(R.id.reveiw_recyclerview);
        mReveiwAdapter = new ReveiewAdapter(getActivity(),new ArrayList<ReveiewBean>());
        rvReveiws.setLayoutManager(new LinearLayoutManager(rvReveiws.getContext()));
        rvReveiws.setAdapter(mReveiwAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fav_btn:
                        insertFavDataIntoDB();
                break;
            case R.id.unfav_btn:
                    deleteFavDataFromDB();
                break;


        }
    }

    private void insertFavDataIntoDB(){

        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,imgObj.getId());
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,imgObj.getOverview());
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,imgObj.getOriginal_title());
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,imgObj.getPoster_path());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,imgObj.getVote_average());
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, imgObj.getRelease_date());
        Uri insertUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,movieValues);
        Toast.makeText(mContext, "Added Favourite Successfully", Toast.LENGTH_SHORT).show();
        favBtn.setVisibility(View.GONE);
        unFavBtn.setVisibility(View.VISIBLE);
    }

    private void deleteFavDataFromDB(){
        int deletion = mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{imgObj.getId()});
        Toast.makeText(mContext,"Unmarked Favourite Successfully",Toast.LENGTH_SHORT).show();
        favBtn.setVisibility(View.VISIBLE);
        unFavBtn.setVisibility(View.GONE);
    }

    private boolean isFavMovie(){
        boolean isFavMovie = false;
        Cursor favMovcursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ", new String[]{imgObj.getId()},null);
        if(favMovcursor.getCount() == 0)
            isFavMovie = false;
        else
            isFavMovie = true;

        favMovcursor.close();

        return isFavMovie;
    }
}
