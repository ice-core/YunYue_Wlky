package com.example.administrator.yunyue.sc_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.Listview.ListView;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class XiaoxiActivity extends AppCompatActivity {
    private static final String TAG = XiaoxiActivity.class.getSimpleName();
    private ImageView iv_gfxx_back;
    private String sUserId = "";
    private SharedPreferences pref;
    RequestQueue queue = null;
    private ListView lv_sqtz;
    private int iPage;
    private ArrayList<HashMap<String, String>> arrmylist;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_xiaoxi);
        queue = Volley.newRequestQueue(XiaoxiActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(XiaoxiActivity.this);
        sUserId = pref.getString("user_id", "");
        iv_gfxx_back = (ImageView) findViewById(R.id.iv_gfxx_back);
        lv_sqtz = (ListView) findViewById(R.id.lv_sqtz);
        iv_gfxx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        iPage = 1;
        arrmylist = new ArrayList<HashMap<String, String>>();
        myAdapter = new MyAdapter(XiaoxiActivity.this);
        lv_sqtz.setOnRefreshListener(new ListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   ToastUtil.showShortToast(context, "刷新成功");

                        iPage = 1;
                        sqtz(iPage);
                      /*  dataList.add(0, "header  header  header");
                        adapter.notifyDataSetChanged();*/

                    }
                }, 2000);
            }
        });

        lv_sqtz.setOnLoadMoreListener(new ListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   ToastUtil.showShortToast(context, "上拉加载成功");
                        sqtz(iPage);
             /*        dataList.add("footer  footer  footer  ");
                        adapter.notifyDataSetChanged();*/

                    }
                }, 2000);
            }
        });
        dialogin("");
        sqtz(iPage);
    }


    /**
     * 获取消息
     */
    private void sqtz(final int page) {
        String url = Api.sUrl + "My/gettongzhi/user_id/" + sUserId + "/page/" + page;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    if (page == 0) {
                        myAdapter = new MyAdapter(XiaoxiActivity.this);
                        arrmylist = new ArrayList<HashMap<String, String>>();
                    }
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemCreatetime", jsonObject.getString("createtime"));//id
                            map.put("ItemContent", jsonObject.getString("content"));//
                            arrmylist.add(map);
                        }
                        if (iPage == 1) {
                            lv_sqtz.setAdapter(myAdapter);
                            lv_sqtz.finishRefresh();
                        } else {
                            lv_sqtz.finishLoadMore();
                        }
                        iPage += 1;

                    } else {
                        if (iPage == 1) {
                            lv_sqtz.setAdapter(myAdapter);
                            lv_sqtz.finishRefresh();
                        } else {
                            lv_sqtz.finishLoadMore();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }


    private void back() {
        finish();
    }

    /**
     *  * 方法名：onKeyDown(int keyCode, KeyEvent event)
     *  * 功    能：菜单栏点击事件
     *  * 参    数：int keyCode, KeyEvent event
     *  * 返回值：无
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                //   showDialog();
                back();
                break;
            /*
            * Home键是系统事件，不能通过KeyDown监听
            * 此处log不会打印
            */
            case KeyEvent.KEYCODE_HOME:
                Log.d(TAG, "onKeyDown KEYCODE_HOME");
                break;
            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG, "onKeyDown KEYCODE_MENU");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(XiaoxiActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(XiaoxiActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;
    /*    public ArrayList<HashMap<String, String>> arrmylist;*/


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);
  /*          arrmylist = new ArrayList<HashMap<String, String>>();*/


        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrmylist.size();
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

        public Bitmap stringToBitmap(String string) {    // 将字符串转换成Bitmap类型
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater1.inflate(R.layout.xiaoxi_item, null);
            }

            TextView tv_sqtc_time = view.findViewById(R.id.tv_sqtc_time);
            TextView tv_sqtz_content = view.findViewById(R.id.tv_sqtz_content);

            tv_sqtc_time.setText(arrmylist.get(position).get("ItemCreatetime"));
            tv_sqtz_content.setText(arrmylist.get(position).get("ItemContent"));


            return view;
        }
    }
}
