package com.example.administrator.yunyue.switchbutton;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.sq_activity.CyshActivity;
import com.example.administrator.yunyue.sq_activity.FbtzActivity;
import com.example.administrator.yunyue.sq_activity.GrtzActivity;
import com.example.administrator.yunyue.sq_activity.GywmActivity;


public class MorePopWindow extends PopupWindow {

    @SuppressLint("InflateParams")
    public MorePopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.popupwindow_add, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        TextView tv_fbdt = content.findViewById(R.id.tv_fbdt);
        TextView tv_cysh = content.findViewById(R.id.tv_cysh);
        TextView tv_wddt = content.findViewById(R.id.tv_wddt);
        TextView tv_gywm = content.findViewById(R.id.tv_gywm);
        tv_fbdt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(context, FbtzActivity.class));
                // intent.putExtra("createGroup", true);
                context.startActivity(intent);
                MorePopWindow.this.dismiss();
            }
        });
        tv_cysh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(context, CyshActivity.class));
                //  intent.putExtra("createGroup", true);
                context.startActivity(intent);
                MorePopWindow.this.dismiss();
            }
        });
        tv_wddt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(context, GrtzActivity.class));
                //  intent.putExtra("createGroup", true);
                GrtzActivity.sFriend_id = "";
                context.startActivity(intent);
                MorePopWindow.this.dismiss();
            }
        });
        tv_gywm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(context, GywmActivity.class));
                //  intent.putExtra("createGroup", true);
                context.startActivity(intent);
                MorePopWindow.this.dismiss();
            }
        });

    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, -10);
        } else {
            this.dismiss();
        }
    }
}
