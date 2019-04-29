package net.bridgeint.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.fragments.MediaDetailsFragment;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.Utility;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ufraj on 10/21/2016.
 */
public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.ViewHolder> {
    private List<UniversityModel> listItems;
    private List<UniversityModel> copyList = new ArrayList<>();
    private Activity ctx;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_search_update, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public Search_Adapter(Activity context, List<UniversityModel> listItems) {
        this.listItems = listItems;
        this.copyList.addAll(listItems);
        this.ctx = context;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final UniversityModel item = listItems.get(position);

            final ImageView ivUni = holder.ivUni;
            TextView tvUniName = holder.tvUniName;
            TextView tvAdmissionOpen = holder.tvAdmissionOpen;
            TextView tvAddress = holder.tvAddress;
            tvUniName.setText(item.getTitle());

            tvAddress.setText(item.getAddress());
            ImageLoader.getInstance().loadImage(Constants.IMAGE_URL+item.getImage(), new ImageSize(Constants.HIGH_WIDTH, Constants.HIGH_HEIGHT), new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.place_holder)
                    .showImageForEmptyUri(R.drawable.place_holder)
                    .showImageOnFail(R.drawable.place_holder)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ivUni.setImageBitmap(loadedImage);
                }
            });
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.isNetConnected(ctx)) {
                        ApplyModel.universityModel = listItems.get(position);
                        Log.e("Uni Model search", new Gson().toJson(ApplyModel.universityModel));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.UNIVERSITY, listItems.get(position));
                        MediaDetailsFragment myFragment = new MediaDetailsFragment();
                        myFragment.setArguments(bundle);
                        DashboardUpdateActivity myActivity = (DashboardUpdateActivity) ctx;
                        myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                    } else {
                        Utility.showToast(ctx, "Net Connection Lost");
                    }
                }
            });
            if(item.isAdmissionOpened())
            {
                tvAdmissionOpen.setText("Admission Opened");
            }
            else
            {
                tvAdmissionOpen.setText("Admission Closed");
            }

        } catch (Exception e) {
            Log.e("adapter ", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivUni;
        public TextView tvAdmissionOpen;
        public TextView tvUniName;
        public TextView tvAddress;
        public Context ctx;
        View view;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            view = itemView;
            ivUni = itemView.findViewById(R.id.ivUni);
            tvAdmissionOpen = itemView.findViewById(R.id.tvAdmissionOpen);
            tvUniName = itemView.findViewById(R.id.tvUniName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ctx = context;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void filter(String word)
    {
        word = word.toLowerCase();
        listItems.clear();
        if (word.length() == 0) {
            listItems.addAll(copyList);
        } else {
            for (UniversityModel universityModel : copyList) {
                if (universityModel.getTitle().toLowerCase().contains(word)) {
                    listItems.add(universityModel);
                }
            }
        }
        notifyDataSetChanged();
    }
}
