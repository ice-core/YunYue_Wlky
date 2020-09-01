package com.example.administrator.yunyue.sc_fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.dialog.HintDialog;
import com.example.administrator.yunyue.dialog.LoadingDialog;
import com.example.administrator.yunyue.sc.Validator;
import com.example.administrator.yunyue.utils.NullTranslator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZhuceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZhuceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZhuceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String goods_id;
    private String data;

    private static final String TAG = ZhuceFragment.class.getSimpleName();
    private Button bt_zhuce_hqyzm;
    private TimeCount time;
    Validator validator = new Validator();
    private EditText et_denglu_phone;

    private EditText et_zhuce_yzm;
    private EditText et_zhuce_mima;
    private EditText et_zhuce_querenmima;
    private Button bt_zhuce_zc;
    private EditText et_zhuce_yqm;

    public ZhuceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZhuceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZhuceFragment newInstance(String param1, String param2) {
        ZhuceFragment fragment = new ZhuceFragment();
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            goods_id = bundle.getString("goods_id");
            data = bundle.getString("data");
        }
        return inflater.inflate(R.layout.fragment_zhuce, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);

        time = new TimeCount(60000, 1000);
        bt_zhuce_hqyzm = view.findViewById(R.id.bt_zhuce_hqyzm);
        et_denglu_phone = view.findViewById(R.id.et_denglu_phone);
        et_zhuce_yzm = view.findViewById(R.id.et_zhuce_yzm);
        et_zhuce_mima = view.findViewById(R.id.et_zhuce_mima);
        et_zhuce_querenmima = view.findViewById(R.id.et_zhuce_querenmima);
        bt_zhuce_zc = view.findViewById(R.id.bt_zhuce_zc);
        et_zhuce_yqm = view.findViewById(R.id.et_zhuce_yqm);
        et_zhuce_querenmima.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        et_zhuce_mima.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        bt_zhuce_hqyzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_denglu_phone.getText().toString())) {
                    hideDialogin();
                    dialogin("");
                    yanzhengma();
                    time.start();
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    //  Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_zhuce_zc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validator.isMobile(et_denglu_phone.getText().toString())) {
                    if (et_zhuce_yzm.getText().equals("")) {
                        Hint("请输入验证码", HintDialog.WARM);
                        // Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();

                    } else {
                        if (et_zhuce_mima.getText().toString().equals("")) {
                            Hint("请输入密码", HintDialog.WARM);
                            // Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        } else {
                            if (et_zhuce_querenmima.getText().toString().equals("")) {
                                Hint("请输入确认密码", HintDialog.WARM);
                                // Toast.makeText(RegisterActivity.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                            } else {
                                hideDialogin();
                                dialogin("");
                                zhuce();
                            }
                        }
                    }
                } else {
                    Hint("请输入正确的手机号", HintDialog.WARM);
                    // Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Hint(String hint) {
        final Dialog dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        final View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_hint, null);
        final ImageView iv_hint_dialog_img = inflate.findViewById(R.id.iv_hint_dialog_img);
        final TextView tv_hint_dialog_message = inflate.findViewById(R.id.tv_hint_dialog_message);
        TextView btn_hint_dialog_confirm = inflate.findViewById(R.id.btn_hint_dialog_confirm);
        iv_hint_dialog_img.setImageResource(R.drawable.success);
        tv_hint_dialog_message.setText(hint);
        btn_hint_dialog_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

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

    private void initialize() {
        et_zhuce_yzm.setText("");
        et_zhuce_mima.setText("");
        et_zhuce_querenmima.setText("");

        bt_zhuce_hqyzm.setBackgroundResource(R.drawable.login);
    }


    private void yanzhengma() {
        String url = Api.sUrl + "Login/dxSendCode/";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", et_denglu_phone.getText().toString());
        params.put("type", "2");
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
                                et_zhuce_yzm.setText("");
                                Hint(resultMsg, HintDialog.SUCCESS);
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
                    Log.e(TAG, error.getMessage(), error);
                }
            }
        });
        requestQueue.add(jsonRequest);
    }

    private void zhuce() {
        String url = Api.sUrl + "Login/register/";
        String a = et_zhuce_yqm.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", et_denglu_phone.getText().toString());
        params.put("dxcode", et_zhuce_yzm.getText().toString());
        params.put("password", et_zhuce_mima.getText().toString());
        params.put("checkpassword", et_zhuce_querenmima.getText().toString());
        params.put("invitation_code", et_zhuce_yqm.getText().toString());

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
                                initialize();
                                Hint(resultMsg);
                            } else {
                                Hint(resultMsg, HintDialog.ERROR);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());
                        //  Toast.makeText(RegisterActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
             //   Hint(error.getMessage(), HintDialog.ERROR);
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
                    Log.e(TAG, error.getMessage(), error);
                }
                Log.e(TAG, error.getMessage(), error);
            }
        });
        requestQueue.add(jsonRequest);
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bt_zhuce_hqyzm.setBackgroundResource(R.drawable.jf);
            bt_zhuce_hqyzm.setClickable(false);
            bt_zhuce_hqyzm.setText("  " + millisUntilFinished / 1000 + "秒后重新发送  ");
        }

        @Override
        public void onFinish() {
            bt_zhuce_hqyzm.setText(" 重新获取验证码 ");
            bt_zhuce_hqyzm.setClickable(true);
            bt_zhuce_hqyzm.setBackgroundResource(R.drawable.login);

        }
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
