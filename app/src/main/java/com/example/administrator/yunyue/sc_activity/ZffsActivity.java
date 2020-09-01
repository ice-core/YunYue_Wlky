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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.administrator.yunyue.MainActivity_Wlky;
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

public class ZffsActivity extends AppCompatActivity {
    private static final String TAG = ZffsActivity.class.getSimpleName();
    RequestQueue queue = null;
    private CheckBox cb_zffs_ye, cb_zffs_wx, cb_zffs_yl, cb_zffs_zfb;
    private SharedPreferences pref;
    private String sUser_id;
    private LinearLayout ll_zffs_zf;
    private String sDataid;
    private ImageView iv_zffs_back;
    private String sCb = "";
    private EditText et_zhifu_shuru;
    private Dialog dialog;
    private String sMoney;
    private String sActivity;
    private static final int SDK_PAY_FLAG = 1;
    private String resultMsg = "";
    private String sRid = "";
    private String sUserNote = "";
    /**
     * 返回
     */
    private LinearLayout ll_zffs_back;
    /**
     * 是否是从商品详情进入购物车
     * 0--否
     * 1--是
     */
    public static int iIs_spxq = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_zffs);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(ZffsActivity.this);
        Intent intent = getIntent();
        sDataid = intent.getStringExtra("data");

        sMoney = intent.getStringExtra("money");
        sActivity = intent.getStringExtra("activity");

        sRid = intent.getStringExtra("rid");
        sUserNote = intent.getStringExtra("user_note");

        cb_zffs_ye = (CheckBox) findViewById(R.id.cb_zffs_ye);
        cb_zffs_wx = (CheckBox) findViewById(R.id.cb_zffs_wx);
        cb_zffs_yl = (CheckBox) findViewById(R.id.cb_zffs_yl);
        cb_zffs_zfb = (CheckBox) findViewById(R.id.cb_zffs_zfb);
        ll_zffs_zf = (LinearLayout) findViewById(R.id.ll_zffs_zf);

        iv_zffs_back = (ImageView) findViewById(R.id.iv_zffs_back);
        iv_zffs_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        ll_zffs_back = findViewById(R.id.ll_zffs_back);
        ll_zffs_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        sCb = "wxpay";
        cb_zffs_ye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sCb = "qbpay";
                if (cb_zffs_ye.isChecked()) {
                    cb_zffs_ye.setChecked(true);
                    cb_zffs_wx.setChecked(false);
                    cb_zffs_yl.setChecked(false);
                    cb_zffs_zfb.setChecked(false);
                }
            }
        });
        cb_zffs_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sCb = "wxpay";
                if (cb_zffs_wx.isChecked()) {
                    cb_zffs_wx.setChecked(true);
                    cb_zffs_ye.setChecked(false);
                    cb_zffs_yl.setChecked(false);
                    cb_zffs_zfb.setChecked(false);
                }
            }
        });
        cb_zffs_yl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sCb = "ylpay";
                if (cb_zffs_yl.isChecked()) {
                    cb_zffs_yl.setChecked(true);
                    cb_zffs_ye.setChecked(false);
                    cb_zffs_wx.setChecked(false);
                    cb_zffs_zfb.setChecked(false);
                }
            }
        });
        cb_zffs_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sCb = "zfbpay";
                if (cb_zffs_zfb.isChecked()) {
                    cb_zffs_zfb.setChecked(true);
                    cb_zffs_ye.setChecked(false);
                    cb_zffs_wx.setChecked(false);
                    cb_zffs_yl.setChecked(false);
                }
            }
        });
        ll_zffs_zf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sCb.equals("qbpay")) {
                    dialog();
                } else if (sCb.equals("wxpay")) {
                    if (sRid == null) {
                        zhifu();
                    } else if (sRid.equals("")) {
                        zhifu();
                    } else {
                        zhifuxx(sMoney, sRid, sCb, sUserNote, "");
                    }
                } else if (sCb.equals("zfbpay")) {
                    if (sRid == null) {
                        zhifu();
                    } else if (sRid.equals("")) {
                        zhifu();
                    } else {
                        zhifuxx(sMoney, sRid, sCb, sUserNote, "");
                    }
                }


            }
        });
    }

    public void dialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
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
        tv_zhifu_hint.setText("支付（元）");
        ImageView iv_zhifu_type = inflate.findViewById(R.id.iv_zhifu_type);
        TextView tv_zhifu_type = inflate.findViewById(R.id.tv_zhifu_type);
        if (sCb.equals("qbpay")) {
            tv_zhifu_type.setText("余额");
            iv_zhifu_type.setImageResource(R.mipmap.yur);
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

        Glide.with(ZffsActivity.this)
                .load( Api.sUrl+ pref.getString("head_pic", ""))
                .asBitmap()
                .placeholder(R.mipmap.error)
                .error(R.mipmap.error)
                .dontAnimate()
                .into(iv_zhifu_pic);
        tv_zhifu_deposit.setText(sMoney);
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
                        // zhifu(sDataid, sCb, String.valueOf(editable));
                        if (sRid == null) {
                            zhifu();
                        } else if (sRid.equals("")) {
                            zhifu();
                        } else {
                            zhifuxx(sMoney, sRid, sCb, sUserNote, String.valueOf(editable));
                        }

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

    private void zhifuchenggong() {
        Intent intent = new Intent(ZffsActivity.this, ZfcgActivity.class);
        startActivity(intent);
        finish();
    }

    private void zhifu(String order_id, final String paytype, String paypwd) {
        String url = Api.sUrl + "Order/orderPay/";
        RequestQueue requestQueue = Volley.newRequestQueue(ZffsActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sUser_id);
        params.put("order_id", order_id);
        params.put("paytype", paytype);
        if (paytype.equals("qbpay")) {
            params.put("paypwd", paypwd);
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
                    /*        resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");*/
                            if (resultCode > 0) {
                                if (paytype.equals("zfbpay")) {
                                    final String orderInfo = jsonObject1.getString("data");
                                    final Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {
                                            PayTask alipay = new PayTask(ZffsActivity.this);
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
                                } else if (paytype.equals("wxpay")) {
                                    String sData = jsonObject1.getString("data");
                                    JSONObject jsonObjectData = new JSONObject(sData);
                                    String sAppid = jsonObjectData.getString("appid");
                                    String sPartnerid = jsonObjectData.getString("partnerid");
                                    String sPrepayid = jsonObjectData.getString("prepayid");
                                    String sPackage = jsonObjectData.getString("package");
                                    String sNoncestr = jsonObjectData.getString("noncestr");
                                    String sTimestamp = jsonObjectData.getString("timestamp");
                                    String sSign = jsonObjectData.getString("sign");

                                    WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                                    builder.setAppId(sAppid)
                                            .setPartnerId(sPartnerid)
                                            .setPrepayId(sPrepayid)
                                            .setPackageValue("Sign=WXPay")
                                            .setNonceStr(sNoncestr)
                                            .setTimeStamp(sTimestamp)
                                            .setSign(sSign)
                                            .build().toWXPayNotSign(ZffsActivity.this);
                                } else {
                                    // dialog.dismiss();
                                    Hint(resultMsg);

                                }
                            } else {
                                // et_zhifu_shuru.setText("");
                                Hint(resultMsg, HintDialog.ERROR);
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
                Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
            }
        });
        requestQueue.add(jsonRequest);
    }

    private void zhifu() {
        String url = Api.sUrl + "Api/order/orderpay/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/order_list/" + sDataid + "/paytype/wxpay";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    jsonObject = new JSONObject(sDate);
                    resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    /*        resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");*/
                    if (resultCode > 0) {
                        String sData = jsonObject.getString("data");
                        JSONObject jsonObjectData = new JSONObject(sData);
                        String sAppid = jsonObjectData.getString("appid");
                        String sPartnerid = jsonObjectData.getString("partnerid");
                        String sPrepayid = jsonObjectData.getString("prepayid");
                        String sPackage = jsonObjectData.getString("package");
                        String sNoncestr = jsonObjectData.getString("noncestr");
                        String sTimestamp = jsonObjectData.getString("timestamp");
                        String sSign = jsonObjectData.getString("sign");

                        WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                        builder.setAppId(sAppid)
                                .setPartnerId(sPartnerid)
                                .setPrepayId(sPrepayid)
                                .setPackageValue("Sign=WXPay")
                                .setNonceStr(sNoncestr)
                                .setTimeStamp(sTimestamp)
                                .setSign(sSign)
                                .build().toWXPayNotSign(ZffsActivity.this);

                    } else {
                        //  et_zhifu_shuru.setText("");
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


    private void zhifuxx(String amount, String rid, final String paytype, String user_note, String paypwd) {
        String url = Api.sUrl + "Order/offlinePay/";
        RequestQueue requestQueue = Volley.newRequestQueue(ZffsActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", sUser_id);
        params.put("amount", amount);
        params.put("rid", rid);
        params.put("paytype", paytype);
        params.put("user_note", user_note);
        if (paytype.equals("qbpay")) {
            params.put("paypwd", paypwd);
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
                    /*        resultMsg = jsonObject.getString("msg");
                            int resultCode = jsonObject.getInt("code");*/
                            if (resultCode > 0) {
                                if (paytype.equals("zfbpay")) {
                                    final String orderInfo = jsonObject1.getString("data");
                                    final Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {
                                            PayTask alipay = new PayTask(ZffsActivity.this);
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
                                } else if (paytype.equals("wxpay")) {
                                    String sData = jsonObject1.getString("data");
                                    JSONObject jsonObjectData = new JSONObject(sData);
                                    String sAppid = jsonObjectData.getString("appid");
                                    String sPartnerid = jsonObjectData.getString("partnerid");
                                    String sPrepayid = jsonObjectData.getString("prepayid");
                                    String sPackage = jsonObjectData.getString("package");
                                    String sNoncestr = jsonObjectData.getString("noncestr");
                                    String sTimestamp = jsonObjectData.getString("timestamp");
                                    String sSign = jsonObjectData.getString("sign");

                                    WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
                                    builder.setAppId(sAppid)
                                            .setPartnerId(sPartnerid)
                                            .setPrepayId(sPrepayid)
                                            .setPackageValue("Sign=WXPay")
                                            .setNonceStr(sNoncestr)
                                            .setTimeStamp(sTimestamp)
                                            .setSign(sSign)
                                            .build().toWXPayNotSign(ZffsActivity.this);
                                } else {
                                    // dialog.dismiss();
                                    Hint(resultMsg);

                                }
                            } else {
                               // et_zhifu_shuru.setText("");
                                Hint(resultMsg, HintDialog.ERROR);
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
                Log.e(TAG, error.getMessage(), error);
                Hint(error.getMessage(), HintDialog.ERROR);
            }
        });
        requestQueue.add(jsonRequest);
    }


/*    private void zhifu(String order_id, final String paytype, String paypwd) {
        String url = Api.sUrl + "Order/orderPay/user_id/" + sUser_id + "/order_id/" + order_id + "/paytype/" + paytype + "/paypwd/" + paypwd;

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {
                        dialog.dismiss();
                        if (paytype.equals("zfbpay")) {
                            final String orderInfo = jsonObject.getString("data");
                            final Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(ZffsActivity.this);
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
                        } else if (paytype.equals("wxpay")) {
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
                                    .setPackageValue("Sign=WXPay")
                                    .setNonceStr(sNoncestr)
                                    .setTimeStamp(sTimestamp)
                                    .setSign(sSign)
                                    .build().toWXPayNotSign(ZffsActivity.this);
                        } else {
                            Hint(resultMsg);

                        }
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
                et_zhifu_shuru.setText("");
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
                zhifuchenggong();
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
        if (iIs_spxq == 0) {
            if (sActivity == null) {
            } else if (sActivity.equals("txdd")) {
                Intent intent = new Intent(ZffsActivity.this, MainActivity_Wlky.class);
                intent.putExtra("ID", "0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
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
        loadingDialog = new LoadingDialog(ZffsActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(ZffsActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
