package com.qualifies.app.uis.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GoodSpecGridView extends GridView {
    public GoodSpecGridView(Context context) {
        super(context);
    }

    public GoodSpecGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodSpecGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
