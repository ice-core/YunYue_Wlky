package com.example.administrator.yunyue.sc_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.erci.activity.CwhyActivity;
import com.example.administrator.yunyue.erci.activity.GrzlActivity;
import com.example.administrator.yunyue.erci.activity.WdhyActivity;
import com.example.administrator.yunyue.erci.activity.YjfkActivity;
import com.example.administrator.yunyue.fjsc_activity.Fjsc_ShouyeActivity;
import com.example.administrator.yunyue.fjsc_activity.Fjsc_WdddActivity;
import com.example.administrator.yunyue.hyzx_activity.HyzxActivity;
import com.example.administrator.yunyue.image.ImagePagerActivity;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.sc_activity.BzyfkActivity;
import com.example.administrator.yunyue.sc_activity.CjhdActivity;
import com.example.administrator.yunyue.sc_activity.HbkjActivity;
import com.example.administrator.yunyue.sc_activity.Sc_WdddActivity;
import com.example.administrator.yunyue.sc_activity.ShdzActivity;
import com.example.administrator.yunyue.sc_activity.ShoucangActivity;
import com.example.administrator.yunyue.sc_activity.ShouhuolbActivity;
import com.example.administrator.yunyue.sc_activity.SjczActivity;
import com.example.administrator.yunyue.sc_activity.SjlbActivity;
import com.example.administrator.yunyue.sc_activity.SpxqActivity;
import com.example.administrator.yunyue.sc_activity.WdqbActivity;
import com.example.administrator.yunyue.sc_activity.XiaoxiActivity;
import com.example.administrator.yunyue.sc_activity.ZhszActivity;
import com.example.administrator.yunyue.utils.NullTranslator;
import com.example.administrator.yunyue.yjdt_activity.FenleiLiebiaoActivity;
import com.example.administrator.yunyue.yjdt_activity.WdtgActivity;
import com.example.administrator.yunyue.yjdt_activity.YjdtActivity;
import com.example.administrator.yunyue.yjdt_activity.YjdtZfmmActivity;
import com.example.administrator.yunyue.zb_activity.WdzbActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WodeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WodeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SharedPreferences pref;
    private String sUser, sPassword, user_id, mobile;
    private String sIs_vip = "";
    private LinearLayout ll_wode_wdqb, ll_wode_sjcz, ll_wode_hbkj, ll_wode_cjhd, ll_wode_wdsc,
            ll_wode_shdz, ll_wode_sjrz, ll_wode_yqhy, ll_wode_bzyfk;
    private ImageView iv_wode_shezhi;
    private CircleImageView iv_wode_head_pic;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private SharedPreferences.Editor editor;
    private static final String TAG = WodeFragment.class.getSimpleName();
    private TextView tv_wode_user;
    private ImageView iv_wode_xiaoxi;
    private TextView tv_wode_qb;
    private LinearLayout ll_wode_dfk, ll_wode_dfh, ll_wode_dsh, ll_wode_dpl, ll_wode_sh;
    private TextView tv_wode_invitation;
    private TextView tv_wode_first_leader;
    private TextView tv_wode_level;
    private TextView tv_wode_jifen;
    RequestQueue queue = null;
    /**
     * 我的直播
     */
    private LinearLayout ll_wode_wdzb;
    /**
     * 附近商城
     */
    private LinearLayout ll_wode_fjsc;

    /**
     * 附近商城 订单
     */
    private LinearLayout ll_wode_fjsc_dd;

    /**
     * 我的发布
     */
    private LinearLayout ll_wode_wdfb;
    /**
     * 我的参与
     */
    private LinearLayout ll_wode_wdcy;


    /**
     * 支付密码
     */
    private LinearLayout ll_wode_zfmm;

    /**
     * 佣金大厅
     */
    private LinearLayout ll_wode_yjdt;
    /**
     * 我的推广
     */
    private LinearLayout ll_wode_wdtg;

    /**
     * 会员中心
     */
    private LinearLayout ll_wode_hyzx;
    /**
     * 认证信息
     */
    private LinearLayout ll_wode_rzxx;

    public WodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WodeFragment newInstance(String param1, String param2) {
        WodeFragment fragment = new WodeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wode, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        boolean isRemember = pref.getBoolean("user", false);
        sUser = pref.getString("account", "");
        sPassword = pref.getString("password", "");
        user_id = pref.getString("user_id", "");
        mobile = pref.getString("mobile", "");
        ll_wode_wdqb = view.findViewById(R.id.ll_wode_wdqb);
        ll_wode_sjcz = view.findViewById(R.id.ll_wode_sjcz);
        ll_wode_hbkj = view.findViewById(R.id.ll_wode_hbkj);
        ll_wode_cjhd = view.findViewById(R.id.ll_wode_cjhd);
        ll_wode_wdsc = view.findViewById(R.id.ll_wode_wdsc);
        ll_wode_shdz = view.findViewById(R.id.ll_wode_shdz);
        ll_wode_sjrz = view.findViewById(R.id.ll_wode_sjrz);
        ll_wode_yqhy = view.findViewById(R.id.ll_wode_yqhy);
        ll_wode_bzyfk = view.findViewById(R.id.ll_wode_bzyfk);
        iv_wode_shezhi = view.findViewById(R.id.iv_wode_shezhi);
        tv_wode_user = view.findViewById(R.id.tv_wode_user);
        iv_wode_xiaoxi = view.findViewById(R.id.iv_wode_xiaoxi);
        tv_wode_qb = view.findViewById(R.id.tv_wode_qb);
        tv_wode_jifen = view.findViewById(R.id.tv_wode_jifen);
        ll_wode_dfk = view.findViewById(R.id.ll_wode_dfk);
        ll_wode_dfh = view.findViewById(R.id.ll_wode_dfh);
        ll_wode_dsh = view.findViewById(R.id.ll_wode_dsh);
        ll_wode_dpl = view.findViewById(R.id.ll_wode_dpl);
        ll_wode_sh = view.findViewById(R.id.ll_wode_sh);
        ll_wode_wdzb = view.findViewById(R.id.ll_wode_wdzb);
        ll_wode_fjsc = view.findViewById(R.id.ll_wode_fjsc);
        ll_wode_fjsc_dd = view.findViewById(R.id.ll_wode_fjsc_dd);
        ll_wode_yjdt = view.findViewById(R.id.ll_wode_yjdt);
        ll_wode_zfmm = view.findViewById(R.id.ll_wode_zfmm);
        ll_wode_wdtg = view.findViewById(R.id.ll_wode_wdtg);
        ll_wode_wdfb = view.findViewById(R.id.ll_wode_wdfb);
        ll_wode_wdcy = view.findViewById(R.id.ll_wode_wdcy);
        ll_wode_hyzx = view.findViewById(R.id.ll_wode_hyzx);
        ll_wode_rzxx = view.findViewById(R.id.ll_wode_rzxx);
        ll_wode_wdcy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FenleiLiebiaoActivity.class);
                intent.putExtra("id", "");
                intent.putExtra("name", "我的参与");
                startActivity(intent);
            }
        });
        ll_wode_wdfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FenleiLiebiaoActivity.class);
                intent.putExtra("id", "");
                intent.putExtra("name", "我的发布");
                startActivity(intent);
            }
        });
        ll_wode_wdtg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WdtgActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_zfmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YjdtZfmmActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_yjdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YjdtActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_fjsc_dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Fjsc_WdddActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_fjsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Fjsc_ShouyeActivity.class);
                Fjsc_ShouyeActivity.sHint = "汇泉大厦";
                //维度
                Fjsc_ShouyeActivity.sLatitude = "31.252884";
                //经度
                Fjsc_ShouyeActivity.sLongitude = "121.424210";
                startActivity(intent);
            }
        });
        ll_wode_wdzb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WdzbActivity.class);
                startActivity(intent);
            }
        });
        tv_wode_invitation = view.findViewById(R.id.tv_wode_invitation);
        tv_wode_first_leader = view.findViewById(R.id.tv_wode_first_leader);
        tv_wode_level = view.findViewById(R.id.tv_wode_level);
        iv_wode_head_pic = (CircleImageView) view.findViewById(R.id.iv_wode_head_pic);
        tv_wode_qb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 0;
                startActivity(intent);
            }
        });
        ll_wode_dfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 1;
                startActivity(intent);
            }
        });
        ll_wode_dfh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 2;
                startActivity(intent);
            }
        });
        ll_wode_dsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 3;
                startActivity(intent);
            }
        });
        ll_wode_dpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Sc_WdddActivity.class);
                Sc_WdddActivity.iPosition = 4;
                startActivity(intent);
            }
        });
        ll_wode_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShouhuolbActivity.class);
                //intent.putExtra("id", "4");
                startActivity(intent);
            }
        });
        iv_wode_shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ZhszActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        ll_wode_wdqb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WdqbActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_sjcz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("alipays://platformapi/startapp?appId=10000003&url=/www/setNewAccount.htm?subBizTypeCZ"));
                startActivity(intent);*/
                Intent intent = new Intent(getActivity(), SjczActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_hbkj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HbkjActivity.class);
                startActivity(intent);
                // Hint("", HintDialog.WARM);
            }
        });
        ll_wode_cjhd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CjhdActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_wdsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShoucangActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_shdz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShdzActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_sjrz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SjlbActivity.class);
                startActivity(intent);
                /*   Hint("", HintDialog.WARM);*/
            }
        });
        ll_wode_yqhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent intent = new Intent(getActivity(), YqhyActivity.class);
                startActivity(intent);*/
                Hint("", HintDialog.WARM);
            }
        });
        ll_wode_bzyfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), YjfkActivity.class);
                startActivity(intent);
            }
        });
        iv_wode_xiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), XiaoxiActivity.class);
                startActivity(intent);
            }
        });
        ll_wode_hyzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //Intent intent = new Intent(getActivity(), HyzxActivity.class);
                    Intent intent = new Intent(getActivity(), WdhyActivity.class);
                    intent.putExtra("activity", "wd");
                    startActivity(intent);

            }
        });
        isViewCreated = true;
        lazyLoad();
        iv_wode_head_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<String> list = new ArrayList<>();
                list.add(Api.sUrl + pref.getString("head_pic", ""));
                ImagePagerActivity.startImagePagerActivity(getActivity(), list, 0, new ImagePagerActivity
                        .ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight()));
            }
        });
        ll_wode_rzxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GrzlActivity.class);
                startActivity(intent);
            }
        });

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
            hideDialogin();
            dialogin("");
            xiangqing();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
            //    printLog(mTextviewContent+"可见,加载数据");
        }
    }

    private void xiangqing() {
        String url = Api.sUrl + "Api/Login/udetails/appId/" + Api.sApp_Id + "/user_id/" + user_id + "/mobile/" + mobile;
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
                        JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                        String id = jsonObjectdate.getString("id");
                        String nickname = jsonObjectdate.getString("nickname");
                        String mobile = jsonObjectdate.getString("mobile");
                        String headimgurl = jsonObjectdate.getString("headimgurl");
                      //  sIs_vip = jsonObjectdate.getString("is_vip");
                        tv_wode_user.setText(nickname);

                        Glide.with(getActivity())
                                .load(Api.sUrl + headimgurl)
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_wode_head_pic);
                        editor = pref.edit();
                      //  editor.putString("is_vip", sIs_vip);
                        editor.putString("head_pic", headimgurl);
                        editor.putString("nickname", nickname);
                        editor.putString("mobile", mobile);
                        editor.putString("sex", mobile);

                        editor.apply();
                    } else {

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
