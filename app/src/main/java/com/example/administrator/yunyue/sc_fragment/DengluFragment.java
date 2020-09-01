package com.example.administrator.yunyue.sc_fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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
import com.example.administrator.yunyue.sc_activity.ScMainActivity;
import com.example.administrator.yunyue.sc_activity.WjmmActivity;
import com.example.administrator.yunyue.utils.NullTranslator;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DengluFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DengluFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DengluFragment extends Fragment {
    private static final String TAG = DengluFragment.class.getSimpleName();
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

    private Button bt_login;
    private EditText et_login_name;
    private EditText et_login_mima;
    private String user_id = "";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String sUser, sPassword;
    private TextView tv_denglu_wjmm;
    RequestQueue queue = null;


    public DengluFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DengluFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DengluFragment newInstance(String param1, String param2) {
        DengluFragment fragment = new DengluFragment();
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
        return inflater.inflate(R.layout.fragment_denglu, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isRemember = pref.getBoolean("user", false);
        queue = Volley.newRequestQueue(getActivity());
        sUser = pref.getString("account", "");
        sPassword = pref.getString("password", "");
        et_login_name = view.findViewById(R.id.et_login_name);
        et_login_mima = view.findViewById(R.id.et_login_mima);
        tv_denglu_wjmm = view.findViewById(R.id.tv_denglu_wjmm);
        tv_denglu_wjmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WjmmActivity.class);
                startActivity(intent);
            }
        });
        et_login_mima.setTransformationMethod(PasswordTransformationMethod.getInstance());//密码隐藏
        bt_login = view.findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(LoginActivity.this, ScMainActivity.class);
                //startActivity(intent);
                if (et_login_name.getText().toString().equals("")) {
                    Hint("请输入用户名", HintDialog.WARM);
                    //  Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (et_login_mima.getText().toString().equals("")) {
                    Hint("请输入密码", HintDialog.WARM);
                    //  Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    hideDialogin();
                    dialogin("");
                    login();

                    bt_login.setEnabled(false);
                }
            }
        });
        if (isRemember) {
            if (sPassword.equals("")) {
                et_login_name.setText(sUser);
            } else {
                Intent intent1 = new Intent(getActivity(), ScMainActivity.class);
                startActivity(intent1);
                getActivity().finish();
            }
        }
    }
    private void login() {
        String url = Api.sUrl + "Login/login/";
       // String url = "https://www.baidu.com/";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", et_login_name.getText().toString());
       params.put("password", et_login_mima.getText().toString());
        //  params.put("registrationID", JPushInterface.getRegistrationID(getApplicationContext()));
        JSONObject jsonObject = new JSONObject(params);
        //注意，这里的jsonObject一定要传到下面的构造方法中！下面的getParams（）似乎不会传到构造方法中
        final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        bt_login.setEnabled(true);
                        String sDate = response.toString();
                        System.out.println(sDate);
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = new JSONObject(sDate);
                            String resultMsg = jsonObject1.getString("msg");
                            int resultCode = jsonObject1.getInt("code");
                            if (resultCode > 0) {
                                main(sDate);
                                //   Hint(resultMsg, HintDialog.SUCCESS);
                            } else {
                                hideDialogin();
                                Hint(resultMsg, HintDialog.ERROR);
                                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            hideDialogin();
                            e.printStackTrace();
                        }
                        Log.d(TAG, "response -> " + response.toString());


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialogin();
                bt_login.setEnabled(true);
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
                //  Hint(error.toString(), HintDialog.ERROR);
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }

    private void main(String data) {
        System.out.println(data);
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject(data);
            String resultMsg = jsonObject1.getString("msg");
            int resultCode = jsonObject1.getInt("code");
            if (resultCode > 0) {
                JSONObject jsonObjectdate = jsonObject1.getJSONObject("data");
                user_id = jsonObjectdate.getString("user_id");
                String is_email = jsonObjectdate.getString("is_email");
                String is_mobile = jsonObjectdate.getString("is_mobile");
                String is_auth = jsonObjectdate.getString("is_auth");
                String head_pic = jsonObjectdate.getString("head_pic");
                String nickname = jsonObjectdate.getString("nickname");
                String message_mask = jsonObjectdate.getString("message_mask");
                String user_money = jsonObjectdate.getString("user_money");
                String password = jsonObjectdate.getString("password");
                String number = jsonObjectdate.getString("number");
                String is_baoan = jsonObjectdate.getString("is_baoan");
                String is_card_img = jsonObjectdate.getString("is_card_img");
                editor = pref.edit();
                editor.putBoolean("user", true);
                editor.putString("account", number);
                editor.putString("password", password);
                editor.putString("user_id", user_id);
                editor.putString("is_email", is_email);
                editor.putString("is_mobile", is_mobile);
                editor.putString("is_auth", is_auth);
                editor.putString("head_pic", head_pic);
                editor.putString("nickname", nickname);
                editor.putString("message_mask", message_mask);
                editor.putString("user_money", user_money);
                editor.putString("mobile", jsonObjectdate.getString("mobile"));
                editor.putString("is_baoan", is_baoan);
                editor.putString("is_card_img", is_card_img);
                editor.apply();
                Intent intent = new Intent(getActivity(), ScMainActivity.class);
                startActivity(intent);
                getActivity().finish();
                hideDialogin();

                //   Hint(resultMsg, HintDialog.SUCCESS);
            } else {
                Hint(resultMsg, HintDialog.ERROR);
                //   Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
