package com.example.administrator.yunyue.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2019/5/31 0031.
 */

public class SpflGridData implements Serializable {
    /**
     * 子分类
     */
    public ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

    /**
     * 店铺名称
     */
    public String sName = "";

}
