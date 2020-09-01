package com.example.administrator.yunyue.yjdt_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
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
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;

import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.bean.JsonBean;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc_gridview.MyGridView;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class BjzlActivity extends AppCompatActivity {
    private static final String TAG = BjzlActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_bjzl_back;
    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    private static final int REQUEST_CROP = 1;// 裁剪
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    private static final int REQUEST_PERMISSION = 100;

    private Uri imgUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri
    private boolean hasPermission = false;
    private File imgFile;// 拍照保存的图片文件
    private static final int COMPLETED = 0;
    private String resultUrl = "";

    /**
     * 用户头像
     */
    private ImageView iv_bjzl_logo;
    /**
     * 是否成为供应方
     */
    private Switch sc_bjzl_sfcwgyf;
    private TextView tv_bjzl_gyf_dz;
    private TextView tv_bjzl_gyf_fw;
    private String resultSupply_Region = "";
    private String resultSupply_Saas = "";
    /**
     * 是否成为需求方
     */
    private Switch sc_bjzl_sfcwxqf;
    private TextView tv_bjzl_xqf_dz;
    private TextView tv_bjzl_xqf_fw;
    private String resultNeed_Region = "";
    private String resultNeed_Saas = "";


    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    /**
     * 1--供应方
     * 2--需求方
     */
    private String sGX = "";
    MyAdapterlist myAdapterlist;

    private Dialog dialog;
    private View inflate;
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

    /**
     * 省--0
     * 市--1
     * 区--2
     */
    private int iType = 0;
    private String itemid = "";
    private String sSaas_gy = "";
    private String sSaas_xq = "";
    private String sheng = "", shi = "", qu = "";
    /**
     * 备注
     */
    private LinearLayout ll_bjzl_beizhu;
    /**
     * 发消息
     */
    private TextView tv_bjzl_fxx;
    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 被关注用户id
     */
    private String sFocus_id = "";
    //头像
    private TextView tv_bjzl_nickname;

    /**
     * 动态
     */
    private MyGridView mgv_bjzl_dt;
    /**
     * 保存
     */
    private TextView tv_bjzl_baocun;
    /**
     * 备注
     */
    private EditText et_bjzl_beizhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_bjzl);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sFocus_id = getIntent().getStringExtra("id");
        if (sFocus_id == null) {
            sFocus_id = "";
        }
        myAdapterlist = new MyAdapterlist(BjzlActivity.this);
        ll_bjzl_back = findViewById(R.id.ll_bjzl_back);
        ll_bjzl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_bjzl_logo = findViewById(R.id.iv_bjzl_logo);
        iv_bjzl_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog();
            }
        });
        tv_bjzl_nickname = findViewById(R.id.tv_bjzl_nickname);
        sc_bjzl_sfcwgyf = findViewById(R.id.sc_bjzl_sfcwgyf);
        tv_bjzl_gyf_dz = findViewById(R.id.tv_bjzl_gyf_dz);
        tv_bjzl_gyf_fw = findViewById(R.id.tv_bjzl_gyf_fw);

        sc_bjzl_sfcwxqf = findViewById(R.id.sc_bjzl_sfcwxqf);
        tv_bjzl_xqf_dz = findViewById(R.id.tv_bjzl_xqf_dz);
        tv_bjzl_xqf_fw = findViewById(R.id.tv_bjzl_xqf_fw);

        ll_bjzl_beizhu = findViewById(R.id.ll_bjzl_beizhu);
        et_bjzl_beizhu = findViewById(R.id.et_bjzl_beizhu);
        if (sFocus_id.equals("")) {
            ll_bjzl_beizhu.setVisibility(View.GONE);
        } else {
            ll_bjzl_beizhu.setVisibility(View.VISIBLE);
        }
        tv_bjzl_fxx = findViewById(R.id.tv_bjzl_fxx);
        tv_bjzl_fxx.setVisibility(View.GONE);
        mgv_bjzl_dt = findViewById(R.id.mgv_bjzl_dt);
        tv_bjzl_baocun = findViewById(R.id.tv_bjzl_baocun);
        tv_bjzl_baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is = false;
                String sSupply = "";
                if (sc_bjzl_sfcwgyf.isChecked()) {
                    if (!tv_bjzl_gyf_dz.getText().toString().equals("请选择地址")) {
                        if (!sSaas_gy.equals("")) {
                            is = true;
                            sSupply = "{\"address\":\"" + tv_bjzl_gyf_dz.getText().toString() + "\",\"saas\":\"" + sSaas_gy + "\"}";
                        } else {
                            Hint("请选择服务", HintDialog.WARM);
                            is = false;
                        }
                    } else {
                        Hint("请选择地址", HintDialog.WARM);
                        is = false;
                    }
                }

                String sNeed = "";
                if (sc_bjzl_sfcwxqf.isChecked()) {
                    if (!tv_bjzl_xqf_dz.getText().toString().equals("请选择地址")) {
                        if (!sSaas_xq.equals("")) {
                            is = true;
                            sNeed = "{\"address\":\"" + tv_bjzl_xqf_dz.getText().toString() + "\",\"saas\":\"" + sSaas_gy + "\"}";
                        } else {
                            Hint("请选择服务", HintDialog.WARM);
                            is = false;
                        }
                    } else {
                        Hint("请选择地址", HintDialog.WARM);
                        is = false;
                    }
                }
                if (is) {
                    String sName = "";
                    if (sFocus_id.equals("")) {
                        sName = "";
                    } else {
                        sName = et_bjzl_beizhu.getText().toString();
                    }
                    hideDialogin();
                    dialogin("");
                    sUserEditSave(sSupply, sNeed, sName);
                }
            }
        });
        initJsonData();
        show();
        tv_bjzl_gyf_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGX = "1";

                showPickerView();
            }
        });
        tv_bjzl_xqf_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGX = "2";

                showPickerView();
            }
        });
        tv_bjzl_gyf_fw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_bjzl_gyf_fw.setText("");
                sGX = "1";
                iType = 0;
                dialogin("");
                myAdapterlist.option = "sheng";
                sSaaSList("0", "1");
            }
        });
        tv_bjzl_xqf_fw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_bjzl_xqf_fw.setText("");
                sGX = "2";
                iType = 0;
                dialogin("");
                myAdapterlist.option = "sheng";
                sSaaSList("0", "1");
            }
        });
        sUserEdit();
        enabled();
    }


    /**
     * 方法名：sUserEditSave()
     * 功  能：编辑资料保存接口
     * 参  数：appId
     * focus_id--被关注用户id
     */
    private void sUserEditSave(String sSupply, String sNeed, String sName) {
        String url = Api.sUrl + Api.sUserEditSave;
        RequestQueue requestQueue = Volley.newRequestQueue(BjzlActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        if (sFocus_id.equals("")) {
        } else {
            params.put("focus_id", sFocus_id);
        }
        if (!sSupply.equals("")) {
            params.put("supply", sSupply);
        }
        if (!sNeed.equals("")) {
            params.put("need", sNeed);
        }
        if (!sName.equals("")) {
            params.put("name", sName);
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
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                Hint(resultMsg, HintDialog.SUCCESS);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       finish();
                                    }
                                }, 1000);
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


    /**
     * 方法名：sUserEdit()
     * 功  能：编辑资料接口
     * 参  数：appId
     * focus_id--被关注用户id
     */
    private void sUserEdit() {
        String url = Api.sUrl + Api.sUserEdit;
        RequestQueue requestQueue = Volley.newRequestQueue(BjzlActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        if (sFocus_id.equals("")) {
        } else {
            params.put("focus_id", sFocus_id);
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
                        try {
                            JSONObject jsonObject0 = new JSONObject(sDate);
                            String resultMsg = jsonObject0.getString("msg");
                            int resultCode = jsonObject0.getInt("code");
                            if (resultCode > 0) {
                                JSONObject jsonObjectdata = response.getJSONObject("data");
                                //被查看用户id
                                String resultId = jsonObjectdata.getString("id");
                                //名称
                                String resultNickname = jsonObjectdata.getString("nickname");
                                tv_bjzl_nickname.setText(resultNickname);
                                //备注名称
                                String resultName = jsonObjectdata.getString("name");
                                et_bjzl_beizhu.setText(resultName);
                                //头像
                                String resultHeadimgurl = jsonObjectdata.getString("headimgurl");
                                Glide.with(BjzlActivity.this)
                                        .load( Api.sUrl+ resultHeadimgurl)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(iv_bjzl_logo);

                                //供应方数据
                                JSONObject jsonObjectSupply = jsonObjectdata.getJSONObject("supply");
                                resultSupply_Region = jsonObjectSupply.getString("region");
                                resultSupply_Saas = jsonObjectSupply.getString("saas");
                                sSaas_gy = jsonObjectSupply.getString("saas_id");

                                //需求方数据
                                JSONObject jsonObjectNeed = jsonObjectdata.getJSONObject("need");
                                resultNeed_Region = jsonObjectNeed.getString("region");
                                resultNeed_Saas = jsonObjectNeed.getString("saas");
                                sSaas_xq = jsonObjectNeed.getString("saas_id");

                                ArrayList<String> arrayList = new ArrayList<>();
                                JSONArray jsonArrayImglist = jsonObjectdata.getJSONArray("dynamic");
                                for (int a = 0; a < jsonArrayImglist.length(); a++) {
                                    String imge = jsonArrayImglist.get(a).toString();
                                    arrayList.add(imge);
                                }
                                MyAdapter_Dynamic myAdapter_dynamic = new MyAdapter_Dynamic(BjzlActivity.this);
                                myAdapter_dynamic.arrayList = arrayList;
                                mgv_bjzl_dt.setAdapter(myAdapter_dynamic);
                                dz();
                                fw();
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

    /**
     * 禁止点击
     */
    private void enabled() {
        if (sFocus_id.equals("")) {
            sc_bjzl_sfcwgyf.setEnabled(true);
            tv_bjzl_gyf_dz.setEnabled(true);
            tv_bjzl_gyf_fw.setEnabled(true);
            sc_bjzl_sfcwxqf.setEnabled(true);
            tv_bjzl_xqf_dz.setEnabled(true);
            tv_bjzl_xqf_fw.setEnabled(true);
        } else {
            sc_bjzl_sfcwgyf.setEnabled(false);
            tv_bjzl_gyf_dz.setEnabled(false);
            tv_bjzl_gyf_fw.setEnabled(false);
            sc_bjzl_sfcwxqf.setEnabled(false);
            tv_bjzl_xqf_dz.setEnabled(false);
            tv_bjzl_xqf_fw.setEnabled(false);
        }

    }

    /**
     * 地址内容
     */
    private void dz() {
        if (!resultSupply_Region.equals("")) {
            sc_bjzl_sfcwgyf.setChecked(true);
            tv_bjzl_gyf_dz.setText(resultSupply_Region);
        }
        if (!resultNeed_Region.equals("")) {
            sc_bjzl_sfcwxqf.setChecked(true);
            tv_bjzl_xqf_dz.setText(resultNeed_Region);
        }
    }

    /**
     * 服务内容
     */
    private void fw() {
        if (!resultSupply_Saas.equals("")) {
            tv_bjzl_gyf_fw.setText(resultSupply_Saas);
        }
        if (!resultNeed_Saas.equals("")) {
            tv_bjzl_xqf_fw.setText(resultNeed_Saas);
        }
    }


    private class MyAdapter_Dynamic extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<String> arrayList;

        public MyAdapter_Dynamic(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            arrayList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList.size();
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
                view = inflater.inflate(R.layout.fbxx_xx_image_item, null);
            }
            ImageView iv_fbxx_xx_iamge_item = view.findViewById(R.id.iv_fbxx_xx_iamge_item);

            Glide.with(BjzlActivity.this)
                    .load( Api.sUrl+ arrayList.get(position))
                    .asBitmap()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .dontAnimate()
                    .into(iv_fbxx_xx_iamge_item);

            return view;
        }

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
                if (sGX.equals("1")) {
                    if (!resultSupply_Saas.equals("")) {
                        tv_bjzl_gyf_fw.setText(resultSupply_Saas);
                    } else {
                        tv_bjzl_gyf_fw.setText("选择服务");
                    }
                } else {
                    if (!resultNeed_Saas.equals("")) {
                        tv_bjzl_xqf_fw.setText(resultNeed_Saas);
                    } else {
                        tv_bjzl_xqf_fw.setText("选择服务");
                    }
                }
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

    /**
     * 方法名：sSaaSList()
     * 功  能：服务列表
     * 参  数：appId
     * saas--服务id 当为0时为一级
     * level--等级 1为一级 2二级 3三级
     */
    private void sSaaSList(String city, String level) {
        String url = Api.sUrl + Api.sSaaSList;
        RequestQueue requestQueue = Volley.newRequestQueue(BjzlActivity.this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("saas", city);
        params.put("level", level);
        // params.put("address", sType);

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

                            JSONObject jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {

                                myAdapterlist.arrListIdsheng = new ArrayList<String>();
                                myAdapterlist.arrlistNamesheng = new ArrayList<String>();

                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    response = resultJsonArray.getJSONObject(i);
                                    String resultId = response.getString("id");
                                    String resultName = response.getString("name");
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


    /**
     * 显示修改头像的对话框
     */
    public void dialog() {
        // mylist_cat
        checkPermissions();
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_touxiang, null);
        Button bt_tx_xc = inflate.findViewById(R.id.bt_tx_xc);
        Button bt_tx_xj = inflate.findViewById(R.id.bt_tx_xj);
        Button btn_cancel = inflate.findViewById(R.id.btn_cancel);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
        bt_tx_xc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                if (hasPermission) {
                    openGallery();
                }
                dialog.dismiss();
            }
        });
        bt_tx_xj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                if (hasPermission) {
                    takePhoto();
                }
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SCAN_OPEN_PHONE);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否有存储和拍照权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    ) {
                hasPermission = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
            }
        }else {
            hasPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                Toast.makeText(this, "权限授予失败！", Toast.LENGTH_SHORT).show();
                hasPermission = false;
            }
        }
    }

    // 拍照
    @SuppressLint("WrongConstant")
    private void takePhoto() {
        // 要保存的文件名
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = "photo_" + time;
        // 创建一个文件夹
        String path = Environment.getExternalStorageDirectory() + "/take_photo";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 要保存的图片文件
        imgFile = new File(file, fileName + ".jpeg");
        // 将file转换成uri
        // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
        imgUri = getUriForFile(this, imgFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 添加Uri读取权限
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        // 或者
//        grantUriPermission("com.rain.takephotodemo", imgUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 添加图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    // 图片裁剪
    private void cropPhoto(Uri uri, boolean fromCapture) {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        // 注意一定要添加该项权限，否则会提示无法裁剪
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.putExtra("scale", true);

        // 设置裁剪区域的宽高比例
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);

        // 设置裁剪区域的宽度和高度
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);

        intent.putExtra("scale",true);
        intent.putExtra("scaleUpIfNeeded", true);

        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);

        // 指定裁剪完成以后的图片所保存的位置,pic info显示有延时
        if (fromCapture) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
            mCutUri = Uri.fromFile(imgFile);
        } else { // 从相册中选择，那么裁剪的图片保存在take_photo中
            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
            String fileName = "photo_" + time;
            File mCutFile = new File(Environment.getExternalStorageDirectory() + "/take_photo/", fileName + ".jpeg");
            if (!mCutFile.getParentFile().exists()) {
                mCutFile.getParentFile().mkdirs();
            }
            mCutUri = Uri.fromFile(mCutFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        // Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        this.sendBroadcast(intentBc);

        startActivityForResult(intent, REQUEST_CROP); //设置裁剪参数显示图片至ImageVie
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 拍照并进行裁剪
                case REQUEST_TAKE_PHOTO:
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_TAKE_PHOTO:" + imgUri.toString());
                    cropPhoto(imgUri, true);
                    break;

                // 裁剪后设置图片
                case REQUEST_CROP:
                    // iv_grzx_head_pic.setImageURI(mCutUri);
                    dialogin("");
                    upLoadImage(Api.sUrl + "Community/Api/getimg/", new File(mCutUri.getPath()));
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_CROP:" + mCutUri.toString());
                    break;
                // 打开图库获取图片并进行裁剪
                case SCAN_OPEN_PHONE:
                    Log.e(TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    cropPhoto(data.getData(), false);
                    break;

                default:
                    break;
            }
        }
    }


    // 从file中获取uri
    // 7.0及以上使用的uri是contentProvider content://com.rain.takephotodemo.FileProvider/images/photo_20180824173621.jpg
    // 6.0使用的uri为file:///storage/emulated/0/take_photo/photo_20180824171132.jpg
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.example.administrator.kxtw.FileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    //上传头像
    public void upLoadImage(String upload_url, File file) {
        //创建okhClient

        OkHttpClient okHttpClient = new OkHttpClient();
        //创建MultiparBody
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //设置参数
        MediaType mediaType = MediaType.parse("image/png");

        builder.addFormDataPart("logo", file.getName(),
                RequestBody.create(mediaType, file));
        //添加其他参数
   /*     builder.addFormDataPart("user_id", sUser_id);*/
        //    sUser_id = "51a29d817fe243419b0344306b7575bc";
        builder.addFormDataPart("appId", Api.sApp_Id);
        MultipartBody body = builder.build();
        okhttp3.Request request = new okhttp3.Request.Builder().url(upload_url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideDialogin();
                Log.d(TAG, "onFailure() returned: " + "shibai---" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                hideDialogin();
                try {
                    JSONObject jsonObject1 = new JSONObject(response.body().string());
                    String resultMsg = jsonObject1.getString("msg");
                    int resultCode = jsonObject1.getInt("code");
                    if (resultCode > 0) {
                        resultUrl = jsonObject1.getString("data");
              /*          JSONObject jsonObject = new JSONObject(resultData);
                        String resultUrl = jsonObject.getString("url");*/


                        Message message = new Message();
                        message.what = COMPLETED;
                        handler.sendMessage(message);
                        Looper.prepare();

                        // Hint(resultMsg, HintDialog.SUCCESS);
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Hint(resultMsg, HintDialog.ERROR);
                        Looper.loop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse() returned: " + response.body().string());
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPLETED) {
                // imageView.setImageResource(R.drawable.finger); //UI更改操作
                //  touxiang(resultUrl);
            }
        }
    };


    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                /**
                 * 投放地址
                 */
                String sRegion = opt1tx;
                if (!opt1tx.equals("不限")) {
                    if (!opt2tx.equals("不限")) {
                        sRegion = sRegion + "-" + opt2tx;
                        if (!opt3tx.equals("不限")) {
                            sRegion = sRegion + "-" + opt3tx;
                        }
                    }
                }
                if (sGX.equals("1")) {
                    tv_bjzl_gyf_dz.setText(sRegion);
                } else {
                    tv_bjzl_xqf_dz.setText(sRegion);
                }
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        //   mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //   mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(BjzlActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(BjzlActivity.this, R.style.dialog, sHint, type, true).show();
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

                    if (sGX.equals("1")) {
                        tv_bjzl_gyf_fw.setText(tv_bjzl_gyf_fw.getText().toString() + "  " + arrlistNamesheng.get(position));
                    } else {
                        tv_bjzl_xqf_fw.setText(tv_bjzl_xqf_fw.getText().toString() + "  " + arrlistNamesheng.get(position));
                    }
                    if (iType == 1) {
                        if (sGX.equals("1")) {
                            sSaas_gy = arrListIdsheng.get(position);
                        } else {
                            sSaas_xq = arrListIdsheng.get(position);
                        }
                        ll_ssq_shi.setVisibility(View.VISIBLE);
                        tv_ssq_sheng.setText(arrlistNamesheng.get(position));
                        sheng = arrlistNamesheng.get(position);
                        tv_ssq_sheng.setTextColor(tv_ssq_sheng.getResources().getColor(R.color.black));
                        tv_ssq_sheng_xhx.setVisibility(View.GONE);
                        itemid = arrListIdsheng.get(position);
                        hideDialogin();
                        dialogin("");
                        sSaaSList(itemid, String.valueOf(iType + 1));
                    } else if (iType == 2) {
                        if (sGX.equals("1")) {
                            sSaas_gy = sSaas_gy + "-" + arrListIdsheng.get(position);
                        } else {
                            sSaas_xq = sSaas_xq + "-" + arrListIdsheng.get(position);
                        }
                        ll_ssq_qu.setVisibility(View.VISIBLE);
                        tv_ssq_shi.setText(arrlistNamesheng.get(position));
                        shi = arrlistNamesheng.get(position);
                        tv_ssq_shi.setTextColor(tv_ssq_shi.getResources().getColor(R.color.black));
                        tv_ssq_shi_xhx.setVisibility(View.GONE);
                        itemid = arrListIdsheng.get(position);
                        hideDialogin();
                        dialogin("");
                        sSaaSList(itemid, String.valueOf(iType + 1));
                    } else if (iType == 3) {
                        if (sGX.equals("1")) {
                            sSaas_gy = sSaas_gy + "-" + arrListIdsheng.get(position);
                        } else {
                            sSaas_xq = sSaas_xq + "-" + arrListIdsheng.get(position);
                        }
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
}
