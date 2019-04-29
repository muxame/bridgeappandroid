package net.bridgeint.app.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Class is to manage caching of fonts for faster rendering and lesser memory consumption
 * Created by Manndeep Vachhani on 12/10/16.
 */
public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    /**
     * Get recycled Typeface
     *
     * @param fontName font to get
     * @param context  context
     * @return recycled Typeface
     */
    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = fontCache.get(fontName);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontName, typeface);
        }

        return typeface;
    }
}