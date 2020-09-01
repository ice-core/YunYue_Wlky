package com.example.administrator.yunyue.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.KyxqXqActivity;
import com.example.administrator.yunyue.erci.activity.LljlActivity;
import com.example.administrator.yunyue.erci.activity.RwzxActivity;
import com.example.administrator.yunyue.erci.activity.WdmpActivity;
import com.example.administrator.yunyue.erci.activity.WdtdActivity;
import com.example.administrator.yunyue.erci.activity.YjfkActivity;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.sc_activity.HbkjActivity;
import com.example.administrator.yunyue.sc_activity.Sc_WdddActivity;
import com.example.administrator.yunyue.sc_activity.ShoucangActivity;
import com.example.administrator.yunyue.sc_activity.ShouhuolbActivity;
import com.example.administrator.yunyue.sc_activity.WdqbActivity;
import com.example.administrator.yunyue.sc_activity.ZhszActivity;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rong.imageloader.utils.L;
import master.flame.danmaku.danmaku.model.FBDanmaku;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GrzxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrzxFragment extends Fragment {
    private static final String TAG = GrzxFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GrzxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GrzxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GrzxFragment newInstance(String param1, String param2) {
        GrzxFragment fragment = new GrzxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    /**
     * 用户头像
     */
    private CircleImageView civ_grzx_headimgurl;
    /**
     * 用户昵称
     */
    private TextView tv_grzx_nickname;
    /**
     * 共享金
     */
    private TextView tv_grzx_corpus;
    /**
     * 积分
     */
    private TextView tv_grzx_jifen;
    /***
     * 工分
     */
    private TextView tv_grzx_workpoints;
    private LinearLayout ll_grzx_workpoints;
    /**
     * 总收益
     */
    private TextView tv_grzx_totalEarnings;
    /**
     * 到账收益
     */
    private TextView tv_grzx_earnings;
    private SharedPreferences pref;
    private String sUser_id = "";
    /**
     * 全部订单
     */
    private TextView tv_grzx_qb;
    /**
     * 待付款
     */
    private LinearLayout ll_grzx_dfk;
    /**
     * 待发货
     */
    private LinearLayout ll_grzx_dfh;
    /**
     * 待收货
     */
    private LinearLayout ll_grzx_dsh;
    /**
     * 待评论
     */
    private LinearLayout ll_grzx_dpl;
    /**
     * 售后
     */
    private LinearLayout ll_grzx_sh;
    /**
     * 我的名片
     */
    private LinearLayout ll_grzx_wdmp;
    /**
     * 我的团队
     */
    private LinearLayout ll_grzx_wdtd;
    /**
     * 浏览记录
     */
    private LinearLayout ll_grzx_lljl;
    /**
     * 任务中心
     */
    private LinearLayout ll_grzx_rwzx;
    /**
     * 意见反馈
     */
    private LinearLayout ll_grzx_yjfk;
    /**
     * 设置
     */
    private ImageView iv_grzx_sz;

    /**
     * 我的收藏
     */
    private LinearLayout ll_grzx_wdsc;
    /**
     * 我的钱包
     */
    private LinearLayout ll_grzx_wdqb;
    /**
     * 我的优惠卷
     */
    private LinearLayout ll_grzx_wdyhj;
    /**
     * 信息公告
     */
    private LinearLayout ll_grzx_xxgg;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grzx, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sUser_id = pref.getString("user_id", "");
        civ_grzx_headimgurl = view.findViewById(R.id.civ_grzx_headimgurl);
        tv_grzx_nickname = view.findViewById(R.id.tv_grzx_nickname);
        tv_grzx_corpus = view.findViewById(R.id.tv_grzx_corpus);
        tv_grzx_jifen = view.findViewById(R.id.tv_grzx_jifen);
        tv_grzx_workpoints = view.findViewById(R.id.tv_grzx_workpoints);
        tv_grzx_totalEarnings = view.findViewById(R.id.tv_grzx_totalEarnings);
        tv_grzx_earnings = view.findViewById(R.id.tv_grzx_earnings);
        iv_grzx_sz = view.findViewById(R.id.iv_grzx_sz);
        iv_grzx_sz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZhszActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        ll_grzx_workpoints = view.findViewById(R.id.ll_grzx_workpoints);

        tv_grzx_qb = view.findViewById(R.id.tv_grzx_qb);
        tv_grzx_qb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 0;
                startActivity(intent);
            }
        });
        ll_grzx_dfk = view.findViewById(R.id.ll_grzx_dfk);
        ll_grzx_dfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 1;
                startActivity(intent);
            }
        });
        ll_grzx_dfh = view.findViewById(R.id.ll_grzx_dfh);
        ll_grzx_dfh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 2;
                startActivity(intent);
            }
        });
        ll_grzx_dsh = view.findViewById(R.id.ll_grzx_dsh);
        ll_grzx_dsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 3;
                startActivity(intent);
            }
        });
        ll_grzx_dpl = view.findViewById(R.id.ll_grzx_dpl);
        ll_grzx_dpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 4;
                startActivity(intent);
            }
        });
        ll_grzx_sh = view.findViewById(R.id.ll_grzx_sh);
        ll_grzx_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShouhuolbActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_wdmp = view.findViewById(R.id.ll_grzx_wdmp);
        ll_grzx_wdmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WdmpActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_wdtd = view.findViewById(R.id.ll_grzx_wdtd);
        ll_grzx_wdtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WdtdActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_lljl = view.findViewById(R.id.ll_grzx_lljl);
        ll_grzx_lljl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LljlActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_rwzx = view.findViewById(R.id.ll_grzx_rwzx);
        ll_grzx_rwzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RwzxActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_yjfk = view.findViewById(R.id.ll_grzx_yjfk);
        ll_grzx_yjfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YjfkActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_wdsc = view.findViewById(R.id.ll_grzx_wdsc);
        ll_grzx_wdsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShoucangActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_wdqb = view.findViewById(R.id.ll_grzx_wdqb);
        ll_grzx_wdqb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WdqbActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_wdyhj = view.findViewById(R.id.ll_grzx_wdyhj);
        ll_grzx_wdyhj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HbkjActivity.class);
                startActivity(intent);
            }
        });
        ll_grzx_xxgg = view.findViewById(R.id.ll_grzx_xxgg);
        ll_grzx_xxgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hint("敬请期待！", HintDialog.WARM);
            }
        });
        isViewCreated = true;
        lazyLoad();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            sUserInfo();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
            //    printLog(mTextviewContent+"可见,加载数据");
        }
    }

    /**
     * 方法名：sUserInfo()
     * 功  能：个人中心接口
     * 参  数：appId
     */
    private void sUserInfo() {
        hideDialogin();
        dialogin("");
        String url = Api.sUrl + Api.sUserInfo;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                String resultNickname = jsonObject1.getString("nickname");
                                String resultHeadimgurl = jsonObject1.getString("headimgurl");
                                String resultWorkpoints = jsonObject1.getString("workpoints");
                                String resultJifen = jsonObject1.getString("jifen");
                                String resultEarnings = jsonObject1.getString("earnings");
                                String resultTotalEarnings = jsonObject1.getString("totalEarnings");
                                String resultCorpus = jsonObject1.getString("corpus");

                                Glide.with(getActivity())
                                        .load( Api.sUrl+resultHeadimgurl)
                                        .asBitmap()
                                        .placeholder(R.mipmap.error)
                                        .error(R.mipmap.error)
                                        .dontAnimate()
                                        .into(civ_grzx_headimgurl);
                                tv_grzx_nickname.setText(resultNickname);
                                tv_grzx_corpus.setText("￥" + resultCorpus);
                                tv_grzx_jifen.setText(resultJifen);
                                tv_grzx_workpoints.setText(resultWorkpoints);
                                tv_grzx_totalEarnings.setText("￥" + resultTotalEarnings);
                                tv_grzx_earnings.setText("￥" + resultEarnings);
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
        loadingDialog = new LoadingDialog(getActivity(), R.style.dialog, msg, LoadingDialog.WAITING_C);
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
        new HintDialog(getActivity(), R.style.dialog, sHint, type, true).show();
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
