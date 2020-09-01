package com.example.administrator.yunyue.fjsc_activity;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.yunyue.R;


import java.util.List;

/**
 * Created by Raul_lsj on 2018/3/28.
 */

public class ScrollRightAdapter_fjsc extends BaseSectionQuickAdapter<ScrollBean_fjsc, BaseViewHolder> {

    public ScrollRightAdapter_fjsc(int layoutResId, int sectionHeadResId, List<ScrollBean_fjsc> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ScrollBean_fjsc item) {
        helper.setText(R.id.right_title, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScrollBean_fjsc item) {
        ScrollBean_fjsc.ScrollItemBean t = item.t;
        // helper.setText(R.id.right_text, t.getText());
        helper.setText(R.id.right_text, t.getText()).addOnClickListener(R.id.ll_you).addOnClickListener(R.id.right_add)
                .addOnClickListener(R.id.right_guge);
        String buy_count = "月销" + t.getBuy_count();
        helper.setText(R.id.right_buy_count, buy_count);
        helper.setText(R.id.right_price, "￥" + t.getPrice());
        if (t.getMylistGuiGe().size() > 1) {
            helper.setVisible(R.id.right_guge, true);
            helper.setVisible(R.id.right_add, false);
        } else {
            helper.setVisible(R.id.right_add, true);
            helper.setVisible(R.id.right_guge, false);
        }

        Glide.with(mContext).
                load(t.getImge())
                .into((ImageView) helper.getView(R.id.right_image));//文章图片

    }


}

