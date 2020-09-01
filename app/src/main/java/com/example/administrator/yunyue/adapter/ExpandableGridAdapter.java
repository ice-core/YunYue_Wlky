package com.example.administrator.yunyue.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.data.SpflGridData;
import com.example.administrator.yunyue.sc_activity.SpflActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExpandableGridAdapter extends BaseExpandableListAdapter implements OnItemClickListener {


    private Context context;
    private MyGridView gridview;
    List<SpflGridData> list;
    ArrayList<HashMap<String, String>> child_array;

    public ExpandableGridAdapter(Context context,
                                 List<SpflGridData> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 获取一级标签总数
     */
    @Override
    public int getGroupCount() {
        return list.size();
    }

    /**
     * 获取一级标签下二级标签的总数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        // 这里返回1是为了让ExpandableListView只显示一个ChildView，否则在展开
        // ExpandableListView时会显示和ChildCount数量相同的GridView
        return 1;
    }

    /**
     * 获取一级标签内容
     */
    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition).sName;
    }

    /**
     * 获取一级标签下二级标签的内容
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).mylist.get(childPosition);
    }

    /**
     * 获取一级标签的ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取二级标签的ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 指定位置相应的组视图
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 对一级标签进行设置
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        convertView = (LinearLayout) LinearLayout.inflate(context,
                R.layout.item_gridview_group_layout, null);

        TextView group_title = (TextView) convertView
                .findViewById(R.id.group_title);
        ImageView iv_group = convertView.findViewById(R.id.iv_group);
        if (isExpanded) {
            iv_group.setImageResource(R.drawable.btn_next_arrow_copy);
        } else {

            iv_group.setImageResource(R.drawable.btn_next_arrow_copy_2);
        }
        group_title.setText(list.get(groupPosition).sName.toString());
        return convertView;
    }

    /**
     * 对一级标签下的二级标签进行设置
     */
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = (RelativeLayout) RelativeLayout.inflate(context,
                R.layout.item_grid_child_layout, null);
        gridview = (MyGridView) convertView.findViewById(R.id.gridview);

        int size = list.get(groupPosition).mylist.size();
        child_array = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < size; i++) {
            child_array.add(list.get(groupPosition).mylist.get(i));
        }
        gridview.setAdapter(new GridTextAdapter(context, child_array));
        gridview.setOnItemClickListener(this);
        return convertView;
    }

    /**
     * 当选择子节点的时候，调用该方法
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        SpflActivity.sFl_Id = child_array.get(position).get("id");
        Toast.makeText(context, "当前选中的是:" + child_array.get(position).get("id"),
                Toast.LENGTH_SHORT).show();

    }
}