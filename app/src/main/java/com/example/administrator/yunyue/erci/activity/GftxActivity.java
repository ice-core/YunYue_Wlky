package com.example.administrator.yunyue.erci.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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

public class GftxActivity extends AppCompatActivity {
    private static final String TAG = GftxActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_tixian_back;
    /**
     * 手机号
     */
    private EditText et_tixian_account;

    /**
     * 提现金额
     */
    private EditText et_tixian_price;
    /**
     * 下一步
     */
    private TextView tv_tixian_xyb;
    private SharedPreferences pref;
    private String sUser_id;

    private EditText et_zhifu_shuru;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_gftx);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_tixian_back = findViewById(R.id.ll_tixian_back);
        et_tixian_account = findViewById(R.id.et_tixian_account);
        et_tixian_price = findViewById(R.id.et_tixian_price);
        ll_tixian_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_tixian_xyb = findViewById(R.id.tv_tixian_xyb);
        tv_tixian_xyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAccount = et_tixian_account.getText().toString();
                String sPrice = et_tixian_price.getText().toString();
                if (sAccount.equals("")) {
                    Hint("支付宝账号手机号不能为空！", HintDialog.WARM);
                } else if (sPrice.equals("")) {
                    Hint("提现金额为空！", HintDialog.WARM);
                } else {
                    dialog();
                }
            }
        });
    }

    public void dialog() {
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.zhifu_item, null);
        TextView tv_zhifu_hint = inflate.findViewById(R.id.tv_zhifu_hint);
        final ImageView iv_zhifu_close = inflate.findViewById(R.id.iv_zhifu_close);
        final ImageView iv_zhifu_pic = inflate.findViewById(R.id.iv_zhifu_pic);
        final TextView tv_zhifu_deposit = inflate.findViewById(R.id.tv_zhifu_deposit);
        et_zhifu_shuru = inflate.findViewById(R.id.et_zhifu_shuru);
        final TextView tv_zhifu_paypwd = inflate.findViewById(R.id.tv_zhifu_paypwd);
        final TextView tv_zhifu_paypwd1 = inflate.findViewById(R.id.tv_zhifu_paypwd1);
        final TextView tv_zhifu_paypwd2 = inflate.findViewById(R.id.tv_zhifu_paypwd2);
        final TextView tv_zhifu_paypwd3 = inflate.findViewById(R.id.tv_zhifu_paypwd3);
        final TextView tv_zhifu_paypwd4 = inflate.findViewById(R.id.tv_zhifu_paypwd4);
        final TextView tv_zhifu_paypwd5 = inflate.findViewById(R.id.tv_zhifu_paypwd5);
        tv_zhifu_hint.setText("提现（元）");
        ImageView iv_zhifu_type = inflate.findViewById(R.id.iv_zhifu_type);
        TextView tv_zhifu_type = inflate.findViewById(R.id.tv_zhifu_type);

        Glide.with(GftxActivity.this)
                .load( Api.sUrl+ pref.getString("head_pic", ""))
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_zhifu_pic);
        tv_zhifu_deposit.setText(et_tixian_price.getText().toString());
        iv_zhifu_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        et_zhifu_shuru.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int a = editable.length();
                tv_zhifu_paypwd.setText("");
                tv_zhifu_paypwd1.setText("");
                tv_zhifu_paypwd2.setText("");
                tv_zhifu_paypwd3.setText("");
                tv_zhifu_paypwd4.setText("");
                tv_zhifu_paypwd5.setText("");
                switch (editable.length()) {
                    case 6:
                        tv_zhifu_paypwd5.setText(editable.subSequence(5, 6));
                        tv_zhifu_paypwd5.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        dialogin("");
                        sTixina(String.valueOf(editable));
                    case 5:
                        tv_zhifu_paypwd4.setText(editable.subSequence(4, 5));
                        tv_zhifu_paypwd4.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    case 4:
                        tv_zhifu_paypwd3.setText(editable.subSequence(3, 4));
                        tv_zhifu_paypwd3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    case 3:
                        tv_zhifu_paypwd2.setText(editable.subSequence(2, 3));
                        tv_zhifu_paypwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    case 2:
                        tv_zhifu_paypwd1.setText(editable.subSequence(1, 2));
                        tv_zhifu_paypwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    case 1:
                        tv_zhifu_paypwd.setText(editable.subSequence(0, 1));
                        tv_zhifu_paypwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    default:
                        break;
                }
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }


    /**
     * 方法名：sTixina()
     * 功  能：提现接口
     * 参  数：appId
     * type--1为佣金 2为推广
     * price--提现金额
     * account--支付宝账号或手机号
     * paypwd--支付密码
     */
    private void sTixina(String sPaypwd) {
        String url = Api.sUrl + Api.sTixina;
        RequestQueue requestQueue = Volley.newRequestQueue(GftxActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("price", et_tixian_price.getText().toString());
        params.put("account", et_tixian_account.getText().toString());
        params.put("paypwd", sPaypwd);
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

                                Hint(resultMsg, HintDialog.SUCCESS);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1200);
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                                et_zhifu_shuru.setText("");
                            }
                        } catch (JSONException e) {
                            hideDialogin();
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


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(GftxActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GftxActivity.this, R.style.dialog, sHint, type, true).show();
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
}

