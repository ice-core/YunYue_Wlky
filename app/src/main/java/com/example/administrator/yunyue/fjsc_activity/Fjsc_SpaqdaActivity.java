package com.example.administrator.yunyue.fjsc_activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fjsc_SpaqdaActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_SpaqdaActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_spaqda_back;
    private String sShangjia_id = "";
    /**
     * 商家名称
     */
    private TextView tv_fjsc_spaqda_shangjianame;
    /**
     * 许可证号
     */
    private TextView tv_fjsc_spaqda_lesence;
    /**
     * 商家地址
     */
    private TextView tv_fjsc_spaqda_address;
    /**
     * 经营范围
     */
    private TextView tv_fjsc_spaqda_type_name;
    /**
     * 有效期
     */
    private TextView tv_fjsc_spaqda_indate;
    /**
     * 营业许可证
     */
    private ImageView iv_fjsc_spaqda_xukezheng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__spaqda);
        sShangjia_id = getIntent().getStringExtra("shangjia_id");
        ll_fjsc_spaqda_back = findViewById(R.id.ll_fjsc_spaqda_back);
        ll_fjsc_spaqda_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_fjsc_spaqda_shangjianame = findViewById(R.id.tv_fjsc_spaqda_shangjianame);
        tv_fjsc_spaqda_lesence = findViewById(R.id.tv_fjsc_spaqda_lesence);
        tv_fjsc_spaqda_address = findViewById(R.id.tv_fjsc_spaqda_address);
        tv_fjsc_spaqda_type_name = findViewById(R.id.tv_fjsc_spaqda_type_name);
        tv_fjsc_spaqda_indate = findViewById(R.id.tv_fjsc_spaqda_indate);
        iv_fjsc_spaqda_xukezheng = findViewById(R.id.iv_fjsc_spaqda_xukezheng);
        sShangjiadetail();
    }

    /**
     * 方法名：sShangjiadetail()
     * 功  能：商家详情
     * 参  数：appId
     * shangjia_id--商家ID
     */
    private void sShangjiadetail() {
        String url = Api.sUrl + Api.sShangjiadetail;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("shangjia_id", sShangjia_id);
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
                                JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                                //商家名称
                                String shangjianame = jsonObjectdata.getString("shangjianame");
                                tv_fjsc_spaqda_shangjianame.setText(shangjianame);
                                //许可证号
                                String lesence = jsonObjectdata.getString("lesence");
                                tv_fjsc_spaqda_lesence.setText(lesence);
                                //商家地址
                                String address = jsonObjectdata.getString("address");
                                tv_fjsc_spaqda_address.setText(address);
                                //商家分类
                                String type_name = jsonObjectdata.getString("type_name");
                                tv_fjsc_spaqda_type_name.setText(type_name);

                                //许可证有效期
                                String indate = jsonObjectdata.getString("indate");
                                tv_fjsc_spaqda_indate.setText(indate);
                                //	许可证图片
                                String xukezheng = jsonObjectdata.getString("xukezheng");

                                Glide.with(Fjsc_SpaqdaActivity.this)
                                        .load( Api.sUrl+ xukezheng)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(iv_fjsc_spaqda_xukezheng);

                            } else {
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
        loadingDialog = new LoadingDialog(Fjsc_SpaqdaActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_SpaqdaActivity.this, R.style.dialog, sHint, type, true).show();
    }

}
