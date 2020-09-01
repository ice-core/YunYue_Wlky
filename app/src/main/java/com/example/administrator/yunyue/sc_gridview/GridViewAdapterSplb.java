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

public class GridViewAdapterSplb extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> myList;
    private Context mContext;


    private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示

    public GridViewAdapterSplb(Context mContext,
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.splb_item,
                null);
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.iv_splb = (ImageView) convertView.findViewById(R.id.iv_splb);
            viewHolder.tv_splb = (TextView) convertView.findViewById(R.id.tv_splb);
            viewHolder.tv_splb_q = (TextView) convertView.findViewById(R.id.tv_splb_q);
            viewHolder.tv_splb_id = (TextView) convertView.findViewById(R.id.tv_splb_id);

        }
        String url = myList.get(position).get("iv_splb").toString();
        Glide.with(mContext).load( Api.sUrl+ myList.get(position).get("iv_splb").toString())
                .asBitmap()
                .dontAnimate().into(viewHolder.iv_splb);
        //viewHolder.iv_sc_y.setBackgroundResource(myList.get(position).get("iv_sc_y").hashCode());
        viewHolder.tv_splb.setText(myList.get(position).get("tv_splb").toString());
        viewHolder.tv_splb_q.setText(myList.get(position).get("tv_splb_q").toString());
        viewHolder.tv_splb_id.setText(myList.get(position).get("tv_id").toString());
        return convertView;
    }

    static class ViewHolder {
        ImageView iv_splb;
        TextView tv_splb;
        TextView tv_splb_q;
        TextView tv_splb_id;
    }

}
