package com.example.administrator.yunyue.sc_gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.yunyue.R;

import java.util.ArrayList;
import java.util.HashMap;

public class GridViewAdaptersc extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> myList;
    private Context mContext;


    private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示

    public GridViewAdaptersc(Context mContext,
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.sc_y_lb_item,
                null);
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
         /*   viewHolder.iv_sc_y = (ImageView) convertView.findViewById(R.id.iv_sc_y);
            viewHolder.tv_sc_y = (TextView) convertView.findViewById(R.id.tv_sc_y);
            viewHolder.tv_sc_id = (TextView) convertView.findViewById(R.id.tv_sc_id);*/

        }
     /*   String url = myList.get(position).get("iv_sc_y").toString();
        Glide.with(mContext).load( Api.sUrl+mContext.getString(R.string.tuurl) + myList.get(position).get("iv_sc_y").toString()).into(viewHolder.iv_sc_y);
        //viewHolder.iv_sc_y.setBackgroundResource(myList.get(position).get("iv_sc_y").hashCode());
        viewHolder.tv_sc_y.setText(myList.get(position).get("tv_sc_y").toString());
        viewHolder.tv_sc_id.setText(myList.get(position).get("tv_sc_id").toString());*/
        return convertView;
    }

    static class ViewHolder {
       /* ImageView iv_sc_y;
        TextView tv_sc_y;
        TextView tv_sc_id;*/
    }

}
