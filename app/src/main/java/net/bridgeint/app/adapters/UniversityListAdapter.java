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
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.fragments.MediaDetailsFragment;
import net.bridgeint.app.fragments.UniversityDetailsUpdateFragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ufraj on 10/21/2016.
 */
public class UniversityListAdapter extends RecyclerView.Adapter<UniversityListAdapter.ViewHolder> {

    private List<UniversityModel> listItems;
    private List<UniversityModel> copyList = new ArrayList<>();
    private Activity ctx;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_univerity_update, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public UniversityListAdapter(Activity context, List<UniversityModel> listItems) {
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
            holder.tvDescription.setText(item.getInformation());


            tvAddress.setText(item.getAddress());
            ImageLoader.getInstance().loadImage(Constants.IMAGE_URL + item.getIcon(), new ImageSize(Constants.HIGH_WIDTH, Constants.HIGH_HEIGHT), new DisplayImageOptions.Builder()
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
            holder.tv_media_gallary.setOnClickListener(new View.OnClickListener() {
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRootClick(position,item);
                    //                    if (Utility.isNetConnected(ctx)) {
//                        ApplyModel.universityModel = listItems.get(position);
//                        Log.e("Uni Model search", new Gson().toJson(ApplyModel.universityModel));
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(Constants.UNIVERSITY, listItems.get(position));
//                        UniversityDetailsUpdateFragment myFragment = new UniversityDetailsUpdateFragment();
//                        myFragment.setArguments(bundle);
//                        DashboardUpdateActivity myActivity = (DashboardUpdateActivity) ctx;
//                        myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
//                    } else {
//                        Utility.showToast(ctx, "Net Connection Lost");
//                    }
                }
            });
            holder.tvApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.isNetConnected(ctx)) {
                        ApplyModel.universityModel = listItems.get(position);
                        Log.e("Uni Model search", new Gson().toJson(ApplyModel.universityModel));
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.UNIVERSITY, listItems.get(position));
                        UniversityDetailsUpdateFragment myFragment = new UniversityDetailsUpdateFragment();
                        myFragment.setArguments(bundle);
                        DashboardUpdateActivity myActivity = (DashboardUpdateActivity) ctx;
                        myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                    } else {
                        Utility.showToast(ctx, "Net Connection Lost");
                    }
                }
            });
            if (item.isAdmissionOpened()) {
                tvAdmissionOpen.setText("Admission Opened");
            } else {
                tvAdmissionOpen.setText("Admission Closed");
            }

        } catch (Exception e) {
            Log.e("adapter ", e.toString());
            e.printStackTrace();
        }
    }

    protected void onRootClick(int position, UniversityModel item) {

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivUni)
        ImageView ivUni;
        @BindView(R.id.tvAdmissionOpen)
        TextView tvAdmissionOpen;
        @BindView(R.id.tvUniName)
        TextView tvUniName;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tv_media_gallary)
        TextView tv_media_gallary;
        @BindView(R.id.line)
        View line;
        @BindView(R.id.root)
        RelativeLayout root;
        public Context ctx;
        View view;

        @BindView(R.id.tv_apply)
        TextView tvApply;
        public ViewHolder(Context context, View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
            ctx = context;
        }

        @Override
        public void onClick(View v) {
        }
    }

    public void filter(String word) {
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
