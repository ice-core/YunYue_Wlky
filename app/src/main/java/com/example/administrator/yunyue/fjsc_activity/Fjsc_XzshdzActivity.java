package com.example.administrator.yunyue.fjsc_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.StatusBarUtil;
import com.example.administrator.yunyue.baidudingwei.entity.LocationGpsBean;
import com.example.administrator.yunyue.baidudingwei.listener.MyGetSuggResultListener;
import com.example.administrator.yunyue.baidudingwei.ui.JobAddressAdapter;
import com.example.administrator.yunyue.baidudingwei.ui.OpenGPSDialog;
import com.example.administrator.yunyue.baidudingwei.ui.PermissionDialog;
import com.example.administrator.yunyue.baidudingwei.utils.GlobalUtils;
import com.example.administrator.yunyue.baidudingwei.utils.permission.OnPermisionListener;
import com.example.administrator.yunyue.baidudingwei.utils.permission.PermissionHandler;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.sc_activity.SpxqActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fjsc_XzshdzActivity extends AppCompatActivity {
    private static final String TAG = Fjsc_XzshdzActivity.class.getSimpleName();
    /**
     * 返回
     */
    private LinearLayout ll_fjsc_xzshdz_back;
    /**
     * 地址列表
     */
    private GridView gv_fjsc_xzshdz;
    /**
     * 输入框
     */
    private EditText et_fjsc_xzshdz_query;
    /**
     * 搜索
     */
    private TextView tv_fjsc_xzshdz_query;
    /**
     * 遮挡
     */
    private ImageView iv_fjsc_xzshdz_bj;
    /**
     * 新增地址
     */
    private TextView tv_fjsc_xzshdz_xzdz;
    /**
     * 重新定位
     */
    private TextView tv_fjsc_xzshdz_cxdw;

    private SharedPreferences pref;
    private String sUser_id;
    private String sIs_vip = "";
    MyAdapter myAdapter;

    private SuggestionSearch mSuggestionSearch;
    public String mCity = "上海市";
    private boolean locationTag;//用于标记是否为刚刚判断权限后的定位失败（针对小米手机做的优化）
    private List<LocationGpsBean> jobAddress = new ArrayList<>();

    private JobAddressAdapter mAdapter;
    private LocationGpsBean selectGpsBean;
    private OpenGPSDialog mOpenGPSDialog;
    /**
     * 收货地址列表
     */
    private String sDz = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_fjsc__xzshdz);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        sUser_id = pref.getString("user_id", "");
        sIs_vip = pref.getString("is_vip", "");
        ll_fjsc_xzshdz_back = findViewById(R.id.ll_fjsc_xzshdz_back);
        ll_fjsc_xzshdz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gv_fjsc_xzshdz = findViewById(R.id.gv_fjsc_xzshdz);
        et_fjsc_xzshdz_query = findViewById(R.id.et_fjsc_xzshdz_query);
        et_fjsc_xzshdz_query.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//设置键盘为搜索
        et_fjsc_xzshdz_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String searchInput = et_fjsc_xzshdz_query.getText().toString().trim();
                    sDz = "";
                    search(searchInput);
                }
                return false;
            }
        });
        et_fjsc_xzshdz_query.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    et_fjsc_xzshdz_query.setCursorVisible(true);// 再次点击显示光标
                    iv_fjsc_xzshdz_bj.setVisibility(View.VISIBLE);
                    tv_fjsc_xzshdz_query.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        iv_fjsc_xzshdz_bj = findViewById(R.id.iv_fjsc_xzshdz_bj);
        tv_fjsc_xzshdz_query = findViewById(R.id.tv_fjsc_xzshdz_query);
        tv_fjsc_xzshdz_cxdw = findViewById(R.id.tv_fjsc_xzshdz_cxdw);
        tv_fjsc_xzshdz_cxdw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fjsc_ShouyeActivity.ISQUERY = true;
                finish();
            }
        });

        iv_fjsc_xzshdz_bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_fjsc_xzshdz_bj.setVisibility(View.GONE);
                et_fjsc_xzshdz_query.setCursorVisible(false);
                tv_fjsc_xzshdz_query.setVisibility(View.GONE);
            }
        });
        tv_fjsc_xzshdz_xzdz = findViewById(R.id.tv_fjsc_xzshdz_xzdz);
        tv_fjsc_xzshdz_xzdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(Fjsc_XzshdzActivity.this, Fjsc_XjshdzActivity.class);
                    Fjsc_XjshdzActivity.sDz = "";
                    startActivity(intent);

            }
        });
        sAddresslist();
    }

    private boolean mIsCurrSearchAction = false;

    private void search(String searchInput) {
        GlobalUtils.hideKeyboard(et_fjsc_xzshdz_query);

        if (TextUtils.isEmpty(searchInput)) {
            showToast(getString(R.string.search_input_content_empty));
            return;
        }
        //下面做搜寻的操作
        if (mPoiSearch == null) {
            initSearch();
        }
        // 发起搜索
        if (searchInput.contains("市")) {
            mIsCurrSearchAction = true;
            String[] splitInputContent = searchInput.split("市");
            int length = splitInputContent.length;
            String city = "";
            String keyword = "";
            if (length == 1) {          //不能周边搜索,市的名称作为key
                keyword = splitInputContent[0];
            }
            if (length >= 2) {
                city = splitInputContent[0];
                keyword = splitInputContent[1];
            }
            mPoiSearch.searchInCity((new PoiCitySearchOption())
                    .city(city)
                    .keyword(keyword));
            return;
        }
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .city(mCity != null ? mCity : "")            //在线搜索 == 关键词搜索
                .keyword(searchInput));

    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 兴趣点搜索对象，满足条件的搜索到的对象
     */
    private PoiSearch mPoiSearch;

    private void initSearch() {
        // 创建搜索对象
        mPoiSearch = PoiSearch.newInstance();
        // 设置监听器
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override    // 接收搜索结果
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    if (!GlobalUtils.isOpenGps(Fjsc_XzshdzActivity.this)) {
                        showToast(getString(R.string.usercenter_selete_address_gps_close));
                    } else {
                        if (locationTag) {//如果为刚刚进行权限检测后的定位失败，应该为“定位”权限未打开
                            showNeverAskDialog();
                        } else {
                            showToast(getString(R.string.usercenter_search_no_result));
                        }
                    }
                    locationTag = false;//复位
                    return;
                }
                setBaseAdater(poiResult.getAllPoi());

            }

            @Override  // 详情数据
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                if (poiDetailResult == null || poiDetailResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    showToast(getString(R.string.usercenter_search_no_result));
                    return;
                }
                if (mAdapter != null) {
                    int count = mAdapter.getCount();
                    for (int i = 0; i < count; i++) {
                        String adapterUid = ((LocationGpsBean) mAdapter.getItem(i)).getuId();
                        if (TextUtils.equals(poiDetailResult.getUid(), adapterUid)) {
                            setSelectP(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            }
        });
        mSuggestionSearch = SuggestionSearch.newInstance();
        MyGetSuggResultListener myGetSuggResultListener = new MyGetSuggResultListener(this);
        myGetSuggResultListener.setOnGetSuggestionResListener(new MyGetSuggResultListener.OnGetSuggestionResListener() {
            @Override
            public void getSuggestionRes(List<LocationGpsBean> res) {                           //被回调的接口

                setArrayAdater(res);
            }
        });
        mSuggestionSearch.setOnGetSuggestionResultListener(myGetSuggResultListener);

    }

    //显示在线搜索的结果
    private void setArrayAdater(List<LocationGpsBean> res) {
        mAdapter = new JobAddressAdapter(Fjsc_XzshdzActivity.this, res);
        if (res != null && res.size() > 0) {
            mAdapter.selectPosition(0);
            selectGpsBean = res.get(0);
            jobAddress = res;

        }
        gv_fjsc_xzshdz.setAdapter(mAdapter);
        if (sDz.equals("shdzlb")) {
            Fjsc_ShouyeActivity.sHint = selectGpsBean.blackName;
            Fjsc_ShouyeActivity.ISQUERY = false;
            Fjsc_ShouyeActivity.sLatitude = String.valueOf(selectGpsBean.getPt().latitude);
            Fjsc_ShouyeActivity.sLongitude = String.valueOf(selectGpsBean.getPt().longitude);
            finish();
        }
        gv_fjsc_xzshdz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectP(position);

            }
        });
    }

    private void setSelectP(int position) {
        selectGpsBean = jobAddress.get(position);
        Fjsc_ShouyeActivity.sHint = selectGpsBean.blackName;
        Fjsc_ShouyeActivity.ISQUERY = false;
        Fjsc_ShouyeActivity.sLatitude = String.valueOf(selectGpsBean.getPt().latitude);
        Fjsc_ShouyeActivity.sLongitude = String.valueOf(selectGpsBean.getPt().longitude);
        mAdapter.selectPosition(position);
        finish();
        mAdapter.notifyDataSetChanged();
        gv_fjsc_xzshdz.smoothScrollToPosition(position);

    }


    //显示周边搜索、城市搜索的结果
    private void setBaseAdater(List<PoiInfo> allPoi) {
        jobAddress.clear();
        if (allPoi != null && allPoi.size() > 0) {
            if (mIsCurrSearchAction) {

            }
            for (PoiInfo info : allPoi) {
                LocationGpsBean bean = new LocationGpsBean();
                bean.blackName = info.name;
                bean.address = (TextUtils.isEmpty(info.address.trim())) ? info.name : info.address;
                bean.uId = info.uid;
                bean.pt = info.location;
                jobAddress.add(bean);
            }
        } else {
            showToast(getString(R.string.usercenter_search_no_result));
            return;
        }
        mAdapter = new JobAddressAdapter(Fjsc_XzshdzActivity.this, jobAddress);

        gv_fjsc_xzshdz.setAdapter(mAdapter);
        gv_fjsc_xzshdz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setSelectP(position);
            }
        });
        mAdapter.selectPosition(0);//默认第1项
        selectGpsBean = jobAddress.get(0);
        mIsCurrSearchAction = false;
        if (sDz.equals("shdzlb")) {
            Fjsc_ShouyeActivity.sHint = selectGpsBean.blackName;
            Fjsc_ShouyeActivity.ISQUERY = false;
            Fjsc_ShouyeActivity.sLatitude = String.valueOf(selectGpsBean.getPt().latitude);
            Fjsc_ShouyeActivity.sLongitude = String.valueOf(selectGpsBean.getPt().longitude);
            finish();

        }
    }

    private PermissionHandler mHandler;

    //展示权限选择“不再询问并拒绝”的对话框
    public void showNeverAskDialog() {
        if (mHandler != null) {
            String appName = getString(R.string.app);
            PermissionDialog permissionDialog = new PermissionDialog(this, 1, "由于" + appName + "无法获取“" + mHandler.getPermissionName() + "”权限，不能正常工作，请开启权限后再使用。\n"
                    + "设置路径：设置->应用->" + appName + "->权限");
            permissionDialog.setOnPermisionListener(new OnPermisionListener() {
                @Override
                public void gotoSetting() {
                    /**
                     * 以下方法为调用系统设置中关于本APP的应用详情页
                     */
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }

                @Override
                public void onConfirm() {

                }

                @Override
                public void onCancel() {
                    if (mHandler.isForce())
                        finish();
                }
            });
            permissionDialog.show();
        }
    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        sAddresslist();
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


    /**
     * 方法名：sAddresslist()
     * 功  能：地址列表
     * 参  数：appId
     */
    private void sAddresslist() {
        String url = Api.sUrl + Api.sAddresslist;
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
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                            myAdapter = new MyAdapter(Fjsc_XzshdzActivity.this);
                            if (resultCode > 0) {
                                JSONArray resultJsonArray = jsonObject1.getJSONArray("data");

                                for (int i = 0; i < resultJsonArray.length(); i++) {
                                    JSONObject jsonObject = resultJsonArray.getJSONObject(i);
                                    String resultId = jsonObject.getString("id");
                                    String resultName = jsonObject.getString("username");
                                    String resultMobile = jsonObject.getString("phone");
                                    String resultSheng = jsonObject.getString("sheng");
                                    String resultShi = jsonObject.getString("shi");
                                    String resultQu = jsonObject.getString("qu");
                                    String resultDetail = jsonObject.getString("detail");
                                    String resultIsDefault = jsonObject.getString("is_default");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("ItemId", resultId);
                                    map.put("ItemName", resultName);
                                    map.put("ItemMobile", resultMobile);
                                    map.put("ItemSheng", resultSheng);
                                    map.put("ItemShi", resultShi);
                                    map.put("ItemQu", resultQu);
                                    map.put("ItemDetail", resultDetail);
                                    map.put("ItemIsDefault", resultIsDefault);
                                    mylist.add(map);
                                }
                                myAdapter.mylist = mylist;
                                gv_fjsc_xzshdz.setAdapter(myAdapter);

                                if (mylist.size() == 0) {

                                    gv_fjsc_xzshdz.setVisibility(View.GONE);
                                }
                            } else {
                                if (mylist.size() == 0) {
                                    gv_fjsc_xzshdz.setVisibility(View.GONE);
                                }
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
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

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(Fjsc_XzshdzActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(Fjsc_XzshdzActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        public ArrayList<HashMap<String, String>> mylist;

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            mylist = new ArrayList<HashMap<String, String>>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mylist.size();
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

        public Bitmap stringToBitmap(String string) {    // 将字符串转换成Bitmap类型
            Bitmap bitmap = null;
            try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = inflater.inflate(R.layout.lsdz_item, null);
            }
            LinearLayout ll_lsdz = view.findViewById(R.id.ll_lsdz);
            TextView tv_shdz_name = view.findViewById(R.id.tv_shdz_name);
            TextView tv_shdz_phone = view.findViewById(R.id.tv_shdz_phone);
            TextView tv_shdz_isdefault = view.findViewById(R.id.tv_shdz_isdefault);
            TextView tv_shdz_site = view.findViewById(R.id.tv_shdz_site);
            ImageView iv_shdz_bianji = view.findViewById(R.id.iv_shdz_bianji);
            tv_shdz_name.setText(mylist.get(position).get("ItemName"));
            tv_shdz_phone.setText(mylist.get(position).get("ItemMobile"));
            iv_shdz_bianji.setVisibility(View.GONE);
            tv_shdz_isdefault.setVisibility(View.GONE);

            tv_shdz_site.setText(mylist.get(position).get("ItemSheng") + mylist.get(position).get("ItemShi")
                    + mylist.get(position).get("ItemQu") + mylist.get(position).get("ItemDetail"));

            ll_lsdz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String searchInput = tv_shdz_site.getText().toString();
                    sDz = "shdzlb";
                    search(searchInput);
                    Fjsc_QrddActivity.iDz = Integer.valueOf(mylist.get(position).get("ItemId"));
                }
            });
            return view;
        }
    }
}
