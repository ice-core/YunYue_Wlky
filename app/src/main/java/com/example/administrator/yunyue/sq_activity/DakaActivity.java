package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.ChongzhiActivity;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.sc.WXPayUtils;
import com.example.administrator.yunyue.sc_activity.DpzyActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DakaActivity extends AppCompatActivity {
    private static final String TAG = DakaActivity.class.getSimpleName();
    private ImageView iv_dk_back;
    private GridView gv_daka;
    private int year = 0;
    private int month = 0;
    private int tian = 0;
    private int yuechu = 0;
    private int yuemo = 0;
    private int zong = 0;
    private int date = 0;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sIs_vip = "";
    //private FrameLayout fl_daka_qd;
    private String[] sDkjl;

    private TextView tv_dk_rq;
    private String yue = "";


    /**
     * 早班
     */
    private TextView tv_dk_zaoban;
    /**
     * 中班
     */
    private TextView tv_dk_zhongban;
    /**
     * 晚班
     */
    private TextView tv_dk_wanban;
    /**
     * 夜班
     */
    private TextView tv_dk_yeban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_daka);
        queue = Volley.newRequestQueue(DakaActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        iv_dk_back = findViewById(R.id.iv_dk_back);
        gv_daka = findViewById(R.id.gv_daka);
        //   fl_daka_qd = findViewById(R.id.fl_daka_qd);
        tv_dk_rq = findViewById(R.id.tv_dk_rq);

        iv_dk_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_dk_zaoban = findViewById(R.id.tv_dk_zaoban);
        tv_dk_zaoban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sClockIn();

            }
        });
        tv_dk_zhongban = findViewById(R.id.tv_dk_zhongban);
        tv_dk_zhongban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sClockIn();

            }
        });
        tv_dk_wanban = findViewById(R.id.tv_dk_wanban);
        tv_dk_wanban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sClockIn();

            }
        });
        tv_dk_yeban = findViewById(R.id.tv_dk_yeban);
        tv_dk_yeban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sClockIn();

            }
        });


        Calendar cal = Calendar.getInstance();

        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH) + 1;//老外把一月份整成了0，翻译成中国月份要加1
        date = cal.get(cal.DATE);
        tian = getDaysOfMonth(year, month);

        yuechu = getDayofweek(year + "-" + month + "-1") - 1;
        yuemo = getDayofweek(year + "-" + month + "-" + tian) - 1;

        zong = tian + yuechu;

        zong = zong + (6 - yuemo);

        if (month < 10) {
            yue = "0" + month;
        } else {
            yue = month + "";
        }
        tv_dk_rq.setText(year + "-" + yue + "-" + date);
        query();
        sClockInList();
    }

    /**
     * 获取打卡
     */
    private void query() {
        String url = Api.sUrl + "Community/Api/signList/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONArray jsonArray_data = jsonObject.getJSONArray("data");
                        sDkjl = new String[jsonArray_data.length()];
                        for (int i = 0; i < jsonArray_data.length(); i++) {
                            //提取出family中的所有
                            String s1 = (String) jsonArray_data.get(i);
                            sDkjl[i] = s1;
                            //System.out.println("currentFamily:" + s1);
                        }

                        gv_qunliao();

                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    hideDialogin();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        request.setShouldCache(false);
        queue.add(request);
    }

    /**
     * 方法名：sClockIn()
     * 功  能：打卡接口
     * 参  数：appId
     */
    private void sClockIn() {
        String url = Api.sUrl + Api.sClockIn;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);

        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                Hint(resultMsg, HintDialog.SUCCESS);
                                query();
                                sClockInList();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }


    /**
     * 方法名：sClockInList()
     * 功  能：打卡接口
     * 参  数：appId
     */
    private void sClockInList() {
        String url = Api.sUrl + Api.sClockInList;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialogin();
                        String sDate = response.toString();
                        System.out.println(sDate);
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                                //早班 1为已打卡
                                String sMorning = jsonObjectdate.getString("morning");
                                //中班 1为已打卡
                                String sNoon = jsonObjectdate.getString("noon");
                                //晚班 1为已打卡
                                String sEvening = jsonObjectdate.getString("evening");
                                //夜班 1为已打卡
                                String sNight = jsonObjectdate.getString("night");
                                if (sMorning.equals("1")) {
                                    tv_dk_zaoban.setClickable(false);
                                    tv_dk_zaoban.setText("已打卡");
                                    tv_dk_zaoban.setBackgroundResource(R.drawable.bj_8_d8d8d8);
                                    tv_dk_zaoban.setTextColor(tv_dk_zaoban.getResources().getColor(R.color.hui999999));
                                } else {
                                    tv_dk_zaoban.setClickable(true);
                                    tv_dk_zaoban.setText("打卡");
                                    tv_dk_zaoban.setBackgroundResource(R.drawable.bj_8_ff5b00);
                                    tv_dk_zaoban.setTextColor(tv_dk_zaoban.getResources().getColor(R.color.white));
                                }

                                if (sNoon.equals("1")) {
                                    tv_dk_zhongban.setClickable(false);
                                    tv_dk_zhongban.setText("已打卡");
                                    tv_dk_zhongban.setBackgroundResource(R.drawable.bj_8_d8d8d8);
                                    tv_dk_zhongban.setTextColor(tv_dk_zhongban.getResources().getColor(R.color.hui999999));
                                } else {
                                    tv_dk_zhongban.setClickable(true);
                                    tv_dk_zhongban.setText("打卡");
                                    tv_dk_zhongban.setBackgroundResource(R.drawable.bj_8_ff5b00);
                                    tv_dk_zhongban.setTextColor(tv_dk_zhongban.getResources().getColor(R.color.white));
                                }

                                if (sEvening.equals("1")) {
                                    tv_dk_wanban.setClickable(false);
                                    tv_dk_wanban.setText("已打卡");
                                    tv_dk_wanban.setBackgroundResource(R.drawable.bj_8_d8d8d8);
                                    tv_dk_wanban.setTextColor(tv_dk_wanban.getResources().getColor(R.color.hui999999));
                                } else {
                                    tv_dk_wanban.setClickable(true);
                                    tv_dk_wanban.setText("打卡");
                                    tv_dk_wanban.setBackgroundResource(R.drawable.bj_8_ff5b00);
                                    tv_dk_wanban.setTextColor(tv_dk_wanban.getResources().getColor(R.color.white));
                                }

                                if (sNight.equals("1")) {
                                    tv_dk_yeban.setClickable(false);
                                    tv_dk_yeban.setText("已打卡");
                                    tv_dk_yeban.setBackgroundResource(R.drawable.bj_8_d8d8d8);
                                    tv_dk_yeban.setTextColor(tv_dk_yeban.getResources().getColor(R.color.hui999999));
                                } else {
                                    tv_dk_yeban.setClickable(true);
                                    tv_dk_yeban.setText("打卡");
                                    tv_dk_yeban.setBackgroundResource(R.drawable.bj_8_ff5b00);
                                    tv_dk_yeban.setTextColor(tv_dk_yeban.getResources().getColor(R.color.white));
                                }

                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialogin();
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }

    private void error(VolleyError error) {
        Log.e(TAG, error.getMessage(), error);
        if (error != null) {
            if (error instanceof TimeoutError) {
                // Toast.makeText(mActivity,"网络请求超时，请重试！",Toast.LENGTH_SHORT).show();
                Hint("网络请求超时，请重试！", HintDialog.ERROR);
                return;
            }
            if (error instanceof ServerError) {
                //  Toast.makeText(mActivity,"服务器异常",Toast.LENGTH_SHORT).show();
                Hint("服务器异常", HintDialog.ERROR);
                return;
            }
            if (error instanceof NetworkError) {
                // Toast.makeText(mActivity,"请检查网络",Toast.LENGTH_SHORT).show();
                Hint("请检查网络", HintDialog.ERROR);
                return;
            }
            if (error instanceof ParseError) {
                Hint("数据格式错误", HintDialog.ERROR);
                //  Toast.makeText(mActivity,"数据格式错误",Toast.LENGTH_SHORT).show();
                return;
            }

            Hint(error.toString(), HintDialog.ERROR);
        }
    }


    /**
     * 打卡
     */
    private void daka() {
        String url = Api.sUrl + "Community/Api/sign/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        Hint(resultMsg, HintDialog.SUCCESS);
                        query();
                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    hideDialogin();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        request.setShouldCache(false);
        queue.add(request);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(DakaActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(DakaActivity.this, R.style.dialog, sHint, type, true).show();
    }

    public static int getDayofweek(String date) {
        Calendar cal = Calendar.getInstance();
//   cal.setTime(new Date(System.currentTimeMillis()));
        if (date.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            cal.setTime(new Date(getDateByStr2(date).getTime()));
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static Date getDateByStr2(String dd) {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sd.parse(dd);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    //取得某个月有多少天
    public static int getDaysOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int days_of_month = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days_of_month;
    }

    /**
     * 群聊
     */
    private void gv_qunliao() {
        MyAdapter myAdapter = new MyAdapter(this);
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < zong; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            if (i < yuechu) {
                map.put("ItemId", "");
                map.put("ItemIs", "");
                mylist.add(map);
            } else if (i > (tian + yuechu - 1)) {
                map.put("ItemId", "");
                map.put("ItemIs", "");
                mylist.add(map);
            } else {
                String day = "";
                if ((i - yuechu + 1) < 10) {
                    day = "0" + (i - yuechu + 1);
                } else {
                    day = "" + (i - yuechu + 1);
                }
                if (findStr(sDkjl, year + "-" + yue + "-" + day)) {
                    map.put("ItemIs", "");
                } else {
                    map.put("ItemIs", "1");
                }

                map.put("ItemId", String.valueOf(i - yuechu + 1));
                mylist.add(map);
            }
        }
        myAdapter.arrlist = mylist;
        gv_daka.setAdapter(myAdapter);

    }

    public boolean findStr(String[] args, String str) {
        boolean result = false;
        //第一种：List
        result = Arrays.asList(args).contains(str);
        return result;
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
                view = inflater.inflate(R.layout.daka_item, null);
            }
            FrameLayout fl_daka_item = view.findViewById(R.id.fl_daka_item);

            TextView tv_daka_item = view.findViewById(R.id.tv_daka_item);
            ImageView iv_daka_item = view.findViewById(R.id.iv_daka_item);
            TextView tv_daka_item1 = view.findViewById(R.id.tv_daka_item1);
            tv_daka_item.setText(arrlist.get(position).get("ItemId"));
            tv_daka_item1.setText(arrlist.get(position).get("ItemId"));
            if (arrlist.get(position).get("ItemId").equals("")) {
                iv_daka_item.setVisibility(View.GONE);
            } else {
                if (arrlist.get(position).get("ItemIs").equals("1")) {
                    //  iv_daka_item.setImageResource(R.drawable.icon_zuanshi_normal_3x);
                    iv_daka_item.setVisibility(View.GONE);
                    tv_daka_item.setVisibility(View.GONE);
                    tv_daka_item1.setVisibility(View.VISIBLE);
                } else {
                    tv_daka_item1.setVisibility(View.GONE);
                    iv_daka_item.setImageResource(R.mipmap.daka_gou);
                }
            }
            return view;
        }
    }
}
