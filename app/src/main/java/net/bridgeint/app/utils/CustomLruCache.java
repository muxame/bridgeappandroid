package net.bridgeint.app.utils;

import android.content.Context;

import com.squareup.picasso.LruCache;

/**
 * Created by DeviceBee on 8/29/2017.
 */

public class CustomLruCache extends LruCache {
    public CustomLruCache(Context context){
        super(context);
    }

    public CustomLruCache(int value){
        super(value);
    }
}
