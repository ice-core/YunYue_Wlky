package com.example.administrator.yunyue.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.yunyue.R;


/**
 * Description：ViewPager切换的Fragment
 * <p>
 * Created by Mjj on 2016/11/19.
 */
public class NewsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View frView = inflater.inflate(R.layout.fragment_news, null);
        return frView;
    }
}
