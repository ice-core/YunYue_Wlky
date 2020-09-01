package com.example.administrator.yunyue.jysq_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_activity.GuanggaoActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Jysq_ShouyeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Jysq_ShouyeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Jysq_ShouyeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Jysq_ShouyeFragment() {
        // Required empty public constructor
    }

    private String sUser_id = "";
    RequestQueue queue = null;
    private SharedPreferences pref;
    /**
     * 话题热点查看更多
     */
    private TextView tv_jysq_shouye_re_ckgd;

    /**
     * 热点话题
     */
    private GridView gv_jysq_redian;
    /**
     * 热门帖子
     */
    private MyGridView mgv_jysq_rmtz;
    private ListView lv_jysq_rmtz;
    /**
     * 发动态
     */
    private LinearLayout ll_jysq_shouye_fdt;
    private MyAdapterRmtz myAdapterShequ;
    private int iPage = 1;
    @BindView(R.id.srl_control)
    SmartRefreshLayout srlControl;
    private EditText et_jysq_shouye_query;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Jysq_ShouyeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Jysq_ShouyeFragment newInstance(String param1, String param2) {
        Jysq_ShouyeFragment fragment = new Jysq_ShouyeFragment();
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
        return inflater.inflate(R.layout.fragment_jysq__shouye, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        queue = Volley.newRequestQueue(getActivity());
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sUser_id = pref.getString("user_id", "");
        tv_jysq_shouye_re_ckgd = view.findViewById(R.id.tv_jysq_shouye_re_ckgd);
        tv_jysq_shouye_re_ckgd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Jysq_Redianctivity.class);
                startActivity(intent);
            }
        });
        gv_jysq_redian = view.findViewById(R.id.gv_jysq_redian);
        mgv_jysq_rmtz = view.findViewById(R.id.mgv_jysq_rmtz);
        ll_jysq_shouye_fdt = view.findViewById(R.id.ll_jysq_shouye_fdt);
        lv_jysq_rmtz = view.findViewById(R.id.lv_jysq_rmtz);
        ll_jysq_shouye_fdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Jysq_FbtzActivity.class);
                intent.putExtra("id", "");
                startActivity(intent);
            }
        });
        et_jysq_shouye_query = view.findViewById(R.id.et_jysq_shouye_query);
        et_jysq_shouye_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    String sQueryText = et_jysq_shouye_query.getText().toString();

                    if (sQueryText.equals("")) {
                    } else {
                        Intent intent = new Intent(getActivity(), Jysq_Redianctivity.class);
                        intent.putExtra("name", sQueryText);
                        startActivity(intent);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
        srlControl = view.findViewById(R.id.srl_control);
        smartRefresh();
        query();
    }

    //监听下拉和上拉状态
    public void smartRefresh() {
        //下拉刷新
        srlControl.setOnRefreshListener(refreshlayout -> {
            srlControl.setEnableRefresh(true);//启用刷新
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 Data() 来模拟我们刷新出来的数据
             */
            iPage = 1;
            query();
            // refreshAdapter.strList.clear();
            // refreshAdapter.refreshData(Data());
            //  iPage = 1;
            // query();
            // srlControl.finishRefresh();//结束刷新
        });
        //上拉加载
        srlControl.setOnLoadmoreListener(refreshlayout -> {
            srlControl.setEnableLoadmore(true);//启用加载
            /**
             * 正常来说，应该在这里加载网络数据
             * 这里我们就使用模拟数据 MoreDatas() 来模拟我们加载出来的数据
             */
            //  refreshAdapter.loadMore(MoreDatas());
            iPage += 1;
            query();

        });
    }

    /**
     * 社群首页信息获取
     */
    private void query() {
        String url = Api.sUrl + "Shequn/Api/index/appId/" + Api.sApp_Id + "/user_id/" + sUser_id + "/page/" + iPage;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (iPage == 1) {
                    srlControl.finishRefresh();//结束刷新
                } else {
                    srlControl.finishLoadmore();//结束加载
                }
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    ArrayList<HashMap<String, String>> mylist_post = new ArrayList<HashMap<String, String>>();
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArray_redian = jsonObjectdata.getJSONArray("redian");
                        ArrayList<HashMap<String, String>> mylist_redian = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < jsonArray_redian.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray_redian.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String ItemTitle = jsonObject2.getString("title");
                            String ItemImg = jsonObject2.getString("img");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("title", ItemTitle);
                            map.put("img", ItemImg);
                            mylist_redian.add(map);
                        }
                        setGridView(mylist_redian);
                        JSONArray jsonArray_post = jsonObjectdata.getJSONArray("post");

                        for (int i = 0; i < jsonArray_post.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArray_post.opt(i);
                            String ItemNickName = jsonObject2.getString("nickname");
                            String ItemUser_Id = jsonObject2.getString("user_id");
                            String ItemId = jsonObject2.getString("id");
                            String ItemHeadimgurl = jsonObject2.getString("headimgurl");
                            String ItemTitle = jsonObject2.getString("title");
                            String ItemContent = jsonObject2.getString("content");
                            String ItemTime = jsonObject2.getString("time");
                            String ItemImg1 = jsonObject2.getString("img1");
                            String ItemImg2 = jsonObject2.getString("img2");
                            String ItemImg3 = jsonObject2.getString("img3");
                            String ItemZan = jsonObject2.getString("zan");
                            String ItemComment = jsonObject2.getString("comment");
                            String ItemUrl = jsonObject2.getString("url");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("nickname", ItemNickName);
                            map.put("user_id", ItemUser_Id);
                            map.put("id", ItemId);
                            map.put("headimgurl", ItemHeadimgurl);
                            map.put("title", ItemTitle);
                            map.put("content", ItemContent);
                            map.put("time", ItemTime);
                            map.put("img1", ItemImg1);
                            map.put("img2", ItemImg2);
                            map.put("img3", ItemImg3);
                            map.put("zan", ItemZan);
                            map.put("comment", ItemComment);
                            map.put("url", ItemUrl);
                            mylist_post.add(map);
                        }
                        if (iPage == 1) {
                            Mgv_Rmtz(mylist_post);
                        } else {
                            if (mylist_post.size() == 0) {
                                iPage -= 1;
                            } else {
                                Mgv_Rmtz1(mylist_post);
                            }
                        }
                        // Hint(resultMsg, HintDialog.SUCCESS);
                    } else {

                        Hint(resultMsg, HintDialog.ERROR);
                        if (iPage == 1) {
                            Mgv_Rmtz(mylist_post);
                        } else {
                            if (mylist_post.size() == 0) {
                                iPage -= 1;
                            } else {
                                Mgv_Rmtz1(mylist_post);
                            }
                        }
                    }
                } catch (JSONException e) {
                    hideDialogin();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                srlControl.finishRefresh();//结束刷新
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        request.setShouldCache(false);
        queue.add(request);
    }

    /**
     * 话题热点
     * 设置GirdView参数，绑定数据
     */
    private void setGridView(ArrayList<HashMap<String, String>> mylist) {
        MyAdapter myAdapter = new MyAdapter(getActivity());
        myAdapter.arrlist = mylist;
        int size = mylist.size();
        int length = 110;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gv_jysq_redian.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gv_jysq_redian.setColumnWidth(itemWidth); // 设置列表项宽
        gv_jysq_redian.setHorizontalSpacing(10); // 设置列表项水平间距
        gv_jysq_redian.setStretchMode(GridView.NO_STRETCH);
        gv_jysq_redian.setNumColumns(size); // 设置列数量=列表集合数
        gv_jysq_redian.setAdapter(myAdapter);
/*        sv_shequn.smoothScrollTo(0, 20);
        sv_shequn.setFocusable(true);*/

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
                view = inflater.inflate(R.layout.jysq_rd_item, null);
            }
        /*    map.put("id", ItemId);
            map.put("title", ItemTitle);
            map.put("img", ItemImg);*/
            LinearLayout ll_jysq_rd_item = view.findViewById(R.id.ll_jysq_rd_item);
            TextView tv_jysq_rd_item_title = view.findViewById(R.id.tv_jysq_rd_item_title);
            tv_jysq_rd_item_title.setText(arrlist.get(position).get("title"));

            ImageView iv_jysq_rd_item_img = view.findViewById(R.id.iv_jysq_rd_item_img);
            Glide.with(getActivity())
                    .load( Api.sUrl+ arrlist.get(position).get("img"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_jysq_rd_item_img);
            ll_jysq_rd_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Jysq_XqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("id"));
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    /**
     * 热门帖子
     */
    private void Mgv_Rmtz(ArrayList<HashMap<String, String>> mylist) {
        myAdapterShequ = new MyAdapterRmtz(getActivity());
        myAdapterShequ.arrlist = mylist;
        //  mgv_jysq_rmtz.setAdapter(myAdapterShequ);
        lv_jysq_rmtz.setAdapter(myAdapterShequ);
        setListViewHeightBasedOnChildren(lv_jysq_rmtz);
    }

    private void Mgv_Rmtz1(ArrayList<HashMap<String, String>> mylist) {

        myAdapterShequ.arrlist.addAll(mylist);
        //  mgv_jysq_rmtz.setAdapter(myAdapterShequ);
        lv_jysq_rmtz.setAdapter(myAdapterShequ);
        setListViewHeightBasedOnChildren(lv_jysq_rmtz);

    }

    /**
     * listview自适应高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView1) {
        BaseAdapter listAdapter = (BaseAdapter) listView1.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //获取listView的宽度
        ViewGroup.LayoutParams params = listView1.getLayoutParams();
        int listViewWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView1);
            //给item的measure设置参数是listView的宽度就可以获取到真正每一个item的高度
            listItem.measure(widthSpec, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        params.height = totalHeight + (listView1.getDividerHeight() * (listAdapter.getCount() + 1));
        listView1.setLayoutParams(params);
    }

    private class MyAdapterRmtz extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterRmtz(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<HashMap<String, String>>();

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
                view = inflater.inflate(R.layout.jysq_rm_item, null);
            }

            LinearLayout ll_jysq_rm_item = view.findViewById(R.id.ll_jysq_rm_item);
            ll_jysq_rm_item.setBackgroundResource(R.drawable.bj_4_ffffff);
            ImageView civ_jysq_rm_item_headimgurl = view.findViewById(R.id.civ_jysq_rm_item_headimgurl);
            TextView tv_jysq_rm_item_nickname = view.findViewById(R.id.tv_jysq_rm_item_nickname);
            TextView tv_jysq_rm_item_title = view.findViewById(R.id.tv_jysq_rm_item_title);
            TextView tv_jysq_rm_item_content = view.findViewById(R.id.tv_jysq_rm_item_content);

            ImageView iv_jysq_rm_item_img1 = view.findViewById(R.id.iv_jysq_rm_item_img1);
            ImageView iv_jysq_rm_item_img2 = view.findViewById(R.id.iv_jysq_rm_item_img2);
            ImageView iv_jysq_rm_item_img3 = view.findViewById(R.id.iv_jysq_rm_item_img3);

            TextView tv_jysq_rm_item_zan = view.findViewById(R.id.tv_jysq_rm_item_zan);
            TextView tv_jysq_rm_item_comment = view.findViewById(R.id.tv_jysq_rm_item_comment);
            TextView tv_jysq_rm_item_kw = view.findViewById(R.id.tv_jysq_rm_item_kw);
            tv_jysq_rm_item_kw.setVisibility(View.VISIBLE);

     /*       map.put("nickname", ItemNickName);
            map.put("user_id", ItemUser_Id);
            map.put("id", ItemId);
            map.put("headimgurl", ItemHeadimgurl);
            map.put("title", ItemTitle);
            map.put("content", ItemContent);
            map.put("time", ItemTime);
            map.put("img1", ItemImg1);
            map.put("img2", ItemImg2);
            map.put("img3", ItemImg3);
            map.put("zan", ItemZan);
            map.put("comment", ItemComment);
            map.put("url", ItemUrl);*/


            Glide.with(getActivity())
                    .load( Api.sUrl+ arrlist.get(position).get("headimgurl"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(civ_jysq_rm_item_headimgurl);
            tv_jysq_rm_item_nickname.setText(arrlist.get(position).get("nickname"));
            tv_jysq_rm_item_title.setText(arrlist.get(position).get("title"));
            tv_jysq_rm_item_content.setText(arrlist.get(position).get("content"));


            tv_jysq_rm_item_zan.setText(arrlist.get(position).get("zan"));
            tv_jysq_rm_item_comment.setText(arrlist.get(position).get("comment"));
            LinearLayout ll_jysq_rm_item_img = view.findViewById(R.id.ll_jysq_rm_item_img);
            /*civ_jysq_rm_item_headimgurl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GrtzActivity.class);
                    GrtzActivity.sFriend_id = arrlist.get(position).get("user_id");
                    ShequnXqActivity.community_id = arrlist.get(position).get("community_id");
                    startActivity(intent);
                }
            });*/

            if (arrlist.get(position).get("img1").equals("")) {
                ll_jysq_rm_item_img.setVisibility(View.GONE);
            } else {
                ll_jysq_rm_item_img.setVisibility(View.VISIBLE);
                Glide.with(getActivity())
                        .load( Api.sUrl+ arrlist.get(position).get("img1"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_jysq_rm_item_img1);
            }
            if (arrlist.get(position).get("img2").equals("")) {
            } else {
                Glide.with(getActivity())
                        .load( Api.sUrl+ arrlist.get(position).get("img2"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_jysq_rm_item_img2);
            }
            if (arrlist.get(position).get("img3").equals("")) {
            } else {
                Glide.with(getActivity())
                        .load( Api.sUrl+ arrlist.get(position).get("img3"))
                        .asBitmap()
                        .placeholder(R.mipmap.error)
                        .error(R.mipmap.error)
                        .dontAnimate()
                        .into(iv_jysq_rm_item_img3);
            }

            ll_jysq_rm_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GuanggaoActivity.class);
                    intent.putExtra("link", arrlist.get(position).get("url"));
                    startActivity(intent);
                }
            });
           /* ll_shequn_shequ_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iv_shequn_shequ_item.getDrawable().getCurrent().getConstantState().equals(getResources()
                            .getDrawable(R.drawable.public_btn_like_current_3x).getConstantState())) {
                        //当image1的src为R.drawable.A时，设置image1的src为R.drawable.B
                        iv_shequn_shequ_item.setImageResource(R.drawable.public_btn_like_moren_3x);
                        tv_shequn_dianzhan.setText(String.valueOf(Integer.valueOf(tv_shequn_dianzhan.getText().toString()) - 1));
                    } else {
                        //否则设置image1的src为R.drawable.A
                        iv_shequn_shequ_item.setImageResource(R.drawable.public_btn_like_current_3x);
                        tv_shequn_dianzhan.setText(String.valueOf(Integer.valueOf(tv_shequn_dianzhan.getText().toString()) + 1));
                    }
                }
            });
            tv_shequn_tuijian_name_guanzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tv_shequn_tuijian_name_guanzhu.getText().equals("关注")) {
                        dialogin("");
                        jiaruShequn(arrlist.get(position).get("community_id"));
                        tv_shequn_tuijian_name_guanzhu.setText("取消关注");
                        tv_shequn_tuijian_name_guanzhu.setBackgroundResource(R.drawable.bj_lan12);
                        tv_shequn_tuijian_name_guanzhu.setTextColor(tv_shequn_tuijian_name_guanzhu.getResources().getColor(R.color.white_c));
                    } else {
                        tv_shequn_tuijian_name_guanzhu.setText("关注");
                        tv_shequn_tuijian_name_guanzhu.setBackgroundResource(R.drawable.bg_lan12);
                        tv_shequn_tuijian_name_guanzhu.setTextColor(tv_shequn_tuijian_name_guanzhu.getResources().getColor(R.color.lan_4A90E2));
                    }
                }
            });
            ll_shequn_tuijian_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ShequnXqActivity.class);
                    intent.putExtra("id", arrlist.get(position).get("community_id"));
                    startActivity(intent);
                }
            });*/
            return view;
        }
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(getActivity(), R.style.dialog, msg, LoadingDialog.WAITING_C);
        loadingDialog.show();
    }

    private void hideDialogin() {
        if (!NullTranslator.isNullEmpty(loadingDialog)) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 消息提示
     */

    protected void Hint(String sHint, int type) {
        new HintDialog(getActivity(), R.style.dialog, sHint, type, true).show();
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
