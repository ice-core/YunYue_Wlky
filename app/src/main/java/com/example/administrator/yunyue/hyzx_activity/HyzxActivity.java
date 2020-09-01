package com.example.administrator.yunyue.hyzx_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.sc_activity.GwcActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.yjdt_activity.BjzlActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HyzxActivity extends AppCompatActivity {
    private static final String TAG = HyzxActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_hyzx_back;
    /**
     * 升级礼包
     */
    private LinearLayout ll_hyzx_sjlb;
    /**
     * 会员专享
     */
    private LinearLayout ll_hyzx_hyzx;
    /**
     * 金币超值
     */
    private LinearLayout ll_hyzx_jbcz;
    /**
     * 生日惊喜
     */
    private LinearLayout ll_hyzx_srjx;
    /**
     * 专享客服
     */
    private LinearLayout ll_hyzx_zxkf;
    /**
     * 去提升
     */
    private TextView tv_hyzx_qts;

    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 规则
     */
    private TextView tv_hyzx_gz;
    /**
     * 用户金币
     */
    private TextView tv_hyzx_coins;
    /**
     * 用户昵称
     */
    private TextView tv_hyzx_nickname;
    /**
     * 用户头像
     */
    private CircleImageView civ_hyzx_headimgurl;
    /**
     * 会员等级描述
     */
    private TextView tv_hyzx_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.lan_4350b6);
        }
        setContentView(R.layout.activity_hyzx);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_hyzx_back = findViewById(R.id.ll_hyzx_back);
        ll_hyzx_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_hyzx_sjlb = findViewById(R.id.ll_hyzx_sjlb);
        ll_hyzx_hyzx = findViewById(R.id.ll_hyzx_hyzx);
        ll_hyzx_jbcz = findViewById(R.id.ll_hyzx_jbcz);
        ll_hyzx_srjx = findViewById(R.id.ll_hyzx_srjx);
        ll_hyzx_zxkf = findViewById(R.id.ll_hyzx_zxkf);
        tv_hyzx_qts = findViewById(R.id.tv_hyzx_qts);
        tv_hyzx_gz = findViewById(R.id.tv_hyzx_gz);
        tv_hyzx_coins = findViewById(R.id.tv_hyzx_coins);
        tv_hyzx_nickname = findViewById(R.id.tv_hyzx_nickname);
        civ_hyzx_headimgurl = findViewById(R.id.civ_hyzx_headimgurl);

        tv_hyzx_text = findViewById(R.id.tv_hyzx_text);
        tv_hyzx_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HyzxActivity.this, Hyzx_JlgzActivity.class);
                startActivity(intent);
            }
        });
        sShouye();
    }


    /**
     * 方法名：sShouye()
     * 功  能：会员中心首页信息
     * 参  数：appId
     */
    private void sShouye() {
        String url = Api.sUrl + Api.sShouye;
        RequestQueue requestQueue = Volley.newRequestQueue(HyzxActivity.this);
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
                                //用户 ID
                                String resultId = jsonObjectuser.getString("id");
                                //用户手机号
                                String resultMobile = jsonObjectuser.getString("mobile");
                                //用户昵称
                                String resultNickname = jsonObjectuser.getString("nickname");
                                tv_hyzx_nickname.setText(resultNickname);
                                //用户头像
                                String resultHeadimgurl = jsonObjectuser.getString("headimgurl");

                                Glide.with(HyzxActivity.this)
                                        .load( Api.sUrl+resultHeadimgurl)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(civ_hyzx_headimgurl);

                                //用户金币
                                String resultCoins = jsonObjectuser.getString("coins");
                                tv_hyzx_coins.setText(resultCoins + "金");
                                //会员等级
                                String resultLevel = jsonObjectuser.getString("level");
                                //会员等级描述
                                String resultText = jsonObjectuser.getString("text");
                                tv_hyzx_text.setText(resultText);
                                //分类数据
                                JSONArray jsonArraytype = jsonObjectdata.getJSONArray("type");
                                ArrayList<HashMap<String, String>> mylist_type = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArraytype.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArraytype.opt(i);
                                    //分类ID
                                    String ItemId = jsonObject.getString("id");
                                    //分类描述
                                    String ItemDesc = jsonObject.getString("desc");
                                    //分类图像
                                    String ItemImg = jsonObject.getString("img");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("desc", ItemDesc);
                                    map.put("img", ItemImg);
                                    mylist_type.add(map);
                                }

                                //升级礼包
                                JSONArray jsonArrayshenji = jsonObjectdata.getJSONArray("shenji");
                                ArrayList<HashMap<String, String>> mylist_shenji = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArrayshenji.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArrayshenji.opt(i);
                                    //分类ID
                                    String ItemId = jsonObject.getString("id");
                                    //分类描述
                                    String ItemLogo = jsonObject.getString("logo");
                                    //分类图像
                                    String ItemName = jsonObject.getString("name");
                                    //分类图像
                                    String ItemCoins = jsonObject.getString("coins");
                                    //分类图像
                                    String ItemLevel = jsonObject.getString("level");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("logo", ItemLogo);
                                    map.put("name", ItemName);
                                    map.put("coins", ItemCoins);
                                    map.put("level", ItemLevel);
                                    mylist_shenji.add(map);
                                }

                                //红包数据
                                JSONArray jsonArrayhong = jsonObjectdata.getJSONArray("hong");
                                ArrayList<HashMap<String, String>> mylist_hong = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArrayhong.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArrayhong.opt(i);
                                    //分类ID
                                    String ItemId = jsonObject.getString("id");
                                    //分类描述
                                    String ItemName = jsonObject.getString("name");
                                    //最低消费
                                    String ItemZuidi = jsonObject.getString("zuidi");
                                    //红包金额
                                    String ItemYouhui = jsonObject.getString("youhui");
                                    //会员等级
                                    String ItemLevel = jsonObject.getString("level");
                                    //金币
                                    String ItemCoins = jsonObject.getString("coins");
                                    //会员等级描述
                                    String ItemText = jsonObject.getString("text");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("name", ItemName);
                                    map.put("zuidi", ItemZuidi);
                                    map.put("youhui", ItemYouhui);
                                    map.put("level", ItemLevel);
                                    map.put("coins", ItemCoins);
                                    map.put("text", ItemText);
                                    mylist_hong.add(map);
                                }

                                //定时更新
                                JSONArray jsonArraymiaosha = jsonObjectdata.getJSONArray("miaosha");
                                ArrayList<HashMap<String, String>> mylist_miaosha = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArraymiaosha.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArraymiaosha.opt(i);
                                    //定点更新来临时间
                                    String ItemLinetime = jsonObject.getString("linetime");
                                    //array
                                    String ItemGoodlsit = jsonObject.getString("goodlsit");
                                    //商品id
                                    String ItemId = jsonObject.getString("id");
                                    //
                                    String ItemName = jsonObject.getString("name");
                                    //商品图片
                                    String ItemLogo = jsonObject.getString("logo");
                                    //商品价格
                                    String ItemPrice = jsonObject.getString("price");
                                    //商品市场价格
                                    String ItemMktprice = jsonObject.getString("mktprice");
                                    //库存
                                    String ItemStock = jsonObject.getString("stock");
                                    //已售数量
                                    String ItemBuy_count = jsonObject.getString("buy_count");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("linetime", ItemLinetime);
                                    map.put("goodlsit", ItemGoodlsit);
                                    map.put("id", ItemId);
                                    map.put("name", ItemName);
                                    map.put("logo", ItemLogo);
                                    map.put("price", ItemPrice);
                                    map.put("mktprice", ItemMktprice);
                                    map.put("stock", ItemStock);
                                    map.put("buy_count", ItemBuy_count);

                                    mylist_miaosha.add(map);
                                }


                            } else {
                                hideDialogin();
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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
        loadingDialog = new LoadingDialog(HyzxActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(HyzxActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
