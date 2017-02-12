package com.udacity.popularmoviestage1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmoviestage1.R;
import com.udacity.popularmoviestage1.model.ImageObject;
import com.udacity.popularmoviestage1.model.ReveiewBean;

import java.util.ArrayList;

/**
 * Created by rj125e on 8/25/2016.
 */
public class ImageAdapter extends ArrayAdapter<ImageObject> {
    private Context mContext;
    private ArrayList<ImageObject> mImgObjList;
    private int listOfMovies;

    public ImageAdapter(Context context, int resource,ArrayList<ImageObject> mImgList){
        super(context,resource,mImgList);
        mContext = context;
        this.mImgObjList = mImgList;
    }

    public void clear() {
        mImgObjList.clear();
    }
    public void add(ImageObject imgObj) {
        mImgObjList.add(imgObj);
        notifyDataSetChanged();
    }
    public long getItemId(int position) {
        return 0;
    }

    public int getCount() {

        if (mImgObjList != null) {
            listOfMovies = mImgObjList.size();
        }
        return listOfMovies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView = convertView;
        ImageObject imgObj = null;
        ImageView imgMovie = null;
        imgObj = mImgObjList.get(position);
        if(convertView == null){
          //  gridView = new View(mContext);
            gridView = inflater.inflate(R.layout.grid_layout,null);

            ViewHolder viewHolder = new ViewHolder(gridView);
            gridView.setTag(viewHolder);
            imgMovie = viewHolder.imgMovie;
          /*  imgMovie = (ImageView) gridView.findViewById(R.id.imgMovie);
            imgMovie.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgMovie.setAdjustViewBounds(true);*/

        }else {

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            imgMovie = viewHolder.imgMovie;
        }
        imgMovie.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgMovie.setAdjustViewBounds(true);

        String baseurl = "http://image.tmdb.org/t/p/w500";
        Picasso.with(mContext).load(baseurl.concat(imgObj.getPoster_path())).into(imgMovie);
        return gridView;
    }
    public static class ViewHolder {
        public final ImageView imgMovie;

        public ViewHolder(View view) {
            imgMovie = (ImageView) view.findViewById(R.id.imgMovie);

        }
    }
}
