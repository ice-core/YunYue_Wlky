package com.example.administrator.yunyue.yjdt_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.Jjcc;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.WeChatShareUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.rong.imkit.utilities.RongUtils.getScreenWidth;

public class GuanggaoxiangqingActivity extends AppCompatActivity {
    private static final String TAG = GuanggaoxiangqingActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_guanggaoxiangqing_back;
    /**
     * 广告id
     */
    private String sId = "";
    /**
     * 标题
     */
    private TextView tv_guanggaoxiangqing_hint;
    /**
     * 结束时间
     */
    private TextView tv_guanggaoxiangqing_end_time;

    /**
     * 标题
     */
    private TextView tv_guanggaoxiangqing_title;

    /**
     * 轮播图logo
     */
    private ImageView iv_guanggaoxiangqing_logo;
    /**
     * 联系电话
     */
    private TextView tv_guanggaoxiangqing_mobile;
    /**
     * 发布者Logo
     */
    private CircleImageView iv_guanggaoxiangqing_post_logo;
    /**
     * 分享金额
     */
    private TextView tv_guanggaoxiangqing_price;
    private Context mContext;
    /**
     * 分享
     */
    private LinearLayout ll_guanggaoxiangqing_fenxiang;
    private WeChatShareUtil weChatShareUtil;
    boolean result = true;
    //分享码
    private String sCode = "";
    private SharedPreferences pref;
    private String sUser_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_guanggaoxiangqing);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        mContext = this;
        sId = getIntent().getStringExtra("id");
        ll_guanggaoxiangqing_back = findViewById(R.id.ll_guanggaoxiangqing_back);
        tv_guanggaoxiangqing_hint = findViewById(R.id.tv_guanggaoxiangqing_hint);
        tv_guanggaoxiangqing_end_time = findViewById(R.id.tv_guanggaoxiangqing_end_time);
        tv_guanggaoxiangqing_title = findViewById(R.id.tv_guanggaoxiangqing_title);
        iv_guanggaoxiangqing_logo = findViewById(R.id.iv_guanggaoxiangqing_logo);
        tv_guanggaoxiangqing_mobile = findViewById(R.id.tv_guanggaoxiangqing_mobile);
        iv_guanggaoxiangqing_post_logo = findViewById(R.id.iv_guanggaoxiangqing_post_logo);
        tv_guanggaoxiangqing_price = findViewById(R.id.tv_guanggaoxiangqing_price);
        weChatShareUtil = WeChatShareUtil.getInstance(this);
        ll_guanggaoxiangqing_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_guanggaoxiangqing_fenxiang = findViewById(R.id.ll_guanggaoxiangqing_fenxiang);
        ll_guanggaoxiangqing_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWeixinAvilible(mContext)) {
                    if (weChatShareUtil.isSupportWX()) {
                        String text = sCode;
                        result = weChatShareUtil.shareText(text, SendMessageToWX.Req.WXSceneSession);
                    } else {
                        Toast.makeText(GuanggaoxiangqingActivity.this, "手机上微信版本不支持分享到朋友圈", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mContext, "您还没有安装微信，请先安装微信客户端", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sAdvertisingInfo();
    }


    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 方法名：sAdvertisingInfo()
     * 功  能：广告详情接口
     * 参  数：appId
     * advertising_id--	广告id
     */
    private void sAdvertisingInfo() {
        String url = Api.sUrl + Api.sAdvertisingInfo;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("advertising_id", sId);
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
                            JSONObject jsonObject = new JSONObject(sDate);
                            String resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");
                            if (resultCode > 0) {

                                JSONObject jsonObjectdata = jsonObject.getJSONObject("data");
                                //标题
                                String sTitle = jsonObjectdata.getString("title");
                                tv_guanggaoxiangqing_hint.setText(sTitle);
                                tv_guanggaoxiangqing_title.setText(sTitle);
                                //轮播图url
                                String sLogo = jsonObjectdata.getString("logo");
                                int screenwidth = getScreenWidth(); //获取屏幕的宽度
                                ViewGroup.LayoutParams layoutParams = iv_guanggaoxiangqing_logo.getLayoutParams();//获取banner组件的参数
                                Glide.with(GuanggaoxiangqingActivity.this)
                                        .load( Api.sUrl+ sLogo)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .skipMemoryCache(true)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                iv_guanggaoxiangqing_logo.setImageBitmap(resource);
                                                //比例
                                                double bl = 0;
                                                bl = Jjcc.div(Double.valueOf(resource.getHeight()), Double.valueOf(resource.getWidth()));
                                                //比例
                                                double gao = 0;
                                                gao = Jjcc.mul(Double.valueOf(screenwidth), bl);
                                                int iGao = (new Double(gao)).intValue();
                                                layoutParams.height = iGao; //这里设置轮播图的长度等于宽度
                                                iv_guanggaoxiangqing_logo.setLayoutParams(layoutParams); //设置参数
                                            }
                                        });
                                //分享码
                                sCode = jsonObjectdata.getString("code");
                                //结束时间
                                String sEnd_Time = jsonObjectdata.getString("end_time");
                                //分享金额
                                String sPrice = jsonObjectdata.getString("price");
                                tv_guanggaoxiangqing_price.setText(sPrice + "元/人");
                                tv_guanggaoxiangqing_end_time.setText("结束时间：" + sEnd_Time);
                                //发布者图片
                                String sPost_Logo = jsonObjectdata.getString("post_logo");

                                Glide.with(GuanggaoxiangqingActivity.this)
                                        .load( Api.sUrl+ sPost_Logo)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(iv_guanggaoxiangqing_post_logo);

                                //联系电话
                                String sMobile = jsonObjectdata.getString("mobile");
                                tv_guanggaoxiangqing_mobile.setText(sMobile);
                                //详情数据
                                JSONArray jsonArray = jsonObjectdata.getJSONArray("intro");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                                    String ItemLogo = jsonObject2.getString("logo");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("logo", ItemLogo);
                                    mylist.add(map);
                                }

                                LinearLayout llGroup = (LinearLayout) findViewById(R.id.ll_guanggaoxiangqing);

                                //size:代码中获取到的图片数量

                                llGroup.removeAllViews();  //clear linearlayout
                                for (int i = 0; i < mylist.size(); i++) {
                                    final ImageView imageView = new ImageView(GuanggaoxiangqingActivity.this);
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));  //设置图片宽高
                                    //    imageView.setImageResource(R.drawable.ic_launcher); //图片资源
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    imageView.setAdjustViewBounds(true);
                                    Glide.with(GuanggaoxiangqingActivity.this)
                                            .load( Api.sUrl+ mylist.get(i).get("logo"))
                                            .asBitmap()
                                            .placeholder(R.mipmap.error)
                                            .error(R.mipmap.error)
                                            .skipMemoryCache(true)
                                            .into(new SimpleTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                    imageView.setImageBitmap(resource);
                                                }
                                            });
                                    llGroup.addView(imageView); //动态添加图片
                                }
                            } else {
                                hideDialogin();
                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        //   Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener()

        {
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
        loadingDialog = new LoadingDialog(GuanggaoxiangqingActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GuanggaoxiangqingActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
