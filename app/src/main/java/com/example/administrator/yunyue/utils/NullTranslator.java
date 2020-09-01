package com.example.administrator.yunyue.utils;

import java.util.Collection;

/**
 * Created by crom.lai on 2018/2/3.
 */

public class NullTranslator {

    private NullTranslator() {
    }
    /**
     * convert null to ""
     * @param para
     * @return
     */
    public static String avoidNull(String para) {
        if(para==null) return "";
        return para;
    }
    /**
     * if para is null, return ss, else return para itself
     * @param para
     * @param ss
     * @return
     */
    public static String avoidNull(Object para,String ss) {
        if(para==null) return ss;
        return (String)para;
    }
    /**
     *  convert null to ""
     * @param para
     * @return
     */
    public static String avoidNull(Object para) {
        if(para==null) return "";
        return para.toString();
    }

    /**
     * if obj is null, return replace if replacer is not null, if replace is null, return null
     * @param obj
     * @param replacer
     * @return
     */
    public static String convertNullIntoString(Object obj, String replacer) {
        if (obj == null) {
            if (replacer == null) {
                return "";
            } else {
                return replacer;
            }
        } else {
            return obj.toString();
        }
    }

    /**
     * if obj is null or obj is an empty String, return true, else return false
     * @param ob
     * @return
     */
    public static boolean isNullEmpty(Object obj)
    {
        return obj==null || obj.toString().isEmpty();
    }

    /**
     * check if a collection is null or empty
     * @param collection
     * @return
     */
    public static boolean isNullEmptyCollection(Collection<?> collection)
    {
        return null==collection || collection.isEmpty();
    }
}
