package com.example.administrator.yunyue.zb_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.lzt.flowviews.global.OnFlowClickListener;
import com.lzt.flowviews.view.FlowView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SousuoActivity extends AppCompatActivity {
    /**
     * 返回
     */
    private LinearLayout ll_ss_back;

    /**
     * 取消
     */
    private TextView tv_ss_qx;
    RequestQueue queue = null;
    private SharedPreferences pref;
    private String sUser_id;
    /**
     * 搜索历史记录
     */
    private FlowView fv_ss_lsjl;
    private TextView tv_ss_lsjl;
    /**
     * 热门搜索
     */
    private FlowView fv_ss_rmss;
    private TextView tv_ss_rmss;
    private static String[] lijl = new String[]{};
    private static String[] rmss = new String[]{};
    private EditText et_sousuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        setContentView(R.layout.activity_sousuo);

        queue = Volley.newRequestQueue(SousuoActivity.this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        ll_ss_back = findViewById(R.id.ll_ss_back);
        tv_ss_qx = findViewById(R.id.tv_ss_qx);
        tv_ss_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_ss_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fv_ss_lsjl = findViewById(R.id.fv_ss_lsjl);
        tv_ss_lsjl = findViewById(R.id.tv_ss_lsjl);
        et_sousuo = findViewById(R.id.et_sousuo);
        fv_ss_lsjl.setOnFlowClickListener(new OnFlowClickListener() {
            @Override
            public void onClickedView(View view, Object value, int position, int type) {
                super.onClickedView(view, value, position, type);
                String s = lijl[position];
                //  Toast.makeText(SousuoActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SousuoActivity.this, LiebiaoActivity.class);
                intent.putExtra("content", s);
                startActivity(intent);
            }
        });
        fv_ss_rmss = findViewById(R.id.fv_ss_rmss);
        tv_ss_rmss = findViewById(R.id.tv_ss_rmss);
        fv_ss_rmss.setOnFlowClickListener(new OnFlowClickListener() {
            @Override
            public void onClickedView(View view, Object value, int position, int type) {
                super.onClickedView(view, value, position, type);
                String s = rmss[position];
                //  Toast.makeText(SousuoActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SousuoActivity.this, LiebiaoActivity.class);
                intent.putExtra("content", s);
                startActivity(intent);
            }
        });
        et_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    String sQueryText = et_sousuo.getText().toString();
                    if (sQueryText.equals("")) {
                    } else {
                        Intent intent = new Intent(SousuoActivity.this, LiebiaoActivity.class);
                        intent.putExtra("content", sQueryText);
                        startActivity(intent);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                return false;
            }
        });
        query();
    }

    /**
     * 搜索记录，热门搜索信息获取
     */
    private void query() {
        String url = Api.sUrl + "Broadcast/Api/videoHot/appId/" + Api.sApp_Id + "/user_id/" + sUser_id;
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
                        JSONObject jsonObjectdata = new JSONObject(sDate.toString()).getJSONObject("data");
                        JSONArray jsonArraysearch_hot = jsonObjectdata.getJSONArray("search_hot");
                        ArrayList<HashMap<String, String>> mylist_search_hot = new ArrayList<HashMap<String, String>>();
                        lijl = new String[jsonArraysearch_hot.length()];
                        for (int i = 0; i < jsonArraysearch_hot.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArraysearch_hot.opt(i);
                            String ItemId = jsonObject2.getString("id");
                            String Itemcontent = jsonObject2.getString("content");
                            String Itemsearch_cont = jsonObject2.getString("search_cont");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", ItemId);
                            map.put("content", Itemcontent);
                            map.put("search_cont", Itemsearch_cont);
                            lijl[i] = Itemcontent;
                            mylist_search_hot.add(map);
                        }
                        if (jsonArraysearch_hot.length() == 0) {
                            tv_ss_lsjl.setVisibility(View.GONE);
                        } else {
                            tv_ss_lsjl.setVisibility(View.VISIBLE);
                        }
                        fv_ss_lsjl.setAttr(R.color.hei666666, R.drawable.bj_4_f4f4f4)
                                .addViewCommon(lijl, R.layout.sousuo_textview_flow, 1, false)
                                .setUseSelected(false);
                        // setGridView_fenlei(mylist_banner);
                        JSONArray jsonArrayplay_hot = jsonObjectdata.getJSONArray("play_hot");
                        ArrayList<HashMap<String, String>> mylist_play_hot = new ArrayList<HashMap<String, String>>();
                        rmss = new String[jsonArrayplay_hot.length()];
                        for (int i = 0; i < jsonArrayplay_hot.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) jsonArrayplay_hot.opt(i);
                            String Itemid = jsonObject2.getString("id");
                            String Itemtitle = jsonObject2.getString("title");
                            String Itemplay_cont = jsonObject2.getString("play_cont");
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", Itemid);
                            map.put("typename", Itemtitle);
                            map.put("type_img", Itemplay_cont);
                            rmss[i] = Itemtitle;
                            mylist_play_hot.add(map);
                        }
                        fv_ss_rmss.setAttr(R.color.hei666666, R.drawable.bj_4_f4f4f4)
                                .addViewCommon(rmss, R.layout.sousuo_textview_flow, 1, false)
                                .setUseSelected(false);
                        if (jsonArrayplay_hot.length() == 0) {
                            tv_ss_rmss.setVisibility(View.GONE);
                        } else {
                            tv_ss_rmss.setVisibility(View.VISIBLE);
                        }

                        // Hint(resultMsg, HintDialog.SUCCESS);
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(SousuoActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(SousuoActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
