package com.example.administrator.yunyue.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2019/5/31 0031.
 */

public class TxddGridData implements Serializable {
    /**
     * 商品信息
     */
    public ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

    // public List<String> urlList = new ArrayList<>();
    /**
     * 店铺名称id
     */
    public String sName_id = "";
    /**
     * 店铺名称
     */
    public String sName = "";
    /**
     * 留言
     */
    public String sLiuyan = "";
    /**
     * 发票类型
     */
    public String sFapiao_Type = "";
    /**
     * 发票——单位名称
     */
    public String sFapiao_Name = "";
    /**
     * 发票——纳税号
     */
    public String sFapiao_Nsh = "";
    /**
     * 优惠卷id
     */
    public String sYhj_id = "";
    /**
     * 优惠卷
     */
    public String sYhj_name = "";
    /**
     * 优惠卷数
     */
    public String sYhj_num = "";

    /**
     * 优惠卷-满减
     */
    public String sYhj_amount = "";

    /**
     * 购物车Id
     */
    public String sGwc_id = "";


    /**
     * 规格Id
     */
    public String sGg_id = "";
    /**
     * 商品数量Id
     */
    public String sNum = "";

    /**
     * 运费
     */
    public String sYf = "";


}
