package com.example.administrator.yunyue.sc_gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;


import java.util.ArrayList;
import java.util.HashMap;

public class GridViewAdaptershangcheng extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> myList;
    private Context mContext;


    private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示

    public GridViewAdaptershangcheng(Context mContext,
                                     ArrayList<HashMap<String, Object>> myList) {
        this.mContext = mContext;
        // this.names=names;
        this.myList = myList;

    }

    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int index = position;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_shangcheng_item,
                null);
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.iv_sc_item = (ImageView) convertView.findViewById(R.id.iv_sc_item);
            viewHolder.tv_sc_miaoshu = (TextView) convertView.findViewById(R.id.tv_sc_miaoshu);
            viewHolder.tv_sc_id = (TextView) convertView.findViewById(R.id.tv_sc_id);

        }
        Glide.with(mContext).load( Api.sUrl + myList.get(position).get("logo").toString())
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .into(viewHolder.iv_sc_item);
        viewHolder.tv_sc_miaoshu.setText(myList.get(position).get("name").toString());
        viewHolder.tv_sc_id.setText(myList.get(position).get("id").toString());

        return convertView;
    }

    static class ViewHolder {
        ImageView iv_sc_item;
        TextView tv_sc_miaoshu;
        TextView tv_sc_id;


    }

}
