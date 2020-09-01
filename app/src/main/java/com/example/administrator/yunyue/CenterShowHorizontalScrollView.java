package com.example.administrator.yunyue;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by chenglin on 2017-6-23.
 */

public class CenterShowHorizontalScrollView extends HorizontalScrollView {
    private LinearLayout mShowLinear;

    public CenterShowHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mShowLinear = new LinearLayout(context);
        mShowLinear.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mShowLinear.setGravity(Gravity.CENTER_VERTICAL);
        this.addView(mShowLinear, params);
    }

    public void onClicked(View v) {
        if (v.getTag(R.id.item_position) != null) {
            int position = (Integer) v.getTag(R.id.item_position);
            View itemView = mShowLinear.getChildAt(position);
            int itemWidth = itemView.getWidth();
            int scrollViewWidth = this.getWidth();

            smoothScrollTo(itemView.getLeft() - (scrollViewWidth / 2 - itemWidth / 2), 0);
        }
    }

    public LinearLayout getLinear() {
        return mShowLinear;
    }

    public void addItemView(View itemView, int position) {
        itemView.setTag(R.id.item_position, position);
        mShowLinear.addView(itemView);
    }


}