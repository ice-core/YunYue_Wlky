package com.example.administrator.yunyue.sc_gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.yunyue.R;

import java.util.ArrayList;
import java.util.HashMap;

public class GridViewAdapterchanpin extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> myList;
    private Context mContext;
    private int location;


    private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示

    public GridViewAdapterchanpin(Context mContext,
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.chanpin_item,
                null);
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.tv_chanpin_itemid = (TextView) convertView.findViewById(R.id.tv_chanpin_itemid);
            viewHolder.tv_chanpin_itemname = (TextView) convertView.findViewById(R.id.tv_chanpin_itemname);
            viewHolder.ll_chanpin_bg = (LinearLayout) convertView.findViewById(R.id.ll_chanpin_bg);
        }
        if (location == position) {
            viewHolder.ll_chanpin_bg.setBackgroundResource(R.drawable.normal_bg1);
            viewHolder.tv_chanpin_itemname.setTextColor(viewHolder.tv_chanpin_itemname.getResources().getColor(R.color.theme));
        } else {
            viewHolder.ll_chanpin_bg.setBackgroundResource(R.drawable.normal_bg);
            viewHolder.tv_chanpin_itemname.setTextColor(viewHolder.tv_chanpin_itemname.getResources().getColor(R.color.hui));
        }
/*
        if (position == 0) {
            viewHolder.ll_chanpin_bg.setBackgroundResource(R.drawable.normal_bg1);
            viewHolder.tv_chanpin_itemname.setTextColor(viewHolder.tv_chanpin_itemname.getResources().getColor(R.color.lan));
        }*/
        viewHolder.tv_chanpin_itemid.setText(myList.get(position).get("ItemItemId").toString());
        viewHolder.tv_chanpin_itemname.setText(myList.get(position).get("ItemName").toString());

        return convertView;
    }

    public void setSelection(int selection) {
        this.location = selection;
    }

    static class ViewHolder {
        TextView tv_chanpin_itemid;
        TextView tv_chanpin_itemname;
        LinearLayout ll_chanpin_bg;
    }

   /* public void setSeclection(int position) {
        location = position;
    }*/

}
