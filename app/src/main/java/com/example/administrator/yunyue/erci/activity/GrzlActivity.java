package com.example.administrator.yunyue.erci.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.administrator.yunyue.yjdt_activity.BjzlActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import master.flame.danmaku.danmaku.model.FBDanmaku;

public class GrzlActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = GrzlActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_grzl_back;
    /**
     * 姓名
     */
    private EditText et_grzl_name;
    /**
     * 性别
     */
    private LinearLayout ll_grzl_sex;
    private TextView tv_grzl_sex;
    /**
     * 身份证号
     */
    private EditText et_grzl_id_number;
    /**
     * 我的地址
     */
    private EditText et_grzl_dz;
    /**
     * 紧急联系人姓名
     */
    private EditText et_grzl_jjlxr_name1, et_grzl_jjlxr_name2, et_grzl_jjlxr_name3;
    /**
     * 联系人关系
     */
    private Spinner sp_grzl_jjlxr_relation1, sp_grzl_jjlxr_relation2, sp_grzl_jjlxr_relation3;
    /**
     * 联系人电话
     */
    private EditText et_grzl_jjlxr_phone1, et_grzl_jjlxr_phone2, et_grzl_jjlxr_phone3;

    /**
     * 保存
     */
    private TextView tv_grzl_save;

    private Button bt_lg_sl;
    private Button bt_lg_zc;
    private Button cancel;
    private Dialog dialog;
    private View inflate;

    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 性别
     */
    private String sSex = "";
    private String sRelation1 = "", sRelation2 = "", sRelation3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_grzl);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_grzl_back = findViewById(R.id.ll_grzl_back);
        ll_grzl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_grzl_name = findViewById(R.id.et_grzl_name);
        ll_grzl_sex = findViewById(R.id.ll_grzl_sex);
        tv_grzl_sex = findViewById(R.id.tv_grzl_sex);
        et_grzl_id_number = findViewById(R.id.et_grzl_id_number);
        et_grzl_dz = findViewById(R.id.et_grzl_dz);
        et_grzl_jjlxr_name1 = findViewById(R.id.et_grzl_jjlxr_name1);
        sp_grzl_jjlxr_relation1 = findViewById(R.id.sp_grzl_jjlxr_relation1);
        et_grzl_jjlxr_phone1 = findViewById(R.id.et_grzl_jjlxr_phone1);
        et_grzl_jjlxr_name2 = findViewById(R.id.et_grzl_jjlxr_name2);
        sp_grzl_jjlxr_relation2 = findViewById(R.id.sp_grzl_jjlxr_relation2);
        et_grzl_jjlxr_phone2 = findViewById(R.id.et_grzl_jjlxr_phone2);
        et_grzl_jjlxr_name3 = findViewById(R.id.et_grzl_jjlxr_name3);
        sp_grzl_jjlxr_relation3 = findViewById(R.id.sp_grzl_jjlxr_relation3);
        et_grzl_jjlxr_phone3 = findViewById(R.id.et_grzl_jjlxr_phone3);
        tv_grzl_save = findViewById(R.id.tv_grzl_save);
        sp_grzl_jjlxr_relation1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sRelation1 = getResources().getStringArray(R.array.lxrgx)[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_grzl_jjlxr_relation2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sRelation2 = getResources().getStringArray(R.array.lxrgx)[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_grzl_jjlxr_relation3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sRelation3 = getResources().getStringArray(R.array.lxrgx)[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tv_grzl_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sName = et_grzl_name.getText().toString();
                String sId_number = et_grzl_id_number.getText().toString();
                String sDz = et_grzl_dz.getText().toString();
                String sJjlxr_name1 = et_grzl_jjlxr_name1.getText().toString();
                String sJjlxr_phone1 = et_grzl_jjlxr_phone1.getText().toString();
                String sJjlxr_name2 = et_grzl_jjlxr_name2.getText().toString();
                String sJjlxr_phone2 = et_grzl_jjlxr_phone2.getText().toString();
                String sJjlxr_name3 = et_grzl_jjlxr_name3.getText().toString();
                String sJjlxr_phone3 = et_grzl_jjlxr_phone3.getText().toString();
                String sLinkman = "";
                if (sName.equals("")) {
                    Hint("姓名不能为空！", HintDialog.WARM);
                } else if (sId_number.equals("")) {
                    Hint("身份证号码不能为空！", HintDialog.WARM);
                } else if (sDz.equals("")) {
                    Hint("地址不能为空！", HintDialog.WARM);
                } else {
                    if (!sJjlxr_name1.equals("") | !sJjlxr_phone1.equals("")) {
                        if (sJjlxr_name1.equals("") | sJjlxr_phone1.equals("")) {
                            Hint("请完善紧急联系人1信息", HintDialog.WARM);
                            return;
                        } else {
                            sLinkman = "\"1\":{\"name\":\"" + sJjlxr_name1 + "\",\"relation\":\"" + sRelation1 + "\",\"linkphone\":\"" + sJjlxr_phone1 + "\"}";
                        }
                    }
                    if (!sJjlxr_name2.equals("") | !sJjlxr_phone2.equals("")) {
                        if (sJjlxr_name2.equals("") | sJjlxr_phone2.equals("")) {
                            Hint("请完善紧急联系人2信息", HintDialog.WARM);
                            return;
                        } else {
                            sLinkman = sLinkman + "\"1\":{\"name\":\"" + sJjlxr_name2 + "\",\"relation\":\"" + sRelation2 + "\",\"linkphone\":\"" + sJjlxr_phone2 + "\"}";
                        }
                    }
                    if (!sJjlxr_name3.equals("") | !sJjlxr_phone3.equals("")) {
                        if (sJjlxr_name3.equals("") | sJjlxr_phone3.equals("")) {
                            Hint("请完善紧急联系人3信息", HintDialog.WARM);
                            return;
                        } else {
                            sLinkman = sLinkman + "\"1\":{\"name\":\"" + sJjlxr_name3 + "\",\"relation\":\"" + sRelation3 + "\",\"linkphone\":\"" + sJjlxr_phone3 + "\"}";
                        }
                    }
                    if (sLinkman.equals("")) {
                        Hint("请添加紧急联系人", HintDialog.WARM);
                    } else {
                        sLinkman = "{" + sLinkman + "}";
                        hideDialogin();
                        dialogin("");
                        sLinkman(sName, sId_number, sDz, sLinkman);
                    }
                }

            }
        });
        ll_grzl_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        sLinkmanList();
    }


    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        bt_lg_sl = (Button) inflate.findViewById(R.id.bt_lg_sl);
        bt_lg_zc = (Button) inflate.findViewById(R.id.bt_lg_zc);
        cancel = (Button) inflate.findViewById(R.id.btn_cancel);
        bt_lg_sl.setOnClickListener(this);
        bt_lg_zc.setOnClickListener(this);
        cancel.setOnClickListener(this);
        bt_lg_sl.setText("男");
        bt_lg_zc.setText("女");
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_lg_sl:
                tv_grzl_sex.setText("男");
                sSex = "男";
                dialog.dismiss();
                break;
            case R.id.bt_lg_zc:
                tv_grzl_sex.setText("女");
                sSex = "女";
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
        }
    }


    /**
     * 方法名：sLinkmanList()
     * 功  能：个人资料列表
     * 参  数：appId
     */
    private void sLinkmanList() {
        String url = Api.sUrl + Api.sLinkmanList;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                                JSONObject jsonObject1 = new JSONObject(sDate.toString()).getJSONObject("data");
                                String resultId = jsonObject1.getString("id");
                                String resultSex = jsonObject1.getString("sex");
                                if (resultSex.equals("1")) {
                                    tv_grzl_sex.setText("男");
                                } else {
                                    tv_grzl_sex.setText("女");
                                }
                                String resultAddress = jsonObject1.getString("address");
                                et_grzl_dz.setText(resultAddress);
                                String resultIdentity = jsonObject1.getString("identity");
                                et_grzl_id_number.setText(resultIdentity);
                                String resultReal_name = jsonObject1.getString("real_name");
                                et_grzl_name.setText(resultReal_name);
                                JSONArray jsonArray_link = jsonObject1.getJSONArray("link");
                                ArrayList<HashMap<String, String>> mylist_link = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < jsonArray_link.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) jsonArray_link.opt(i);
                                    String ItemId = jsonObject2.getString("id");
                                    String ItemUser_id = jsonObject2.getString("user_id");
                                    String ItemLinkman = jsonObject2.getString("linkman");
                                    String ItemRelation = jsonObject2.getString("relation");
                                    String ItemLinkphone = jsonObject2.getString("linkphone");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", ItemId);
                                    map.put("user_id", ItemUser_id);
                                    map.put("linkman", ItemLinkman);
                                    map.put("relation", ItemRelation);
                                    map.put("linkphone", ItemLinkphone);
                                    mylist_link.add(map);
                                }
                                if (mylist_link.size() >= 1) {
                                    et_grzl_jjlxr_name1.setText(mylist_link.get(0).get("linkman"));
                                    for (int i = 0; i < getResources().getStringArray(R.array.lxrgx).length; i++) {
                                        if (getResources().getStringArray(R.array.lxrgx)[i].equals(mylist_link.get(0).get("relation"))) {
                                            sp_grzl_jjlxr_relation1.setSelection(i, true);
                                        }
                                    }
                                    et_grzl_jjlxr_phone1.setText(mylist_link.get(0).get("linkphone"));
                                }
                                if (mylist_link.size() >= 2) {
                                    et_grzl_jjlxr_name2.setText(mylist_link.get(1).get("linkman"));

                                    for (int i = 0; i < getResources().getStringArray(R.array.lxrgx).length; i++) {
                                        if (getResources().getStringArray(R.array.lxrgx)[i].equals(mylist_link.get(1).get("relation"))) {
                                            sp_grzl_jjlxr_relation2.setSelection(i, true);
                                        }
                                    }
                                /*    int index = Arrays.binarySearch(getResources().getStringArray(R.array.lxrgx), mylist_link.get(1).get("relation"));
                                    sp_grzl_jjlxr_relation2.setSelection(index, true);*/
                                    et_grzl_jjlxr_phone2.setText(mylist_link.get(1).get("linkphone"));
                                }
                                if (mylist_link.size() >= 3) {
                                    et_grzl_jjlxr_name3.setText(mylist_link.get(2).get("linkman"));
                                    for (int i = 0; i < getResources().getStringArray(R.array.lxrgx).length; i++) {
                                        if (getResources().getStringArray(R.array.lxrgx)[i].equals(mylist_link.get(2).get("relation"))) {
                                            sp_grzl_jjlxr_relation3.setSelection(i, true);
                                        }
                                    }
                                    et_grzl_jjlxr_phone3.setText(mylist_link.get(2).get("linkphone"));
                                }

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

    /**
     * 方法名：sLinkman()
     * 功  能：个人资料
     * 参  数：appId
     */
    private void sLinkman(String sUser_name, String sIdentity, String sAddress, String sLinkman) {
        String url = Api.sUrl + Api.sLinkman;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appId", Api.sApp_Id);
        params.put("user_id", sUser_id);
        params.put("user_name", sUser_name);
        int iSex = 1;
        if (sSex.equals("男")) {
            iSex = 1;
        } else {
            iSex = 2;
        }
        params.put("sex", String.valueOf(iSex));
        params.put("identity", sIdentity);
        params.put("address", sAddress);
        params.put("linkman", sLinkman);

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
                                hideDialogin();
                                Hint(resultMsg, HintDialog.SUCCESS);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1500);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(GrzlActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(GrzlActivity.this, R.style.dialog, sHint, type, true).show();
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
