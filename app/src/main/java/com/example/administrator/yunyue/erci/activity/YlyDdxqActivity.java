package com.example.administrator.yunyue.erci.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
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
import android.widget.CheckBox;
import android.widget.GridView;
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
import com.example.administrator.yunyue.sc_activity.Sc_WdddActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YlyDdxqActivity extends AppCompatActivity {
    private static final String TAG = YlyDdxqActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_yly_ddxq_back;
    private SharedPreferences pref;
    private String sUser_id = "";
    private String sOrder_id = "";
    /**
     * 状态
     */
    private ImageView iv_yly_ddxq_status;
    private TextView tv_yly_ddxq_status;
    /**
     * 入驻码
     */
    private TextView tv_yly_ddxq_check_in_code;
    /**
     * 养老院
     */
    private TextView tv_yly_ddxq_nursing_homes_name;
    /**
     * 地址
     */
    private TextView tv_yly_ddxq_address;
    /**
     * 图
     */
    private ImageView iv_yly_ddxq_logo;
    /**
     * 房间名称
     */
    private TextView tv_yly_ddxq_title;
    /**
     * 房间描述
     */
    private TextView tv_yly_ddxq_presentation;
    /**
     * 房间价格
     */
    private TextView tv_yly_ddxq_price;
    /**
     * 定金
     */
    private TextView tv_yly_ddxq_deposit;
    private TextView tv_yly_ddxq_deposit1;
    /**
     * 入住时间
     */
    private TextView tv_yly_ddxq_expire;
    /**
     * 入住人
     */
    private TextView tv_yly_ddxq_lodger_name;
    /**
     * 身份证号
     */
    private TextView tv_yly_ddxq_id_number;
    /**
     * 联系人
     */
    private TextView tv_yly_ddxq_linkman;
    /**
     * 预计到院时间
     */
    private TextView tv_yly_ddxq_predict_time;
    /**
     * 描述
     */
    private TextView tv_yly_ddxq_remark;
    /**
     * 订单编号
     */
    private TextView tv_yly_ddxq_serial_num;
    /**
     * 创建时间
     */
    private TextView tv_yly_ddxq_add_time;
    /**
     * 付款时间
     */
    private TextView tv_yly_ddxq_payment_time;
    /**
     * 养老院
     */
    private LinearLayout tv_yly_ddxq_yly;
    /**
     * 房间
     */
    private LinearLayout ll_yly_ddxq_fj;
    //养老院联系电话
    private String resulthomephone = "";
    //养老院id
    private String resultrest_id = "";
    //房间id
    private String resultroom_id = "";
    /**
     * 养老院联系电话
     */
    private LinearLayout ll_yly_ddxq_homephone;
    /**
     * 取消订单
     */
    private TextView tv_yly_ddxq_qxdd;
    private LinearLayout ll_yly_ddxq_qxdd;

    private Dialog dialog_qxyy;
    private View inflate_qxyy;
    private GridView gv_tkyy;
    private String sTkyy = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_yly_ddxq);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sOrder_id = getIntent().getStringExtra("id");
        ll_yly_ddxq_back = findViewById(R.id.ll_yly_ddxq_back);
        ll_yly_ddxq_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_yly_ddxq_status = findViewById(R.id.iv_yly_ddxq_status);
        tv_yly_ddxq_status = findViewById(R.id.tv_yly_ddxq_status);
        tv_yly_ddxq_check_in_code = findViewById(R.id.tv_yly_ddxq_check_in_code);
        tv_yly_ddxq_nursing_homes_name = findViewById(R.id.tv_yly_ddxq_nursing_homes_name);
        tv_yly_ddxq_address = findViewById(R.id.tv_yly_ddxq_address);
        iv_yly_ddxq_logo = findViewById(R.id.iv_yly_ddxq_logo);
        tv_yly_ddxq_title = findViewById(R.id.tv_yly_ddxq_title);
        tv_yly_ddxq_presentation = findViewById(R.id.tv_yly_ddxq_presentation);
        tv_yly_ddxq_price = findViewById(R.id.tv_yly_ddxq_price);
        tv_yly_ddxq_deposit = findViewById(R.id.tv_yly_ddxq_deposit);
        tv_yly_ddxq_deposit1 = findViewById(R.id.tv_yly_ddxq_deposit1);
        tv_yly_ddxq_expire = findViewById(R.id.tv_yly_ddxq_expire);
        tv_yly_ddxq_lodger_name = findViewById(R.id.tv_yly_ddxq_lodger_name);
        tv_yly_ddxq_id_number = findViewById(R.id.tv_yly_ddxq_id_number);
        tv_yly_ddxq_linkman = findViewById(R.id.tv_yly_ddxq_linkman);
        tv_yly_ddxq_predict_time = findViewById(R.id.tv_yly_ddxq_predict_time);
        tv_yly_ddxq_remark = findViewById(R.id.tv_yly_ddxq_remark);
        tv_yly_ddxq_serial_num = findViewById(R.id.tv_yly_ddxq_serial_num);
        tv_yly_ddxq_add_time = findViewById(R.id.tv_yly_ddxq_add_time);
        tv_yly_ddxq_payment_time = findViewById(R.id.tv_yly_ddxq_payment_time);
        tv_yly_ddxq_yly = findViewById(R.id.tv_yly_ddxq_yly);
        tv_yly_ddxq_qxdd = findViewById(R.id.tv_yly_ddxq_qxdd);
        ll_yly_ddxq_qxdd = findViewById(R.id.ll_yly_ddxq_qxdd);
        tv_yly_ddxq_yly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YlyDdxqActivity.this, YlyxqActivity.class);
                intent.putExtra("id", resultrest_id);
                startActivity(intent);
            }
        });
        ll_yly_ddxq_fj = findViewById(R.id.ll_yly_ddxq_fj);
        ll_yly_ddxq_fj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YlyDdxqActivity.this, FangjianxiangqingActivity.class);
                intent.putExtra("id", resultroom_id);
                startActivity(intent);
            }
        });
        ll_yly_ddxq_homephone = findViewById(R.id.ll_yly_ddxq_homephone);
        ll_yly_ddxq_homephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(resulthomephone);
            }
        });
        tv_yly_ddxq_qxdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sRefund();
            }
        });
        sRestHomeOrderInfo();
        initialize();
    }


    public void show() {
        dialog_qxyy.setContentView(inflate_qxyy);
        Window dialogWindow = dialog_qxyy.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog_qxyy.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        dialog_qxyy.getWindow().setAttributes(lp);
        dialog_qxyy.show();
    }

    private void initialize() {
        dialog_qxyy = new Dialog(YlyDdxqActivity.this, R.style.ActionSheetDialogStyle);
        inflate_qxyy = LayoutInflater.from(YlyDdxqActivity.this).inflate(R.layout.tkyy_dialog, null);
        TextView tv_tkyy_gb = (TextView) inflate_qxyy.findViewById(R.id.tv_tkyy_gb);
        gv_tkyy = inflate_qxyy.findViewById(R.id.gv_tkyy);
        tv_tkyy_gb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_qxyy.dismiss();
            }
        });
    }

    /**
     * 方法名：sRefund()
     * 功  能：退款原因
     * 参  数：appId
     */
    private void sRefund() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sRefund;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
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
                                JSONArray resultJsonArraytype = jsonObject1.getJSONArray("data");
                                MyAdapterTkyy myAdapterTkyy = new MyAdapterTkyy(YlyDdxqActivity.this);
                                ArrayList<HashMap<String, String>> mylist_type = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArraytype.length(); i++) {
                                    jsonObject1 = resultJsonArraytype.getJSONObject(i);
                                    String resultId = jsonObject1.getString("id");
                                    String resultName = jsonObject1.getString("name");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemId", resultId);
                                    map.put("ItemName", resultName);
                                    myAdapterTkyy.arr_Cb.add("0");
                                    mylist_type.add(map);
                                }
                                myAdapterTkyy.arrlist = mylist_type;
                                gv_tkyy.setAdapter(myAdapterTkyy);
                                show();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        // Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
    }


    /**
     * 方法名：sAddRefund()
     * 功  能：养老院订单 申请退款
     * 参  数：appId
     */
    private void sAddRefund(String sReasons) {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sAddRefund;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_id);
        params.put("reasons", sReasons);
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
                                String resultData = jsonObject1.getString("data");
                                Hint(resultMsg, HintDialog.SUCCESS);
                                sRestHomeOrderInfo();
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "response -> " + response.toString());
                        // Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                //Hint(error.getMessage(), HintDialog.ERROR);
                error(error);

            }
        });
        requestQueue.add(jsonRequest);
    }

    private class MyAdapterTkyy extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;
        public ArrayList<String> arr_Cb;

        public MyAdapterTkyy(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<HashMap<String, String>>();
            arr_Cb = new ArrayList<String>();

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
                view = inflater.inflate(R.layout.tkyy_dialog_item, null);
            }
            final TextView tv_tkyy_dialog_item_name = view.findViewById(R.id.tv_tkyy_dialog_item_name);
            final CheckBox cb_tkyy_dialog_item = view.findViewById(R.id.cb_tkyy_dialog_item);
            cb_tkyy_dialog_item.setVisibility(View.GONE);
            tv_tkyy_dialog_item_name.setText(arrlist.get(position).get("ItemName"));
            tv_tkyy_dialog_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sTkyy = arrlist.get(position).get("ItemId");
                    dialog_qxyy.dismiss();
                    sAddRefund( sTkyy);

                }
            });

            return view;
        }
    }


    public void dialog(final String resultPhone) {
        Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.lxmj_dialog, null);
        final TextView tv_lxmj_dialog = inflate.findViewById(R.id.tv_lxmj_dialog);
        tv_lxmj_dialog.setText(resultPhone);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
        tv_lxmj_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diallPhone(resultPhone);
            }
        });
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 方法名：sRestHomeOrderInfo()
     * 功  能：养老院订单详情页面接口
     * 参  数：appId
     */
    private void sRestHomeOrderInfo() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sRestHomeOrderInfo;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("order_id", sOrder_id);
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
                                JSONObject jsonObject1 = new JSONObject(sDate.toString()).getJSONObject("data");
                                //1为待支付 2为可入住 3为取消订单 4售后
                                String resultstatus = jsonObject1.getString("status");
                                if (resultstatus.equals("2")) {
                                    ll_yly_ddxq_qxdd.setVisibility(View.VISIBLE);
                                    tv_yly_ddxq_qxdd.setVisibility(View.VISIBLE);
                                    iv_yly_ddxq_status.setVisibility(View.VISIBLE);
                                    tv_yly_ddxq_status.setText("可入住");
                                } else if (resultstatus.equals("3")) {
                                    ll_yly_ddxq_qxdd.setVisibility(View.GONE);
                                    tv_yly_ddxq_qxdd.setVisibility(View.GONE);
                                    iv_yly_ddxq_status.setVisibility(View.GONE);
                                    tv_yly_ddxq_status.setText("已取消");
                                } else if (resultstatus.equals("4")) {
                                    ll_yly_ddxq_qxdd.setVisibility(View.GONE);
                                    tv_yly_ddxq_qxdd.setVisibility(View.GONE);
                                    iv_yly_ddxq_status.setVisibility(View.GONE);
                                    tv_yly_ddxq_status.setText("售后");
                                }
                                //入住码
                                String resultcheck_in_code = jsonObject1.getString("check_in_code");
                                tv_yly_ddxq_check_in_code.setText("入驻码：" + resultcheck_in_code);
                                //养老院id
                                resultrest_id = jsonObject1.getString("rest_id");
                                //养老院名称
                                String resultnursing_homes_name = jsonObject1.getString("nursing_homes_name");
                                tv_yly_ddxq_nursing_homes_name.setText(resultnursing_homes_name);
                                //养老院地址
                                String resultaddress = jsonObject1.getString("address");
                                tv_yly_ddxq_address.setText(resultaddress);
                                //房间id
                                resultroom_id = jsonObject1.getString("room_id");
                                //房间名称
                                String resulttitle = jsonObject1.getString("title");
                                tv_yly_ddxq_title.setText(resulttitle);
                                //房间描述
                                String resultpresentation = jsonObject1.getString("presentation");
                                tv_yly_ddxq_presentation.setText(resultpresentation);
                                //房间价格
                                String resultprice = jsonObject1.getString("price");
                                tv_yly_ddxq_price.setText("￥" + resultprice);
                                //房间图片
                                String resultlogo = jsonObject1.getString("logo");
                                Glide.with(YlyDdxqActivity.this)
                                        .load( Api.sUrl+resultlogo)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(iv_yly_ddxq_logo);
                                //房间定金
                                String resultdeposit = jsonObject1.getString("deposit");
                                tv_yly_ddxq_deposit.setText(resultdeposit + "工分");
                                tv_yly_ddxq_deposit1.setText(resultdeposit + "工分");
                                //入住时间
                                String resultexpire = jsonObject1.getString("expire");
                                tv_yly_ddxq_expire.setText(resultexpire);
                                //入住人姓名
                                String resultlodger_name = jsonObject1.getString("lodger_name");
                                tv_yly_ddxq_lodger_name.setText(resultlodger_name);
                                //身份证号
                                String resultid_number = jsonObject1.getString("id_number");
                                tv_yly_ddxq_id_number.setText(resultid_number);
                                //联系人名称
                                String resultlinkman = jsonObject1.getString("linkman");
                                tv_yly_ddxq_linkman.setText(resultlinkman);
                                //联系人手机号
                                String resultlinkphone = jsonObject1.getString("linkphone");
                                //到院时间
                                String resultpredict_time = jsonObject1.getString("predict_time");
                                tv_yly_ddxq_predict_time.setText(resultpredict_time);
                                //描述
                                String resultremark = jsonObject1.getString("remark");
                                tv_yly_ddxq_remark.setText(resultremark);
                                //订单编号
                                String resultserial_num = jsonObject1.getString("serial_num");
                                tv_yly_ddxq_serial_num.setText(resultserial_num);
                                //创建时间
                                String resultadd_time = jsonObject1.getString("add_time");
                                tv_yly_ddxq_add_time.setText(resultadd_time);
                                //支付时间
                                String resultpayment_time = jsonObject1.getString("payment_time");
                                tv_yly_ddxq_payment_time.setText(resultpayment_time);
                                //养老院联系电话
                                resulthomephone = jsonObject1.getString("homephone");


                            } else {
                                //  Hint(resultMsg, HintDialog.ERROR);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(YlyDdxqActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(YlyDdxqActivity.this, R.style.dialog, sHint, type, true).show();
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
