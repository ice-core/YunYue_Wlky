package com.example.administrator.yunyue.sq_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
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
import com.example.administrator.yunyue.txllb.SideBar;
import com.example.administrator.yunyue.txllb.User;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class XzlxrActivity extends AppCompatActivity {

    private SideBar sideBar;
    private ArrayList<User> list;
    private SharedPreferences pref;
    private String sUser_id;
    RequestQueue queue = null;
    public static String sId = "";
    private GridView gv_xzlxr;
    private LinearLayout ll_xzlxr_back;
    private ArrayList<String> arrayList;
    private LinearLayout ll_haoyou_add;

    private EditText et_xzlxr_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusBarUtil.setStatusBarColor(this, R.color.white_c);
        }
        setContentView(R.layout.activity_xzlxr);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(XzlxrActivity.this);
        sUser_id = pref.getString("user_id", "");
        gv_xzlxr = findViewById(R.id.gv_xzlxr);
        arrayList = new ArrayList<>();
        ll_xzlxr_back = findViewById(R.id.ll_xzlxr_back);
        ll_haoyou_add = findViewById(R.id.ll_haoyou_add);
        et_xzlxr_query = findViewById(R.id.et_xzlxr_query);
        et_xzlxr_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    String sQueryText = et_xzlxr_query.getText().toString();

                    if (sQueryText.equals("")) {
                    } else {
                        hideDialogin();
                        dialogin("");
                        query(sQueryText);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

        ll_xzlxr_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ll_haoyou_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sId = "";
                for (int i = 0; i < list.size(); i++) {
                    if (arrayList.get(i).equals("1")) {
                        if (sId.equals("")) {
                            sId = "[" + list.get(i).getId();
                        } else {
                            sId = sId + "," + list.get(i).getId();
                        }
                    }
                }
                sId = sId + "]";
                hideDialogin();
                dialogin("");
                joinsGroupquery(sId);
            }
        });
        query("");
    }

    /**
     * 拉入群聊
     */
    private void joinsGroupquery(String id) {
        String url = Api.sUrl
                + "Community/Api/joinsGroup/appId/" + Api.sApp_Id + "/user_id/" + sUser_id
                + "/group_id/" + QunxiangqingActivity.sGroup_Id + "/id/" + id;

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
                        Hint(resultMsg, HintDialog.SUCCESS);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1200);
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
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    private void query(String name) {
        String url = Api.sUrl
                + "Community/Api/Guserlist/appId/" + Api.sApp_Id + "/user_id/" + sUser_id
                + "/group_id/" + QunxiangqingActivity.sGroup_Id;
        if (!name.equals("")) {
            url = url + "/name/" + name;
        }
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
                    JSONArray resultJsonArray = jsonObject1.getJSONArray("data");
                    MyAdapter myAdapter = new MyAdapter(XzlxrActivity.this);
                    list = new ArrayList<>();
                    arrayList = new ArrayList<>();
                    if (resultCode > 0) {

                      /*  list.add(new User("亳州", "1"));*/
                        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            jsonObject = resultJsonArray.getJSONObject(i);
                            String resultNickname = jsonObject.getString("note");
                            String resultUser_id = jsonObject.getString("user_id");
                            String resultHeadimgurl = jsonObject.getString("headimgurl");
                            list.add(new User(resultNickname, resultUser_id, resultHeadimgurl));
                            arrayList.add("0");
                        }
                        gv_xzlxr.setAdapter(myAdapter);
                    } else {
                        gv_xzlxr.setAdapter(myAdapter);
                        Hint(resultMsg, HintDialog.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Hint(String.valueOf(volleyError), HintDialog.ERROR);
                hideDialogin();
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }

    LoadingDialog loadingDialog;

    private void dialogin(String msg) {
        loadingDialog = new LoadingDialog(XzlxrActivity.this, R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(XzlxrActivity.this, R.style.dialog, sHint, type, true).show();
    }


    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        //  public ArrayList<HashMap<String, String>> mylist;
    /*    private ArrayList<User> list;*/

        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater = LayoutInflater.from(context);
            //  mylist = new ArrayList<HashMap<String, String>>();
            /*list = new ArrayList<>();*/
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
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
                view = inflater.inflate(R.layout.haoyou_item, null);
            }
            final User user = list.get(position);
            LinearLayout ll_haoyou_item = view.findViewById(R.id.ll_haoyou_item);
            LinearLayout llCatalog = view.findViewById(R.id.llCatalog);
            TextView tvCatalog = view.findViewById(R.id.tvCatalog);
            ImageView iv_haoyou_item = view.findViewById(R.id.iv_haoyou_item);
            TextView tv_haoyou_item_name = view.findViewById(R.id.tv_haoyou_item_name);
            final CheckBox ck_haoyou = view.findViewById(R.id.ck_haoyou);
            Glide.with(XzlxrActivity.this)
                    .load( Api.sUrl+ list.get(position).getHeadimgurl())
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.mipmap.error)
                    .error(R.mipmap.error)
                    .into(iv_haoyou_item);
            tv_haoyou_item_name.setText(list.get(position).getName());
            ck_haoyou.setVisibility(View.VISIBLE);
            if (arrayList.get(position).equals("0")) {
                ck_haoyou.setChecked(false);
            } else {
                ck_haoyou.setChecked(true);
            }
            ck_haoyou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ck_haoyou.isChecked()) {
                        arrayList.set(position, "1");
                    } else {
                        arrayList.set(position, "0");
                    }
                }
            });

           /* ll_haoyou_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(XzlxrActivity.this, HyxqActivity.class);
                    sId = list.get(position).getId();
                    startActivity(intent);
          *//*          RongIM.getInstance().startPrivateChat(HaoyouActivity.this, mylist.get(position).get("friend_id").toString(), mylist.get(position).get("nickname").toString());
              *//*
                }
            });*/
            //根据position获取首字母作为目录catalog
            String catalog = list.get(position).getFirstLetter();
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(catalog)) {
                llCatalog.setVisibility(View.VISIBLE);
                tvCatalog.setText(user.getFirstLetter().toUpperCase());
            } else {
                llCatalog.setVisibility(View.GONE);
            }


            return view;
        }

        /**
         * 获取catalog首次出现位置
         */
        public int getPositionForSection(String catalog) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = list.get(i).getFirstLetter();
                if (catalog.equalsIgnoreCase(sortStr)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
