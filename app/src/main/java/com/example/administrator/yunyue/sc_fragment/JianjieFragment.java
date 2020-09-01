package com.example.administrator.yunyue.sc_fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.administrator.yunyue.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JianjieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JianjieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JianjieFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String goods_id;
    private String data;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tv_jianjie_name;
    private TextView tv_jianjie_money;
    private TextView tv_jianjie_remark;
    private TextView tv_jianjie_content;

    private OnFragmentInteractionListener mListener;

    public JianjieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JianjieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JianjieFragment newInstance(String param1, String param2) {
        JianjieFragment fragment = new JianjieFragment();
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
        return inflater.inflate(R.layout.fragment_jianjie, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);
        tv_jianjie_name = view.findViewById(R.id.tv_jianjie_name);
        tv_jianjie_money = view.findViewById(R.id.tv_jianjie_money);
        tv_jianjie_remark = view.findViewById(R.id.tv_jianjie_remark);
        tv_jianjie_content = view.findViewById(R.id.tv_jianjie_content);
        data(data);
    }


    private void data(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String resultMsg = jsonObject.getString("msg");
            int resultCode = jsonObject.getInt("code");
            if (resultCode > 0) {
                String resultData = jsonObject.getString("data");
                JSONObject jsonObjectData = new JSONObject(resultData);
                String resultgoods = jsonObjectData.getString("goods");
                JSONObject jsonObjectgoods = new JSONObject(resultgoods);
                String resultgoods_name = jsonObjectgoods.getString("goods_name");
                String resultshop_price = jsonObjectgoods.getString("shop_price");
                String resultgoods_remark = jsonObjectgoods.getString("goods_remark");
                String resultgoods_content = jsonObjectgoods.getString("goods_content");
                tv_jianjie_name.setText(resultgoods_name);
                tv_jianjie_money.setText("￥" + resultshop_price);
                tv_jianjie_remark.setText(resultgoods_remark);
                tv_jianjie_content.setText(resultgoods_content);


            } else {

            }
        } catch (
                JSONException e)

        {

            e.printStackTrace();
        }
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
