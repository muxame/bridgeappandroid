package net.bridgeint.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import net.bridgeint.app.R;
import net.bridgeint.app.utils.FontCache;


/**
 * Created by Manndeep Vachhani on 11/24/2016.
 */

public class XTextView extends AppCompatTextView {

    public XTextView(Context context) {
        super(context);
    }

    public XTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFont);
            String fontName = a.getString(R.styleable.CustomFont_fontName);
            if (fontName != null) {
                setTypeface(FontCache.getTypeface(fontName, getContext()));
            }
            a.recycle();
        }
    }
}
