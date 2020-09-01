package com.example.administrator.yunyue.erci;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.image.ImagePagerActivity;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FenxiangActivity extends AppCompatActivity {
    private static final String TAG = FenxiangActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fx_back;
    /**
     * 用户头像
     */
    private ImageView iv_fx_headimgurl;
    /**
     * 用户昵称
     */
    private TextView tv_fx_nickname;
    /**
     * 用户推广码
     */
    private TextView tv_fx_invitation;
    /**
     * 分享二维码
     */
    private ImageView iv_fx_code;
    /**
     * 邀请记录
     */
    private MyGridView mgv_fx_yqjl;
    private SharedPreferences pref;
    private String sUser_id;
    //分享二维码
    private String sResultCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fenxiang);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_fx_back = findViewById(R.id.ll_fx_back);
        ll_fx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_fx_headimgurl = findViewById(R.id.iv_fx_headimgurl);
        tv_fx_nickname = findViewById(R.id.tv_fx_nickname);
        tv_fx_invitation = findViewById(R.id.tv_fx_invitation);
        iv_fx_code = findViewById(R.id.iv_fx_code);
        mgv_fx_yqjl = findViewById(R.id.mgv_fx_yqjl);
        sYaoqin();
        iv_fx_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<>();
                list.add(sResultCode);
                ImagePagerActivity.startImagePagerActivity(FenxiangActivity.this, list, 0, new ImagePagerActivity
                        .ImageSize(1000, 1000));
            }
        });
    }

    /**
     * 方法名：sYaoqin()
     * 功  能：分享页面
     * 参  数：appId
     */
    private void sYaoqin() {
        String url = Api.sUrl + Api.sYaoqin;
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
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONObject jsonObjectdata = response.getJSONObject("data");
                                //用户数据
                                JSONObject jsonObjectuser = jsonObjectdata.getJSONObject("user");
                                //用户昵称
                                String resultNickname = jsonObjectuser.getString("nickname");
                                tv_fx_nickname.setText(resultNickname);
                                //用户头像
                                String resultHeadimgurl = jsonObjectuser.getString("headimgurl");
                                Glide.with(FenxiangActivity.this)
                                        .load( Api.sUrl+ resultHeadimgurl)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(iv_fx_headimgurl);
                                tv_fx_nickname.setText(resultNickname);
                                //用户推广码
                                String resultInvitation = jsonObjectuser.getString("invitation");
                                tv_fx_invitation.setText("推广码：" + resultInvitation);
                                //分享二维码
                                sResultCode = jsonObjectuser.getString("code");
                                Glide.with(FenxiangActivity.this)
                                        .load( Api.sUrl+sResultCode)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(iv_fx_code);
                                tv_fx_nickname.setText(resultNickname);
                                MyAdapter myAdapter = new MyAdapter(FenxiangActivity.this);
                                JSONArray jsonArray = jsonObjectdata.getJSONArray("yaoqin");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                                    //昵称
                                    String ItemNickname = jsonObject.getString("nickname");
                                    //账号
                                    String ItemMobile = jsonObject.getString("mobile");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("nickname", ItemNickname);
                                    map.put("mobile", ItemMobile);
                                    mylist.add(map);
                                }
                                myAdapter.arrmylist = mylist;
                                mgv_fx_yqjl.setAdapter(myAdapter);
                            } else {
                                hideDialogin();
                                //  Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());

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


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(FenxiangActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        try {
            new HintDialog(FenxiangActivity.this, R.style.dialog, sHint, type, true).show();
        } catch (Exception e) {
            hideDialogin();
            e.printStackTrace();
        }
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;

        public ArrayList<HashMap<String, String>> arrmylist;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);
            arrmylist = new ArrayList<HashMap<String, String>>();

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

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater1.inflate(R.layout.fx_yqjl_item, null);
            }
            TextView tv_fx_yqjl_nickname = view.findViewById(R.id.tv_fx_yqjl_nickname);
            tv_fx_yqjl_nickname.setText(arrmylist.get(position).get("nickname"));
            TextView tv_fx_yqjl_mobile = view.findViewById(R.id.tv_fx_yqjl_mobile);
            tv_fx_yqjl_mobile.setText(arrmylist.get(position).get("mobile"));
            return view;
        }
    }
}
