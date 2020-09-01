package com.example.administrator.yunyue.sc_fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.yunyue.Api;
import com.example.administrator.yunyue.R;
import com.example.administrator.yunyue.sc.CircleImageView;
import com.example.administrator.yunyue.sc_gridview.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PinglunFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PinglunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PinglunFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyGridView gv_pinglun;


    private String user_id;
    private String goods_id;
    private String data;
    private TextView tv_pinglun_goods_name;
    private TextView tv_pinglun_collect_sum;
    private ImageView iv_pl_rank, iv_pl_rank1, iv_pl_rank2, iv_pl_rank3, iv_pl_rank4;
    private CircleImageView iv_pl_head_pic, iv_pl_head_pic1, iv_pl_head_pic2, iv_pl_head_pic3;
    private TextView tv_pl_head_pic;
    private ImageView iv_pl_head_pic4;

    private OnFragmentInteractionListener mListener;

    public PinglunFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PinglunFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PinglunFragment newInstance(String param1, String param2) {
        PinglunFragment fragment = new PinglunFragment();
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
        return inflater.inflate(R.layout.fragment_pinglun, container, false);
    }

    public void onViewCreated(View view, Bundle saveInstancesState) {
        super.onViewCreated(view, saveInstancesState);

        gv_pinglun = (MyGridView) view.findViewById(R.id.gv_pinglun);
        tv_pinglun_goods_name = (TextView) view.findViewById(R.id.tv_pinglun_goods_name);
        tv_pinglun_collect_sum = (TextView) view.findViewById(R.id.tv_pinglun_collect_sum);
        iv_pl_rank = (ImageView) view.findViewById(R.id.iv_pl_rank);
        iv_pl_rank1 = (ImageView) view.findViewById(R.id.iv_pl_rank1);
        iv_pl_rank2 = (ImageView) view.findViewById(R.id.iv_pl_rank2);
        iv_pl_rank3 = (ImageView) view.findViewById(R.id.iv_pl_rank3);
        iv_pl_rank4 = (ImageView) view.findViewById(R.id.iv_pl_rank4);
        iv_pl_head_pic4 = (ImageView) view.findViewById(R.id.iv_pl_head_pic4);
        iv_pl_head_pic = (CircleImageView) view.findViewById(R.id.iv_pl_head_pic);
        iv_pl_head_pic1 = (CircleImageView) view.findViewById(R.id.iv_pl_head_pic1);
        iv_pl_head_pic2 = (CircleImageView) view.findViewById(R.id.iv_pl_head_pic2);
        iv_pl_head_pic3 = (CircleImageView) view.findViewById(R.id.iv_pl_head_pic3);
        iv_pl_head_pic.setVisibility(View.GONE);
        iv_pl_head_pic1.setVisibility(View.GONE);
        iv_pl_head_pic2.setVisibility(View.GONE);
        iv_pl_head_pic3.setVisibility(View.GONE);
        tv_pl_head_pic = (TextView) view.findViewById(R.id.tv_pl_head_pic);

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
                String resultrank = jsonObjectData.getString("rank");
                if (resultrank.equals("1")) {
                    iv_pl_rank.setImageResource(R.mipmap.shouchang_xz);
                } else if (resultrank.equals("2")) {
                    iv_pl_rank.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank1.setImageResource(R.mipmap.shouchang_xz);
                } else if (resultrank.equals("3")) {
                    iv_pl_rank.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank1.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank2.setImageResource(R.mipmap.shouchang_xz);
                } else if (resultrank.equals("4")) {
                    iv_pl_rank.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank1.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank2.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank3.setImageResource(R.mipmap.shouchang_xz);
                } else if (resultrank.equals("5")) {
                    iv_pl_rank.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank1.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank2.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank3.setImageResource(R.mipmap.shouchang_xz);
                    iv_pl_rank4.setImageResource(R.mipmap.shouchang_xz);
                }
                JSONObject jsonObjectgoods = new JSONObject(resultgoods);
                String resultgoods_name = jsonObjectgoods.getString("goods_name");
                String resultcollect_sum = jsonObjectgoods.getString("collect_sum");
                tv_pinglun_goods_name.setText(resultgoods_name);
                tv_pinglun_collect_sum.setText(resultcollect_sum + "人喜欢这款商品");

                JSONArray resultJsonArraycollectList = jsonObjectData.getJSONArray("collectList");
                ArrayList<HashMap<String, Object>> mylistcollectList = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < resultJsonArraycollectList.length(); i++) {
                    JSONObject jsonObjecti = resultJsonArraycollectList.getJSONObject(i);
                    if (i == 0) {
                        iv_pl_head_pic.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load( Api.sUrl+ jsonObjecti.getString("head_pic"))
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_pl_head_pic);
                    } else if (i == 1) {
                        iv_pl_head_pic1.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load( Api.sUrl+ jsonObjecti.getString("head_pic"))
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_pl_head_pic1);
                    } else if (i == 2) {
                        iv_pl_head_pic2.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load( Api.sUrl+ jsonObjecti.getString("head_pic"))
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_pl_head_pic2);
                    } else if (i == 3) {
                        iv_pl_head_pic3.setVisibility(View.VISIBLE);
                        Glide.with(getActivity())
                                .load( Api.sUrl+ jsonObjecti.getString("head_pic"))
                                .asBitmap()
                                .placeholder(R.mipmap.error)
                                .error(R.mipmap.error)
                                .dontAnimate()
                                .into(iv_pl_head_pic3);
                    }
                }
                if (resultJsonArraycollectList.length() > 4) {
                    int collectnum = resultJsonArraycollectList.length() - 4;
                    tv_pl_head_pic.setText("+" + collectnum);
                    iv_pl_head_pic4.setVisibility(View.VISIBLE);
                } else {
                    iv_pl_head_pic4.setVisibility(View.GONE);
                }
                JSONArray resultJsonArray = jsonObjectData.getJSONArray("commentList");
                ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
                for (int i = 0; i < resultJsonArray.length(); i++) {
                    JSONObject jsonObjecti = resultJsonArray.getJSONObject(i);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("username", jsonObjecti.getString("username"));
                    map.put("content", jsonObjecti.getString("content"));
                    map.put("goods_rank", jsonObjecti.getString("goods_rank"));
                    mylist.add(map);
                }
                MyAdapter myAdapter = new MyAdapter(getActivity());
                myAdapter.arrmylist = mylist;
                gv_pinglun.setAdapter(myAdapter);

            } else {
                //  Hint(resultMsg, HintDialog.ERROR);
            }
        } catch (
                JSONException e) {
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

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater1;
        public ArrayList<HashMap<String, Object>> arrmylist;


        public MyAdapter(Context context) {
            super();
            this.context = context;
            inflater1 = LayoutInflater.from(context);
            arrmylist = new ArrayList<HashMap<String, Object>>();

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrmylist.size();
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
                view = inflater1.inflate(R.layout.pinglun_item, null);
            }
            TextView tv_pinglun_username = (TextView) view.findViewById(R.id.tv_pinglun_username);
            TextView tv_pinglun_content = (TextView) view.findViewById(R.id.tv_pinglun_content);
            ImageView iv_pinglun_rank = (ImageView) view.findViewById(R.id.iv_pinglun_rank);
            ImageView iv_pinglun_rank1 = (ImageView) view.findViewById(R.id.iv_pinglun_rank1);
            ImageView iv_pinglun_rank2 = (ImageView) view.findViewById(R.id.iv_pinglun_rank2);
            ImageView iv_pinglun_rank3 = (ImageView) view.findViewById(R.id.iv_pinglun_rank3);
            ImageView iv_pinglun_rank4 = (ImageView) view.findViewById(R.id.iv_pinglun_rank4);


            tv_pinglun_username.setText(arrmylist.get(position).get("username").toString());
            tv_pinglun_content.setText(arrmylist.get(position).get("content").toString());

            if (arrmylist.get(position).get("goods_rank").toString().equals("1")) {
                iv_pinglun_rank.setImageResource(R.mipmap.shouchang_xz);
            } else if (arrmylist.get(position).get("goods_rank").toString().equals("2")) {
                iv_pinglun_rank.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank1.setImageResource(R.mipmap.shouchang_xz);
            } else if (arrmylist.get(position).get("goods_rank").toString().equals("3")) {
                iv_pinglun_rank.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank1.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank2.setImageResource(R.mipmap.shouchang_xz);
            } else if (arrmylist.get(position).get("goods_rank").toString().equals("4")) {
                iv_pinglun_rank.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank1.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank2.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank3.setImageResource(R.mipmap.shouchang_xz);
            } else if (arrmylist.get(position).get("goods_rank").toString().equals("5")) {
                iv_pinglun_rank.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank1.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank2.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank3.setImageResource(R.mipmap.shouchang_xz);
                iv_pinglun_rank4.setImageResource(R.mipmap.shouchang_xz);
            }


            return view;
        }
    }

}
