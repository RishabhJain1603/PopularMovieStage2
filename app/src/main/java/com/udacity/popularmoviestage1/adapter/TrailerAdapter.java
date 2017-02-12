package com.udacity.popularmoviestage1.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.popularmoviestage1.R;
import com.udacity.popularmoviestage1.model.VideoBean;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by rj125e on 1/22/2017.
 */

public class TrailerAdapter
        extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {


    private ArrayList<VideoBean> trailerList;
    private Context mContext  = null;

    public  class ViewHolder extends RecyclerView.ViewHolder {

        public String mBoundString;
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.play_icon);
            mTextView = (TextView) view.findViewById(R.id.trailer_list_item_url);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }



    public VideoBean getValueAt(int position) {
        return trailerList.get(position);
    }

    public TrailerAdapter(Context context, ArrayList<VideoBean> items) {

        mContext = context;
        trailerList = items;
    }

    public void clear() {
        trailerList.clear();
    }
    public void add(VideoBean trailer) {
        trailerList.add(trailer);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final VideoBean videoObj = trailerList.get(position);
        holder.mTextView.setText(videoObj.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = videoObj.getKey();
                watchYoutubeVideo(key);
               /* Context context = v.getContext();
                Intent intent = new Intent(context, CheeseDetailActivity.class);
                intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);

                context.startActivity(intent);*/
            }
        });

       /* Glide.with(holder.mImageView.getContext())
                .load(Cheeses.getRandomCheeseDrawable())
                .fitCenter()
                .into(holder.mImageView);*/
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    private   void watchYoutubeVideo(String key){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            mContext.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            mContext.startActivity(webIntent);
        }
    }
}
