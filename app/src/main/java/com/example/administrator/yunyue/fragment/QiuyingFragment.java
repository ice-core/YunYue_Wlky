package com.example.administrator.yunyue.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.bean.JsonBean;
import com.example.administrator.yunyue.yjdt_activity.BjzlActivity;
import com.example.administrator.yunyue.yjdt_activity.DongtaiFragment;
import com.example.administrator.yunyue.yjdt_activity.GetJsonDataUtil;
import com.example.administrator.yunyue.yjdt_activity.QiuyingItemFragment;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QiuyingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QiuyingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QiuyingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    public static int iPositionId = 0;
    public static int iPosition = 0;
    private String[] tab = {"通讯录", "找供方", "找需方", "动态"};
    /**
     * 附件商城atab
     */
    private GridView mgv_qiuying;
    private MyAdapter myAdapter;
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    /**
     * 编辑资料
     */
    private TextView tv_qiuying_bjzl;
    /**
     * 地址
     */
    private LinearLayout ll_qiuying_dz;
    private TextView tv_qiuying_dz;
    /**
     * 投放地址
     */
    public static String address = "上海市";

    public QiuyingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QiuyingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QiuyingFragment newInstance(String param1, String param2) {
        QiuyingFragment fragment = new QiuyingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qiuying, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        mgv_qiuying = view.findViewById(R.id.mgv_qiuying);
        tv_qiuying_bjzl = view.findViewById(R.id.tv_qiuying_bjzl);
        tv_qiuying_bjzl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BjzlActivity.class);
                startActivity(intent);
            }
        });
        ll_qiuying_dz = view.findViewById(R.id.ll_qiuying_dz);
        tv_qiuying_dz = view.findViewById(R.id.tv_qiuying_dz);
        setGridView();
        initFragment();
        initJsonData();
        ll_qiuying_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerView();
            }
        });

    }

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                /**
                 * 投放地址
                 */
                String sRegion = opt1tx;
                address = opt1tx;
                if (!opt1tx.equals("不限")) {
                    if (!opt2tx.equals("不限")) {
                        sRegion = opt2tx;
                        address = address + "-" + opt2tx;
                        if (!opt3tx.equals("不限")) {
                            sRegion = opt3tx;
                            address = address + "-" + opt3tx;
                        }
                    }
                }
                tv_qiuying_dz.setText(sRegion);
            }
        }).setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(getActivity(), "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        //   mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //   mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        int count = tab.length;
        for (int i = 0; i < count - 1; i++) {
            QiuyingItemFragment qiuyingItemFragment = new QiuyingItemFragment();
            fragments.add(qiuyingItemFragment);
        }
        DongtaiFragment dongtaiFragment = new DongtaiFragment();
        fragments.add(dongtaiFragment);
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.addOnPageChangeListener(pageListener);
    }


    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            selectTab(position);

        }
    };

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        iPositionId = tab_postion;
        iPosition = tab_postion;
        myAdapter.notifyDataSetChanged();

    }

    private void setGridView() {
        myAdapter = new MyAdapter(getActivity());
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < tab.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemName", tab[i]);
            map.put("ItemId", String.valueOf(i));
            mylist.add(map);
        }
        myAdapter.arrlist = mylist;
        mgv_qiuying.setAdapter(myAdapter);
        /*sv_shouye.smoothScrollTo(0, 20);
        sv_shouye.setFocusable(true);*/
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            new ArrayList<HashMap<String, String>>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrlist.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }


        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.zx_fl_item, null);
            }
            LinearLayout ll_fl_item = view.findViewById(R.id.ll_fl_item);
            TextView zx_fl_item_name = view.findViewById(R.id.zx_fl_item_name);
            TextView zx_fl_item = view.findViewById(R.id.zx_fl_item);
            zx_fl_item_name.setText(arrlist.get(position).get("ItemName"));
            if (iPositionId == Integer.valueOf(arrlist.get(position).get("ItemId"))) {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.theme));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.theme));
            } else {
                zx_fl_item_name.setTextColor(zx_fl_item_name.getResources().getColor(R.color.hui999999));
                zx_fl_item.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.touming));
            }
            ll_fl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iPositionId = Integer.valueOf(arrlist.get(position).get("ItemId"));
                    iPosition = position;
                    notifyDataSetChanged();
                    mViewPager.setCurrentItem(position);
                }
            });

            return view;
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
