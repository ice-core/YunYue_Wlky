package com.example.administrator.yunyue.sc_activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CkddActivity extends AppCompatActivity {
    private static final String TAG = CkddActivity.class.getSimpleName();
    private ImageView iv_ckdd_back;
    private TextView tv_ckdd_hint;
    private TextView tv_ckdd_lxmj;
    private TextView tv_ckdd_ckwl;
    private TextView tv_ckdd_qrsh;
    private String sActivity = "";
    RequestQueue queue = null;
    private String sUser_id;
    private SharedPreferences pref;
    private String sId;
    private TextView tv_ckdd_storename,
            tv_ckdd_coupon_price, tv_ckdd_order_amount_yf, tv_ckdd_order_sn,
            tv_ckdd_add_time, tv_ckdd_pay_time;

    private String resultPhone = "";
    private TextView tv_ckdd_name;
    private TextView tv_ckdd_mobile;
    private TextView tv_ckdd_dizhi;
    private MyGridView gv_ckdd_sp;
    private String sShippingCode = "";
    private TextView tv_ckdd_paytype;
    private TextView tv_ckdd_distribution;
    private TextView tv_ckdd_freight;
    private TextView tv_ckdd_pay;
    private LinearLayout ll_ckdd_lxkf;
    private TextView tv_ckdd_type;
    private TextView tv_ckdd_title;
    private TextView tv_ckdd_content;
    private TextView tv_ckdd_taxpayer;
    private MyGridView mgv_ckdd_wntj;
    private LinearLayout ll_ckdd_fp;
    private TextView tv_ckdd_state;
    private String kuaidi = "";
    //店铺
    private LinearLayout ll_ckdd_storename;

    /**
     * 删除订单
     */
    private TextView tv_ckdd_scdd;
    MyAdapter myAdapter;
    /**
     * 查看物流
     */
    private LinearLayout ll_ckdd_wl;
    //实付金额
    private String pay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_ckdd);
        queue = Volley.newRequestQueue(CkddActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        Intent intent = getIntent();
        sActivity = intent.getStringExtra("activity");
        sId = intent.getStringExtra("id");

        tv_ckdd_hint = (TextView) findViewById(R.id.tv_ckdd_hint);
        iv_ckdd_back = (ImageView) findViewById(R.id.iv_ckdd_back);
        tv_ckdd_lxmj = (TextView) findViewById(R.id.tv_ckdd_lxmj);
        tv_ckdd_ckwl = (TextView) findViewById(R.id.tv_ckdd_ckwl);
        tv_ckdd_qrsh = (TextView) findViewById(R.id.tv_ckdd_qrsh);
        tv_ckdd_name = (TextView) findViewById(R.id.tv_ckdd_name);
        tv_ckdd_mobile = (TextView) findViewById(R.id.tv_ckdd_mobile);
        tv_ckdd_dizhi = (TextView) findViewById(R.id.tv_ckdd_dizhi);
        tv_ckdd_state = findViewById(R.id.tv_ckdd_state);

        tv_ckdd_storename = (TextView) findViewById(R.id.tv_ckdd_storename);
        tv_ckdd_freight = findViewById(R.id.tv_ckdd_freight);
        tv_ckdd_coupon_price = (TextView) findViewById(R.id.tv_ckdd_coupon_price);
        tv_ckdd_order_amount_yf = (TextView) findViewById(R.id.tv_ckdd_order_amount_yf);
        tv_ckdd_order_sn = (TextView) findViewById(R.id.tv_ckdd_order_sn);
        tv_ckdd_add_time = (TextView) findViewById(R.id.tv_ckdd_add_time);
        tv_ckdd_pay_time = (TextView) findViewById(R.id.tv_ckdd_pay_time);
        tv_ckdd_paytype = findViewById(R.id.tv_ckdd_paytype);
        tv_ckdd_distribution = findViewById(R.id.tv_ckdd_distribution);
        tv_ckdd_pay = findViewById(R.id.tv_ckdd_pay);
        ll_ckdd_lxkf = findViewById(R.id.ll_ckdd_lxkf);
        tv_ckdd_type = findViewById(R.id.tv_ckdd_type);
        tv_ckdd_title = findViewById(R.id.tv_ckdd_title);
        tv_ckdd_content = findViewById(R.id.tv_ckdd_content);
        tv_ckdd_taxpayer = findViewById(R.id.tv_ckdd_taxpayer);
        tv_ckdd_scdd = findViewById(R.id.tv_ckdd_scdd);

        ll_ckdd_fp = findViewById(R.id.ll_ckdd_fp);
        mgv_ckdd_wntj = findViewById(R.id.mgv_ckdd_wntj);
        ll_ckdd_storename = findViewById(R.id.ll_ckdd_storename);

        gv_ckdd_sp = findViewById(R.id.gv_ckdd_sp);
        ll_ckdd_wl = findViewById(R.id.ll_ckdd_wl);
        ll_ckdd_wl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CkddActivity.this, CkwlActivity.class);
                intent.putExtra("dh", kuaidi);
                startActivity(intent);
            }
        });
        iv_ckdd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        tv_ckdd_lxmj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(resultPhone);
            }
        });
        tv_ckdd_qrsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_ckdd_qrsh.getText().toString().equals("立即付款")) {
                    Intent intent = new Intent(CkddActivity.this, ZffsActivity.class);
                    intent.putExtra("activity", "wddd");
                    intent.putExtra("data", sId);
                    intent.putExtra("money", pay);
                    startActivity(intent);
                } else if (tv_ckdd_qrsh.getText().toString().equals("确认收货")) {
                    hideDialogin();
                    dialogin("");
                    queren(sId);
                } else if (tv_ckdd_qrsh.getText().toString().equals("评论商品")) {
                    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                    Intent intent = new Intent(CkddActivity.this, PinglunActivity.class);

                    PinglunActivity.list = myAdapter.arrlist;
                    intent.putExtra("id", sId);
                    startActivity(intent);
                }
            }
        });
        tv_ckdd_ckwl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CkddActivity.this, CkwlActivity.class);
                intent.putExtra("dh", kuaidi);
                startActivity(intent);
            }
        });
        ll_ckdd_lxkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog(resultPhone);
            }
        });
        hideDialogin();
        dialogin("");
        huoqu();
        tv_ckdd_scdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDialogin();
                dialogin("");
                Delete(sId);
            }
        });
    }

    private void Delete(String order_id) {
        String url = Api.sUrl + "Api/Order/orderdel/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/order_id/" + order_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {
                        Hint(resultMsg);
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

    private void jianjei(final String pid) {
        String url = Api.sUrl + "Api/Good/gooddetail/appId/" + Api.sApp_Id
                + "/good_id/" + pid + "/user_id/" + sUser_id;
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
                        Intent intent1 = new Intent(CkddActivity.this, SpxqActivity.class);
                        intent1.putExtra("goods_id", pid);
                        intent1.putExtra("data", sDate);
                        startActivity(intent1);
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
                back();
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

    private void queren(String sId) {
        String url = Api.sUrl + "Api/Order/shouhuo/appId/" + Api.sApp_Id
                + "/order_id/" + sId + "/user_id/" + sUser_id;
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
                        Hint(resultMsg);
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
        queue.add(request);
    }

    /**
     * 获取商品详情
     */
    private void huoqu() {
        String url = Api.sUrl + "Api/Order/orderdetail/appId/" + Api.sApp_Id
                + "/user_id/" + sUser_id + "/order_id/" + sId;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideDialogin();
                String sDate = jsonObject.toString();
                System.out.println(sDate);
                try {
                    jsonObject = new JSONObject(sDate);
                    String resultMsg = jsonObject.getString("msg");
                    int resultCode = jsonObject.getInt("code");
                    if (resultCode > 0) {
                        JSONObject jsonObjectdata = new JSONObject(jsonObject.getString("data"));
                        JSONObject jsonObjectdetails = jsonObjectdata.getJSONObject("detail");
                        int state = jsonObjectdetails.getInt("state");
                        int id = jsonObjectdetails.getInt("id");
                        kuaidi = jsonObjectdetails.getString("kuaidi");
                        //商家id
                        String shangjia_id = jsonObjectdetails.getString("shangjia_id");
                        ll_ckdd_storename.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(CkddActivity.this, DpzyActivity.class);
                                intent.putExtra("id", shangjia_id);
                                startActivity(intent);
                            }
                        });
                        if (state == 1) {
                            tv_ckdd_hint.setText("待付款");
                            tv_ckdd_state.setText("待付款");
                            //联系卖家
                            tv_ckdd_lxmj.setVisibility(View.VISIBLE);
                            //查看物流
                            tv_ckdd_ckwl.setVisibility(View.GONE);
                            ll_ckdd_wl.setEnabled(false);
                            //确认收货
                            tv_ckdd_qrsh.setVisibility(View.VISIBLE);
                            tv_ckdd_qrsh.setText("立即付款");
                            //支付时间
                            tv_ckdd_pay_time.setVisibility(View.GONE);

                        } else if (state == 2) {
                            tv_ckdd_hint.setText("待发货 ");
                            tv_ckdd_state.setText("待发货 ");
                            tv_ckdd_scdd.setVisibility(View.GONE);
                            //联系卖家
                            tv_ckdd_lxmj.setVisibility(View.VISIBLE);
                            //查看物流
                            tv_ckdd_ckwl.setVisibility(View.GONE);
                            ll_ckdd_wl.setEnabled(false);
                            //确认收货
                            tv_ckdd_qrsh.setVisibility(View.GONE);
                            //支付时间
                            tv_ckdd_pay_time.setVisibility(View.VISIBLE);
                        } else if (state == 3) {
                            tv_ckdd_hint.setText("待收货 ");
                            tv_ckdd_state.setText("待收货 ");
                            tv_ckdd_scdd.setVisibility(View.GONE);
                            //联系卖家
                            tv_ckdd_lxmj.setVisibility(View.VISIBLE);
                            //查看物流
                            tv_ckdd_ckwl.setVisibility(View.VISIBLE);
                            ll_ckdd_wl.setEnabled(true);
                            //确认收货
                            tv_ckdd_qrsh.setVisibility(View.VISIBLE);
                            //支付时间
                            tv_ckdd_pay_time.setVisibility(View.VISIBLE);
                        } else if (state == 4) {
                            tv_ckdd_hint.setText("待评价");
                            tv_ckdd_state.setText("待评价");
                            //联系卖家
                            tv_ckdd_lxmj.setVisibility(View.VISIBLE);
                            //查看物流
                            tv_ckdd_ckwl.setVisibility(View.GONE);
                            ll_ckdd_wl.setEnabled(false);
                            //确认收货
                            tv_ckdd_qrsh.setVisibility(View.VISIBLE);
                            tv_ckdd_qrsh.setText("评论商品");
                            //支付时间
                            tv_ckdd_pay_time.setVisibility(View.VISIBLE);
                        } else if (state == 5) {
                            tv_ckdd_hint.setText("交易成功");
                            tv_ckdd_state.setText("交易成功");
                            //联系卖家
                            tv_ckdd_lxmj.setVisibility(View.VISIBLE);
                            //查看物流
                            tv_ckdd_ckwl.setVisibility(View.VISIBLE);
                            ll_ckdd_wl.setEnabled(true);
                            //确认收货
                            tv_ckdd_qrsh.setVisibility(View.GONE);
                            //支付时间
                            tv_ckdd_pay_time.setVisibility(View.VISIBLE);
                        }

                        //商家名称
                        String shangjia_name = jsonObjectdetails.getString("shangjia_name");
                        tv_ckdd_storename.setText(shangjia_name);
                        //订单编号
                        String order_sn = jsonObjectdetails.getString("order_no");
                        //下单时间
                        String time = jsonObjectdetails.getString("start_time");
                        //支付方式
                        //  String paytype = jsonObjectdata.getString("paytype");
                        //支付时间
                        String pay_time = jsonObjectdetails.getString("pay_time");
                        //商品总金额
                        String total_amount = jsonObjectdetails.getString("new_price");
                        //实付金额
                        pay = jsonObjectdetails.getString("totle_price");
                        //用户电话
                        String mobile = jsonObjectdetails.getString("mobile");
                        //用户名称
                        String consignee = jsonObjectdetails.getString("user_name");
                        //用户地址
                        String address = jsonObjectdetails.getString("address");
                        //商家电话
                        resultPhone = jsonObjectdetails.getString("lianxiphone");

                        tv_ckdd_order_sn.setText(order_sn);
                        tv_ckdd_add_time.setText(time);
                        tv_ckdd_paytype.setText("微信支付");
                        tv_ckdd_pay_time.setText(pay_time);
                        // tv_ckdd_distribution.setText(distribution);
                        tv_ckdd_name.setText(consignee);
                        tv_ckdd_mobile.setText(mobile);
                        tv_ckdd_dizhi.setText("地址:" + address);
                        tv_ckdd_order_amount_yf.setText("￥" + total_amount);
                    /*    if (state == 1) {
                            tv_ckdd_pay.setText("￥0.00");
                        } else {*/
                        //tv_ckdd_pay.setText("￥" + total_amount);
                        tv_ckdd_pay.setText("￥" + pay);
                        //  }
                        JSONArray resultJsonArrayList = jsonObjectdetails.getJSONArray("list");
                        myAdapter = new MyAdapter(CkddActivity.this);
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArrayList.length(); i++) {
                            JSONObject jsonObjectResultData = resultJsonArrayList.getJSONObject(i);
                            //商品id
                            String resultGoods_id = jsonObjectResultData.getString("goods_id");
                            //商品名称
                            String resultGoods_name = jsonObjectResultData.getString("goods_name");
                            //商品数量
                            String resultGoods_num = jsonObjectResultData.getString("goods_num");
                            //支付价格
                            String resultFinal_price = jsonObjectResultData.getString("final_price");
                            //规格
                            String resultSpec_key_name = jsonObjectResultData.getString("spec_key_name");
                            //商品图片
                            String resultGood_pic = jsonObjectResultData.getString("good_pic");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("ItemGood_id", resultGoods_id);
                            map.put("ItemGood_pic", resultGood_pic);
                            map.put("ItemGoods_Name", resultGoods_name);
                            map.put("ItemSpec_Sey_Name", resultSpec_key_name);
                            map.put("ItemGoods_Price", resultFinal_price);
                            map.put("ItemGoods_Num", resultGoods_num);
                            map.put("ItemShangjia_id", shangjia_id);
                            mylist.add(map);
                        }
                        myAdapter.arrlist = mylist;
                        gv_ckdd_sp.setAdapter(myAdapter);


                        JSONArray resultJsonArraytuijian = jsonObjectdata.getJSONArray("tuijian");
                        MyAdapterWntj myAdapterWntj = new MyAdapterWntj(CkddActivity.this);
                        ArrayList<HashMap<String, String>> mylisttuijian = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArraytuijian.length(); i++) {
                            JSONObject jsonObjectResulttuijian = resultJsonArraytuijian.getJSONObject(i);
                            //商品id
                            String resultId = jsonObjectResulttuijian.getString("id");
                            //商品名称
                            String resultName = jsonObjectResulttuijian.getString("name");
                            //商品价格
                            String resultPrice = jsonObjectResulttuijian.getString("price");
                            //商品图片
                            String resultLogo = jsonObjectResulttuijian.getString("logo");
                            //店铺名称
                            String resultShangjianame = jsonObjectResulttuijian.getString("shangjianame");
                            //店铺ID
                            String resultShangjia_id = jsonObjectResulttuijian.getString("shangjia_id");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", resultId);
                            map.put("name", resultName);
                            map.put("price", resultPrice);
                            map.put("logo", resultLogo);
                            map.put("shangjianame", resultShangjianame);
                            map.put("shangjia_id", resultShangjia_id);
                            mylisttuijian.add(map);
                        }
                        myAdapterWntj.arrlist = mylisttuijian;
                        mgv_ckdd_wntj.setAdapter(myAdapterWntj);
                    } else {
                        hideDialogin();
                        Hint(resultMsg, HintDialog.ERROR);
                        //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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
        queue.add(request);
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
        loadingDialog = new LoadingDialog(CkddActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(CkddActivity.this, R.style.dialog, sHint, type, true).show();
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            new ArrayList<HashMap<String, String>>();


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
                view = inflater.inflate(R.layout.ckdd_item, null);
            }
            LinearLayout ll_ckdd_item = view.findViewById(R.id.ll_ckdd_item);
            ll_ckdd_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogin();
                    dialogin("");
                    jianjei(arrlist.get(position).get("ItemGood_id"));
                }
            });
            ImageView iv_ckdd_image = view.findViewById(R.id.iv_ckdd_image);
            TextView tv_ckdd_goods_name = view.findViewById(R.id.tv_ckdd_goods_name);
            TextView tv_ckdd_spec_key_name = view.findViewById(R.id.tv_ckdd_spec_key_name);
            TextView tv_ckdd_order_amount = view.findViewById(R.id.tv_ckdd_order_amount);
            TextView tv_ckdd_goods_num = view.findViewById(R.id.tv_ckdd_goods_num);
            Glide.with(CkddActivity.this)
                    .load( Api.sUrl+ arrlist.get(position).get("ItemGood_pic"))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_ckdd_image);
            tv_ckdd_goods_name.setText(arrlist.get(position).get("ItemGoods_Name"));
            tv_ckdd_spec_key_name.setText(arrlist.get(position).get("ItemSpec_Sey_Name"));
            tv_ckdd_order_amount.setText("￥" + arrlist.get(position).get("ItemGoods_Price"));
            tv_ckdd_goods_num.setText("数量：" + arrlist.get(position).get("ItemGoods_Num"));


            return view;
        }
    }


    private class MyAdapterWntj extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        ArrayList<HashMap<String, String>> arrlist;


        public MyAdapterWntj(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrlist = new ArrayList<HashMap<String, String>>();


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
                view = inflater.inflate(R.layout.ckdd_wntj_item, null);
            }

            LinearLayout ll_splb_item = view.findViewById(R.id.ll_splb_item);
            ImageView iv_splb = view.findViewById(R.id.iv_splb);
            TextView tv_splb = view.findViewById(R.id.tv_splb);
            TextView tv_splb_q = view.findViewById(R.id.tv_splb_q);
            TextView tv_splb_shangjianame = view.findViewById(R.id.tv_splb_shangjianame);
            Glide.with(CkddActivity.this).load( Api.sUrl+Api.sUrl
                    + arrlist.get(position).get("logo").toString())
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_splb);

            /*  map.put("id", resultId);
                map.put("name", resultName);
                map.put("price", resultPrice);
                map.put("logo", resultLogo);
                map.put("shangjianame", resultShangjianame);
                map.put("buy_count", resultBuy_count);*/
            //viewHolder.iv_sc_y.setBackgroundResource(myList.get(position).get("iv_sc_y").hashCode());
            tv_splb.setText(arrlist.get(position).get("name").toString());
            tv_splb_q.setText(arrlist.get(position).get("price").toString());
            ll_splb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideDialogin();
                    dialogin("");
                    jianjei(arrlist.get(position).get("id").toString());
                }
            });
            tv_splb_shangjianame.setText(arrlist.get(position).get("shangjianame").toString());
            return view;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        huoqu();
    }
}
