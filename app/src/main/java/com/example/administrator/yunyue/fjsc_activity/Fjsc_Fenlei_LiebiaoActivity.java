package com.example.administrator.yunyue.fjsc_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.fjsc_fenlei.*;
import com.example.administrator.yunyue.fjsc_fenlei.ScrollRightAdapter_fjsc;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fjsc_Fenlei_LiebiaoActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_Fenlei_LiebiaoActivity.class.getSimpleName();
    private RecyclerView rec_fjsc_fenlei_liebiao;
    private List<String> left;
    private List<ScrollBean_Fjsc> right;
    private com.example.administrator.yunyue.fjsc_fenlei.ScrollRightAdapter_fjsc rightAdapter;
    private GridLayoutManager rightManager;
    private Context mContext;
    private ImageView iv_fjsc_fenlei_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__fenlei__liebiao);
        mContext = this;
        rec_fjsc_fenlei_liebiao = findViewById(R.id.rec_fjsc_fenlei_liebiao);
        iv_fjsc_fenlei_back = findViewById(R.id.iv_fjsc_fenlei_back);
        iv_fjsc_fenlei_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initRight();
        sTypelist();
    }

    /**
     * 方法名：sTypelist()
     * 功  能：分类列表接口
     * 参  数：appId
     */
    private void sTypelist() {
        String url = Api.sUrl + Api.sTypelist;
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
                        try {
                            JSONObject jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                left = new ArrayList<>();
                                right = new ArrayList<>();
                                ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObjectdate = resultJsonArray.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();

                                    map.put("ItemName", jsonObjectdate.getString("name"));//
                                    map.put("ItemLogo", jsonObjectdate.getString("logo"));//
                                    right.add(new ScrollBean_Fjsc(true, jsonObjectdate.getString("name")));
                                    JSONArray resultJsonArray_type = jsonObjectdate.getJSONArray("type");
                                    for (int a = 0; a < resultJsonArray_type.length(); a++) {
                                        JSONObject jsonObjecttype = resultJsonArray_type.getJSONObject(a);
                                        String name = jsonObjecttype.getString("name");
                                        right.add(new ScrollBean_Fjsc(new ScrollBean_Fjsc.ScrollItemBean(jsonObjecttype.getString("id"),
                                                jsonObjecttype.getString("name"), Api.sUrl + jsonObjectdate.getString("logo"), jsonObjectdate.getString("name"))));
                                    }
                                }
                                rightAdapter.setNewData(right);
                            } else {
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


    private void initRight() {

        rightManager = new GridLayoutManager(mContext, 4);

        if (rightAdapter == null) {
            rightAdapter = new ScrollRightAdapter_fjsc(R.layout.fjsc_fenlei_name, R.layout.fjsc_fenlei_title, null);
            rec_fjsc_fenlei_liebiao.setLayoutManager(rightManager);
            rec_fjsc_fenlei_liebiao.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.set(dpToPx(mContext, getDimens(mContext, R.dimen.dp1))
                            , 0
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp1))
                            , dpToPx(mContext, getDimens(mContext, R.dimen.dp1)));
                }
            });
            rec_fjsc_fenlei_liebiao.setAdapter(rightAdapter);
        } else {
            rightAdapter.notifyDataSetChanged();
        }
        //  rightAdapter.setNewData(right);


        rightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    //点击左侧列表的相应item,右侧列表相应的title置顶显示
                    //(最后一组内容若不能填充右侧整个可见页面,则显示到右侧列表的最底端)
                    case R.id.ll_you:
                        //   HashMap<String, String> map = (HashMap<String, String>) lv_sc_lb.getItemAtPosition(position);
                 /*       Intent intent = new Intent(FenleiActivity.this, FwzxActivity.class);
                        startActivity(intent);*/
                        ScrollBean_Fjsc s = right.get(position);
                        ScrollBean_Fjsc.ScrollItemBean t = s.t;
                        Intent intent = new Intent(Fjsc_Fenlei_LiebiaoActivity.this, Fjsc_FenleiActivity.class);
                        intent.putExtra("id", "");
                        intent.putExtra("goods_type_id", t.getId());
                        intent.putExtra("name", t.getText());

                        startActivity(intent);

               /*         Intent intent = new Intent(Fjsc_Fenlei_LiebiaoActivity.this, SplbActivity.class);
                        intent.putExtra("id", ItemId);
                        intent.putExtra("er_id", "");
                        intent.putExtra("miaoshu", t.getText());
                        intent.putExtra("san_id", t.getId());
                        startActivity(intent);*/
                        break;
                }
            }
        });

    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
    }


    /**
     * 获得资源 dimens (dp)
     *
     * @param context
     * @param id      资源id
     * @return
     */
    public float getDimens(Context context, int id) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float px = context.getResources().getDimension(id);
        return px / dm.density;
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(Fjsc_Fenlei_LiebiaoActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_Fenlei_LiebiaoActivity.this, R.style.dialog, sHint, type, true).show();
    }
}
