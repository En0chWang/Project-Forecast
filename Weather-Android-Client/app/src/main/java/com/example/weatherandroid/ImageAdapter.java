package com.example.weatherandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


    private List<ImageItem> mImageList;
    private Context mContext;


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cityImage;
        public ViewHolder(View view) {
            super(view);
            cityImage = (ImageView) view.findViewById(R.id.img_view_id);
        }
    }

    public ImageAdapter(List<ImageItem> imageList) {
        this.mImageList = imageList;

    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        ImageAdapter.ViewHolder holder = new ImageAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        String url = mImageList.get(position).getUrl();

        // set with holder
        Glide.with(mContext).asBitmap().load(url).placeholder(R.drawable.progress_bar).into(holder.cityImage);

    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }
}
