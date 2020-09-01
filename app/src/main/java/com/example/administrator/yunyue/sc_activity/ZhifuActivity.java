package com.example.administrator.yunyue.sc_activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import com.example.administrator.yunyue.sc.PayResult;
import com.example.administrator.yunyue.sc.WXPayUtils;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ZhifuActivity extends AppCompatActivity {
    private static final String TAG = ZhifuActivity.class.getSimpleName();
    private CheckBox cb_zhifu_ye, cb_zhifu_wx,cb_zhifu_zfb;
    private ImageView iv_zhifu_back;
    private String sResidence_id = "";
    RequestQueue queue = null;
    private String sUser_id = "";
    private SharedPreferences pref;
    private TextView tv_zhifu_deposit;
    // android.app.AlertDialog dialog;
    private Button bt_zhifu_zhifu;
    private String sCb = "";
    private String resultDeposit = "";
    private EditText et_zhifu_shuru;
    private Dialog dialog;
    private static final int SDK_PAY_FLAG = 1;
    private String resultMsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_zhifu);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(ZhifuActivity.this);
        Intent intent = getIntent();
        sResidence_id = intent.getStringExtra("residence_id");
        cb_zhifu_ye = (CheckBox) findViewById(R.id.cb_zhifu_ye);
        cb_zhifu_wx = (CheckBox) findViewById(R.id.cb_zhifu_wx);

        cb_zhifu_zfb = (CheckBox) findViewById(R.id.cb_zhifu_zfb);
        iv_zhifu_back = (ImageView) findViewById(R.id.iv_zhifu_back);
        tv_zhifu_deposit = (TextView) findViewById(R.id.tv_zhifu_deposit);
        bt_zhifu_zhifu = (Button) findViewById(R.id.bt_zhifu_zhifu);
        iv_zhifu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        sCb = "qbpay";
        cb_zhifu_ye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_zhifu_ye.isChecked()) {
                    cb_zhifu_wx.setChecked(false);

                    cb_zhifu_zfb.setChecked(false);
                    sCb = "qbpay";
                }
            }
        });
        cb_zhifu_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_zhifu_wx.isChecked()) {
                    cb_zhifu_ye.setChecked(false);

                    cb_zhifu_zfb.setChecked(false);
                    sCb = "wxpay";
                }
            }
        });

        cb_zhifu_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_zhifu_zfb.isChecked()) {
                    cb_zhifu_ye.setChecked(false);
                    cb_zhifu_wx.setChecked(false);
                    sCb = "zfbpay";
                }
            }
        });
        bt_zhifu_zhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });
        dialogin("");
        huoqu();
    }


    public void dialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.zhifu_item, null);
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
        ImageView iv_zhifu_type = inflate.findViewById(R.id.iv_zhifu_type);
        TextView tv_zhifu_type = inflate.findViewById(R.id.tv_zhifu_type);
        if (sCb.equals("qbpay")) {
            tv_zhifu_type.setText("余额");
            iv_zhifu_type.setImageResource(R.mipmap.yue);
        } else if (sCb.equals("wxpay")) {
            tv_zhifu_type.setText("微信");
            iv_zhifu_type.setImageResource(R.mipmap.icon_wechat);
        } else if (sCb.equals("ylpay")) {
            tv_zhifu_type.setText("银联支付");
            iv_zhifu_type.setImageResource(R.mipmap.icon_yinlian);
        } else if (sCb.equals("zfbpay")) {
            tv_zhifu_type.setText("支付宝");
            iv_zhifu_type.setImageResource(R.mipmap.icon_zhifubao);
        }

        Glide.with(ZhifuActivity.this)
                .load( Api.sUrl+ pref.getString("head_pic", ""))
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_zhifu_pic);
        tv_zhifu_deposit.setText(resultDeposit);
        iv_zhifu_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialogin("");\
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
                        zhifudingjin(String.valueOf(editable), sCb);
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
     * 获取定金
     */
    private void huoqu() {
        String url = Api.sUrl + "Order/deposit/user_id/" + sUser_id + "/residence_id/" + sResidence_id;
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
                        String resultData = jsonObject1.getString("data");
                        JSONObject jsonObjectresultData = new JSONObject(resultData);
                        resultDeposit = jsonObjectresultData.getString("deposit");
                        tv_zhifu_deposit.setText(resultDeposit);

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
        queue.add(request);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //    showAlert(WyfActivity.this, getString(R.string.pay_success) + payResult);
                        Hint(resultMsg);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        // showAlert(WyfActivity.this, getString(R.string.pay_failed) + payResult);
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };


    /**
     * 支付定金
     */
    private void zhifudingjin(String sPaypwd, final String sPaytype) {
        String url = Api.sUrl + "Order/residencePay/";
        RequestQueue requestQueue = Volley.newRequestQueue(ZhifuActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sUser_id);
        params.put("residence_id", sResidence_id);
        params.put("paytype", sPaytype);
        if (sPaytype.equals("qbpay")) {
            params.put("paypwd", sPaypwd);
        }
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
                            resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                if (sPaytype.equals("zfbpay")) {
                                    final String orderInfo = jsonObject1.getString("data");
                                    final Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {
                                            PayTask alipay = new PayTask(ZhifuActivity.this);
                                            Map<String, String> result = alipay.payV2(orderInfo, true);
                                            Log.i("msp", result.toString());

                                            Message msg = new Message();
                                            msg.what = SDK_PAY_FLAG;
                                            msg.obj = result;
                                            mHandler.sendMessage(msg);
                                        }
                                    };

                                    // 必须异步调用
                                    Thread payThread = new Thread(payRunnable);
                                    payThread.start();
                                } else if (sPaytype.equals("wxpay")) {
                                    String sData = jsonObject1.getString("data");
                                    JSONObject jsonObjectData = new JSONObject(sData);
                                    String sAppid = jsonObjectData.getString("appid");
                                    //   String sPartnerid = jsonObjectData.getString("partnerid");
                                    String sPrepayid = jsonObjectData.getString("prepayid");
                                    String sPackage = jsonObjectData.getString("package");
                                    String sNoncestr = jsonObjectData.getString("noncestr");
                                    String sTimestamp = jsonObjectData.getString("timestamp");
                                    String sSign = jsonObjectData.getString("sign");

                                    WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                                    builder.setAppId(sAppid)
                                            .setPartnerId("1520574701")
                                            .setPrepayId(sPrepayid)
                                            .setPackageValue(sPackage)
                                            .setNonceStr(sNoncestr)
                                            .setTimeStamp(sTimestamp)
                                            .setSign(sSign)
                                            .build().toWXPayNotSign(ZhifuActivity.this);
                                } else {
                                    Hint(resultMsg);
                                }

                                dialog.dismiss();
                            } else {
                                et_zhifu_shuru.setText("");
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
                Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
            }
        });
        requestQueue.add(jsonRequest);
    }


    /**
     * 支付定金
     */
/*    private void zhifudingjin(String sPaypwd, final String sPaytype) {
        String url = Api.sUrl + "Order/residencePay/user_id/" + sUser_id + "/residence_id/" + sResidence_id + "/paypwd/" + sPaypwd + "/paytype/" + sPaytype;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    JSONObject jsonObject1 = new JSONObject(sDate);
                    resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        if (sPaytype.equals("zfbpay")) {
                            final String orderInfo = jsonObject.getString("data");
                            final Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(ZhifuActivity.this);
                                    Map<String, String> result = alipay.payV2(orderInfo, true);
                                    Log.i("msp", result.toString());

                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };

                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } else if (sPaytype.equals("wxpay")) {
                            String sData = jsonObject.getString("data");
                            JSONObject jsonObjectData = new JSONObject(sData);
                            String sAppid = jsonObjectData.getString("appid");
                            //   String sPartnerid = jsonObjectData.getString("partnerid");
                            String sPrepayid = jsonObjectData.getString("prepayid");
                            String sPackage = jsonObjectData.getString("package");
                            String sNoncestr = jsonObjectData.getString("noncestr");
                            String sTimestamp = jsonObjectData.getString("timestamp");
                            String sSign = jsonObjectData.getString("sign");

                            WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                            builder.setAppId(sAppid)
                                    .setPartnerId("1520574701")
                                    .setPrepayId(sPrepayid)
                                    .setPackageValue(sPackage)
                                    .setNonceStr(sNoncestr)
                                    .setTimeStamp(sTimestamp)
                                    .setSign(sSign)
                                    .build().toWXPayNotSign(ZhifuActivity.this);
                        } else {
                            Hint(resultMsg);
                        }

                        dialog.dismiss();
                    } else {
                        et_zhifu_shuru.setText("");
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
        queue.add(request);
    }*/
    public void Hint(String hint) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_hint, null);
        final ImageView iv_hint_dialog_img = inflate.findViewById(R.id.iv_hint_dialog_img);
        final TextView tv_hint_dialog_message = inflate.findViewById(R.id.tv_hint_dialog_message);
        TextView btn_hint_dialog_confirm = inflate.findViewById(R.id.btn_hint_dialog_confirm);
        iv_hint_dialog_img.setImageResource(R.drawable.success);
        tv_hint_dialog_message.setText(hint);
        btn_hint_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(ZhifuActivity.this, ScMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ID", "3");
                startActivity(intent);
                finish();
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


    private void back() {
        finish();
    }

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
        loadingDialog = new LoadingDialog(ZhifuActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ZhifuActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
