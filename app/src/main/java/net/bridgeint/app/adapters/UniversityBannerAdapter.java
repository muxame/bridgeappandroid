package net.bridgeint.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.YoutubeVideoActivity;
import net.bridgeint.app.models.BannerModelForAdapter;
import net.bridgeint.app.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DeviceBee on 10/25/2017.
 */

public class UniversityBannerAdapter extends PagerAdapter {

    Activity mContext;
    LayoutInflater mLayoutInflater;
    List<BannerModelForAdapter> bannerModelForAdapters = new ArrayList<>();
    private static MyClickListener myClickListener;

    public interface MyClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        UniversityBannerAdapter.myClickListener = myClickListener;
    }
    public UniversityBannerAdapter(Activity context, ArrayList<BannerModelForAdapter> bannerModelForAdapters) {
        mContext = context;
        this.bannerModelForAdapters.addAll(bannerModelForAdapters);
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View itemView = mLayoutInflater.inflate(R.layout.uni_banner, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        final RelativeLayout youtube_view = itemView.findViewById(R.id.youtube_view);
        if(bannerModelForAdapters.get(position).isVideo)
        {
            imageView.setVisibility(View.GONE);
            youtube_view.setVisibility(View.VISIBLE);
            youtube_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   mContext.startActivity(new Intent(mContext, YoutubeVideoActivity.class).putExtra("video",bannerModelForAdapters.get(position).title_url));
                }
            });
        }
        else
        {
            imageView.setVisibility(View.VISIBLE);
            youtube_view.setVisibility(View.GONE);
            Picasso.with(mContext).load(Constants.IMAGE_URL+bannerModelForAdapters.get(position).title_url).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(position);
                }
            });
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
