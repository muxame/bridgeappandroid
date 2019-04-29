package net.bridgeint.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.DashboardUpdateActivity;
import net.bridgeint.app.fragments.MediaDetailsFragment;
import net.bridgeint.app.models.UniversityModel;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.Utility;

import java.util.List;


/**
 * Created by ufraj on 10/21/2016.
 */
public class University_Adapter extends RecyclerView.Adapter<University_Adapter.ViewHolder> {
    private List<UniversityModel> listItems;
    private Activity ctx;
    private int type;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_dashboard_uni, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public University_Adapter(Activity context, List<UniversityModel> listItems) {
        this.listItems = listItems;
        ctx = context;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final UniversityModel item = listItems.get(position);

            final ImageView ivUni = holder.ivUni;
            TextView tvUni = holder.tvUni;
            TextView tvAddress = holder.tvAddress;
            tvUni.setText(item.getTitle());
            tvAddress.setText(item.getAddress());
            Picasso.with(ctx).load(Constants.IMAGE_URL + item.getImage()).placeholder(R.drawable.place_holder).resize(Constants.MIDIEUM_WIDTH_150, Constants.MIDIEUM_HEIGHT_150).centerCrop().into(ivUni);
            holder.tv_media_gallary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Utility.isNetConnected(ctx)) {
                        ApplyModel.clearAll();
//                        Intent intent = new Intent(ctx, UniversityActivity.class);
//                        intent.putExtra(Constants.UNIVERSITY,listItems.get(position));
//                        ctx.startActivityForResult(intent,Constants.MOVE_TO_APPLY);
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
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.isNetConnected(ctx)) {
                        ApplyModel.clearAll();
//                        Intent intent = new Intent(ctx, UniversityActivity.class);
//                        intent.putExtra(Constants.UNIVERSITY,listItems.get(position));
//                        ctx.startActivityForResult(intent,Constants.MOVE_TO_APPLY);
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
        } catch (Exception e) {
            Log.e("adapter ", e.toString());
            e.printStackTrace();
        }
    }

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivUni;
        public TextView tvUni;
        public TextView tvAddress;
        public TextView tv_media_gallary;
        public Context ctx;
        public View view;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            super(itemView);
            view = itemView;
            ivUni = itemView.findViewById(R.id.ivUni);
            tvUni = itemView.findViewById(R.id.tvUni);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tv_media_gallary = itemView.findViewById(R.id.tv_media_gallary);
            ctx = context;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void UpdateList(List<UniversityModel> listItems) {
        this.listItems.clear();
        this.listItems.addAll(listItems);
        notifyDataSetChanged();
    }
}
