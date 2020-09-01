package com.example.administrator.yunyue.erci.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
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
import com.example.administrator.yunyue.sc.WXPayUtils;
import com.example.administrator.yunyue.sc_activity.XjshdzActivity;
import com.example.administrator.yunyue.sc_activity.ZffsActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CwhyActivity extends AppCompatActivity {
    private static final String TAG = GrzlActivity.class.getSimpleName();
    /**
     * 会员
     */
    private LinearLayout ll_cwhy_back;
    /**
     * 手机号
     */
    private EditText et_cwhy_phone;
    /**
     * 姓名
     */
    private EditText et_cwhy_name;
    /**
     * 身份证号
     */
    private EditText et_cwhy_id_number;
    /**
     * 确认支付
     */
    private TextView tv_cwhy_qrzf;
    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 会员价格
     */
    private String sPrice = "9999";
    /**
     * 地址
     */
    public static String address = "";
    private TextView tv_cwhy_dw;
    private LinearLayout ll_cwhy_dw;

    MyAdapterlist myAdapterlist;
    /**
     * 省--0
     * 市--1
     * 区--2
     */
    private int iType = 0;
    private String itemid = "";
    private String sCitylist = "";
    private String sheng = "", shi = "", qu = "";
    private String sDz = "";

    RequestQueue queue = null;
    ListView lv_dialog;
    LinearLayout ll_ssq_sheng;
    LinearLayout ll_ssq_shi;
    LinearLayout ll_ssq_qu;
    TextView tv_ssq_sheng;
    TextView tv_ssq_shi;
    TextView tv_ssq_qu;
    TextView tv_ssq_sheng_xhx;
    TextView tv_ssq_shi_xhx;
    TextView tv_ssq_qu_xhx;
    private Dialog dialog;
    private View inflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_cwhy);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        queue = Volley.newRequestQueue(CwhyActivity.this);
        ll_cwhy_back = findViewById(R.id.ll_cwhy_back);
        ll_cwhy_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_cwhy_phone = findViewById(R.id.et_cwhy_phone);
        et_cwhy_name = findViewById(R.id.et_cwhy_name);
        et_cwhy_id_number = findViewById(R.id.et_cwhy_id_number);
        tv_cwhy_qrzf = findViewById(R.id.tv_cwhy_qrzf);
        tv_cwhy_dw = findViewById(R.id.tv_cwhy_dw);
        tv_cwhy_dw.setText(address);
        ll_cwhy_dw = findViewById(R.id.ll_cwhy_dw);
        myAdapterlist = new MyAdapterlist(CwhyActivity.this);
        ll_cwhy_dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDz = tv_cwhy_dw.getText().toString();
                iType = 0;
                hideDialogin();
                dialogin("");
                myAdapterlist.option = "sheng";
                huoqu("0", "1");
            }
        });
        tv_cwhy_qrzf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sPhone = et_cwhy_phone.getText().toString();
                String sName = et_cwhy_name.getText().toString();
                String sId_number = et_cwhy_id_number.getText().toString();
                if (sPhone.equals("")) {
                    Hint("请先输入手机号！", HintDialog.WARM);
                } else if (sName.equals("")) {
                    Hint("请先输入姓名！", HintDialog.WARM);
                } else if (sName.equals("")) {
                    Hint("请先输入身份证号！", HintDialog.WARM);
                } else {
                    hideDialogin();
                    dialogin("");
                    sVipadd(sName, sPhone, sId_number, sPrice);
                }
            }
        });
        show();
    }

    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.list_item, null);
        lv_dialog = (ListView) inflate.findViewById(R.id.lv_dialog);
        ImageView iv_shdz_gb = inflate.findViewById(R.id.iv_shdz_gb);
        ll_ssq_sheng = inflate.findViewById(R.id.ll_ssq_sheng);
        ll_ssq_shi = inflate.findViewById(R.id.ll_ssq_shi);
        ll_ssq_qu = inflate.findViewById(R.id.ll_ssq_qu);
        tv_ssq_sheng = inflate.findViewById(R.id.tv_ssq_sheng);
        tv_ssq_shi = inflate.findViewById(R.id.tv_ssq_shi);
        tv_ssq_qu = inflate.findViewById(R.id.tv_ssq_qu);
        tv_ssq_sheng_xhx = inflate.findViewById(R.id.tv_ssq_sheng_xhx);
        tv_ssq_shi_xhx = inflate.findViewById(R.id.tv_ssq_shi_xhx);
        tv_ssq_qu_xhx = inflate.findViewById(R.id.tv_ssq_qu_xhx);
        iv_shdz_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_cwhy_dw.setText(sDz);
                dialog.dismiss();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //  WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setAttributes(lp);
    }


    private void huoqu(String city, String level) {
        String url = Api.sUrl + "Api/Order/cityList/appId/" + Api.sApp_Id
                + "/city/" + city + "/level/" + level;
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

                        myAdapterlist.arrListIdsheng = new ArrayList<String>();
                        myAdapterlist.arrlistNamesheng = new ArrayList<String>();

                        JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultId = jsonObject.getString("id");
                            String resultName = jsonObject.getString("name");
                            myAdapterlist.arrListIdsheng.add(resultId);
                            myAdapterlist.arrlistNamesheng.add(resultName);

                        }
                        lv_dialog.setAdapter(myAdapterlist);
                        myAdapterlist.notifyDataSetChanged();
                        dialog.show();
                        if (iType == 0) {
                            iType = 1;
                        } else if (iType == 1) {
                            iType = 2;
                        } else if (iType == 2) {
                            iType = 3;
                        }

                    } else {
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
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

    private class MyAdapterlist extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<String> arrListIdsheng;
        public ArrayList<String> arrlistNamesheng;
        public ArrayList<String> arrListIdshi;
        public ArrayList<String> arrlistNameshi;
        public ArrayList<String> arrListIdqu;
        public ArrayList<String> arrlistNamequ;
        public String option = "";

        //public ArrayList<String> arr;

        public MyAdapterlist(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrListIdsheng = new ArrayList<String>();
            arrlistNamesheng = new ArrayList<String>();
            arrListIdshi = new ArrayList<String>();
            arrlistNameshi = new ArrayList<String>();
            arrListIdqu = new ArrayList<String>();
            arrlistNamequ = new ArrayList<String>();

        }

        @Override
        public int getCount() {
            int returen = 0;
            // TODO Auto-generated method stub
            if (myAdapterlist.option.equals("sheng")) {
                returen = arrListIdsheng.size();
            } else if (myAdapterlist.option.equals("shi")) {
                returen = arrListIdshi.size();
            } else if (myAdapterlist.option.equals("qu")) {
                returen = arrListIdqu.size();
            }
            return returen;//arrListIdqu.size();
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
                view = inflater.inflate(R.layout.listview_item, null);
            }
            TextView tv_lv_dialog_id = (TextView) view.findViewById(R.id.tv_lv_dialog_id);
            TextView tv_lv_dialog_name = (TextView) view.findViewById(R.id.tv_lv_dialog_name);
            tv_lv_dialog_id.setText(arrListIdsheng.get(position));
            tv_lv_dialog_name.setText(arrlistNamesheng.get(position));
            tv_lv_dialog_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    address = arrlistNamesheng.get(position);
                    tv_cwhy_dw.setText(arrlistNamesheng.get(position));
                    if (iType == 1) {
                        sCitylist = arrListIdsheng.get(position);
                        ll_ssq_shi.setVisibility(View.VISIBLE);
                        tv_ssq_sheng.setText(arrlistNamesheng.get(position));
                        sheng = arrlistNamesheng.get(position);
                        tv_ssq_sheng.setTextColor(tv_ssq_sheng.getResources().getColor(R.color.black));
                        tv_ssq_sheng_xhx.setVisibility(View.GONE);
                        itemid = arrListIdsheng.get(position);

                        dialogin("");
                        huoqu(itemid, String.valueOf(iType + 1));
                    } else if (iType == 2) {
                        sCitylist = sCitylist + "-" + arrListIdsheng.get(position);
                        ll_ssq_qu.setVisibility(View.VISIBLE);
                        tv_ssq_shi.setText(arrlistNamesheng.get(position));
                        shi = arrlistNamesheng.get(position);
                        tv_ssq_shi.setTextColor(tv_ssq_shi.getResources().getColor(R.color.black));
                        tv_ssq_shi_xhx.setVisibility(View.GONE);
                        itemid = arrListIdsheng.get(position);
                        dialogin("");
                        huoqu(itemid, String.valueOf(iType + 1));
                    } else if (iType == 3) {
                        sCitylist = sCitylist + "-" + arrListIdsheng.get(position);
                        tv_ssq_qu.setText(arrlistNamesheng.get(position));
                        qu = arrlistNamesheng.get(position);
                        tv_ssq_qu.setTextColor(tv_ssq_qu.getResources().getColor(R.color.black));
                        tv_ssq_qu_xhx.setVisibility(View.GONE);
                        itemid = arrListIdsheng.get(position);
                        dialog.dismiss();
                    }
                    /*      tv_xzdz_qu.setText(arrlistNamequ.get(position));*/

                }
            });
            return view;
        }
    }

    /**
     * 方法名：sVipadd()
     * 功  能：开通会员
     * 参  数：appId
     */
    private void sVipadd(String sUser_name, String sPhone, String sIdentity, String sPrice) {
        String url = Api.sUrl + Api.sVipadd;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("phone", sPhone);
        params.put("identity", sIdentity);
        params.put("user_name", sUser_name);
        params.put("price", sPrice);
        params.put("address", address);
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
                                        .build().toWXPayNotSign(CwhyActivity.this);
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
                //  Hint(error.getMessage(), HintDialog.ERROR);
                error(error);
            }
        });
        requestQueue.add(jsonRequest);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(CwhyActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(CwhyActivity.this, R.style.dialog, sHint, type, true).show();
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
