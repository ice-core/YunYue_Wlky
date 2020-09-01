package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.yunyue.Model;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;

import com.example.administrator.yunyue.adapter.GridTextAdapter;
import com.example.administrator.yunyue.data.SpflGridData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpflActivity extends AppCompatActivity {
    private ExpandableListView expandableGridView;

    ExpandableGridAdapter adapter;

    private List<Map<String, Object>> list;
    private String[][] child_text_array;

    private int sign = -1;// 控制列表的展开
    /**
     * 返回
     */
    private ImageView iv_spfl_back;

    public static String sFl_Id = "";

    /**
     * 全部商品
     */
    private LinearLayout ll_spfl_qbsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_spfl);
        iv_spfl_back = findViewById(R.id.iv_spfl_back);
        iv_spfl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_spfl_qbsp = findViewById(R.id.ll_spfl_qbsp);
        ll_spfl_qbsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpflActivity.sFl_Id = "全部";
                finish();
            }
        });
        init();
        initModle();
        setListener();

    }

    private void init() {
        expandableGridView = (ExpandableListView) findViewById(R.id.list);

        child_text_array = Model.EXPANDABLE_MOREGRIDVIEW_TXT;
    }

    private void setListener() {
        expandableGridView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (sign == -1) {
                    // 展开被选的group
                    expandableGridView.expandGroup(groupPosition);
                    // 设置被选中的group置于顶端
                    //  expandableGridView.setSelectedGroup(groupPosition);
                    sign = groupPosition;
                } else if (sign == groupPosition) {
                    expandableGridView.collapseGroup(sign);
                    sign = -1;
                } else {
                    expandableGridView.collapseGroup(sign);
                    // 展开被选的group
                    expandableGridView.expandGroup(groupPosition);
                    // 设置被选中的group置于顶端
                    //	expandableGridView.setSelectedGroup(groupPosition);
                    sign = groupPosition;
                }
                return true;
            }
        });
    }

    private void initModle() {
        list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < Model.EXPANDABLE_GRIDVIEW_TXT.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("txt", Model.EXPANDABLE_GRIDVIEW_TXT[i]);
            list.add(map);
        }

        List<SpflGridData> list_cat_id = new ArrayList<>();
        for (int i = 0; i < Model.EXPANDABLE_GRIDVIEW_TXT.length; i++) {
            SpflGridData model = new SpflGridData();

            model.sName = Model.EXPANDABLE_GRIDVIEW_TXT[i];
            for (int a = 0; a < Model.EXPANDABLE_MOREGRIDVIEW_TXT.length; a++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", Model.EXPANDABLE_MOREGRIDVIEW_TXT[i].toString());
                model.mylist.add(map);
            }
            list_cat_id.add(model);
        }
        adapter = new ExpandableGridAdapter(this, list_cat_id);
        expandableGridView.setAdapter(adapter);
    }

    public class ExpandableGridAdapter extends BaseExpandableListAdapter implements AdapterView.OnItemClickListener {

        private Context context;
        private com.example.administrator.yunyue.adapter.MyGridView gridview;
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
            gridview = (com.example.administrator.yunyue.adapter.MyGridView) convertView.findViewById(R.id.gridview);

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
            finish();
            Toast.makeText(context, "当前选中的是:" + child_array.get(position).get("id"),
                    Toast.LENGTH_SHORT).show();

        }
    }
}
