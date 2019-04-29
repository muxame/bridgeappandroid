package net.bridgeint.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.YoutubeVideoActivity;
import net.bridgeint.app.models.BannerModelForAdapter;
import net.bridgeint.app.utils.Constants;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DeviceBee on 10/25/2017.
 */

public class UniversityImagesAdapter extends PagerAdapter {

    Activity mContext;
    LayoutInflater mLayoutInflater;
    List<BannerModelForAdapter> bannerModelForAdapters = new ArrayList<>();
    Boolean ischat_adapter= false;

    public UniversityImagesAdapter(Activity context, ArrayList<BannerModelForAdapter> bannerModelForAdapters,boolean isChat) {
        mContext = context;
        this.bannerModelForAdapters.addAll(bannerModelForAdapters);
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ischat_adapter = isChat;
    }

    @Override
    public int getCount() {
        return bannerModelForAdapters.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.uni_images, container, false);
        ZoomageView imageView = itemView.findViewById(R.id.ivUni);
        final RelativeLayout youtube_view = itemView.findViewById(R.id.youtube_view);

        if (position == 3) {
            youtube_view.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            youtube_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, YoutubeVideoActivity.class).putExtra("video", bannerModelForAdapters.get(position).title_url));
                }
            });

        } else {
            imageView.setVisibility(View.VISIBLE);
            youtube_view.setVisibility(View.GONE);
            if(mContext.getIntent().hasExtra("from")){
                if(ischat_adapter){

                    Picasso.with(mContext).load( bannerModelForAdapters.get(position).title_url).into(imageView);
                }

            }else {
                Picasso.with(mContext).load(Constants.IMAGE_URL + bannerModelForAdapters.get(position).title_url).into(imageView);
            }

        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
