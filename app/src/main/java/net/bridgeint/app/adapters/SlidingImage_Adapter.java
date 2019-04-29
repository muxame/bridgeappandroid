package net.bridgeint.app.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.bridgeint.app.R;

import java.util.ArrayList;
 
 
public class SlidingImage_Adapter extends PagerAdapter {
 
 
    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
 
 
    public SlidingImage_Adapter(Context context,ArrayList<Integer> IMAGES) {
        this.context = context;
this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
 
    @Override
    public int getCount() {
        return IMAGES.size();
    }
 
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_tutorial_screen, view, false);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);
 
 
imageView.setImageResource(IMAGES.get(position));
 
        view.addView(imageLayout, 0);
 
        return imageLayout;
    }

    protected void onItemClick(int position) {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
 
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
 
    @Override
    public Parcelable saveState() {
        return null;
    }
 
 
}