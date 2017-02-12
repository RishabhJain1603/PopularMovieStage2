package com.udacity.popularmoviestage1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.popularmoviestage1.R;
import com.udacity.popularmoviestage1.model.ReveiewBean;
import com.udacity.popularmoviestage1.model.VideoBean;

import java.util.ArrayList;

/**
 * Created by rj125e on 1/22/2017.
 */

public class ReveiewAdapter
        extends RecyclerView.Adapter<ReveiewAdapter.ViewHolder> {


    private ArrayList<ReveiewBean> reveiwList;

    public  class ViewHolder extends RecyclerView.ViewHolder {


        public final View mView;
        public final TextView mReveiwTextView;
        public final TextView mContentTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mReveiwTextView = (TextView) view.findViewById(R.id.review_author);
            mContentTextView = (TextView) view.findViewById(R.id.review_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }



    public ReveiewBean getValueAt(int position) {
        return reveiwList.get(position);
    }

    public ReveiewAdapter(Context context, ArrayList<ReveiewBean> items) {

        reveiwList = items;
    }

    public void clear() {
        reveiwList.clear();
    }
    public void add(ReveiewBean reveiw) {
        reveiwList.add(reveiw);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ReveiewBean reveiwObj = reveiwList.get(position);
        holder.mContentTextView.setText(reveiwObj.getContent());
        holder.mReveiwTextView.setText(reveiwObj.getAuthor());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return reveiwList.size();
    }
}