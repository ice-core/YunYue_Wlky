package com.example.administrator.yunyue.sc_fenlei;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.yunyue.R;

import java.util.List;

/**
 * Created by Raul_lsj on 2018/3/28.
 */

public class ScrollRightAdapter extends BaseSectionQuickAdapter<ScrollBean, BaseViewHolder> {
    //  private String url = "";

    public ScrollRightAdapter(int layoutResId, int sectionHeadResId, List<ScrollBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, ScrollBean item) {
        helper.setText(R.id.right_title, item.header);

    }

    @Override
    protected void convert(BaseViewHolder helper, ScrollBean item) {
        ScrollBean.ScrollItemBean t = item.t;
        helper.setText(R.id.right_text, t.getText());
        helper.setText(R.id.right_text, t.getText()).addOnClickListener(R.id.ll_you);

        // helper.setImageBitmap(R.id.right_image, getURLimage(t.getImge()));
        Glide.with(mContext).
                load(t.getImge())
                .into((ImageView) helper.getView(R.id.right_image));//文章图片
    }

}
