package com.codeying.utils;

public class MyUtils {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(Object e){
        return !isNotEmpty(e);
    }

    public static boolean isNotEmpty(Object s){
        if(s == null){
            return false;
        }
        return isNotEmpty((String) s);
    }

    public static boolean isNotEmpty(String s){
        if(s==null || "".equals(s)){
            return false;
        }
        return true;
    }


}
