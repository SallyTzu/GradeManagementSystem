package com.codeying.utils;

import com.mysql.cj.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//工具类，常用工具在此。
public class Utils {

    public static String TIME_ERROR = "时间格式错误，正确的格式为:yyyy-MM-dd hh:mm:ss。";
    public static String DIGIT_ERROR = "输入数字格式错误。";
    //字符串转int
    public static Integer strToInt(String str){
        Integer integer = null;
        try {
            integer = Integer.valueOf(str);
        }catch (Exception e){}
        return integer;
    }
    public static Double strToDouble(String str){
        Double a = null;
        try {
            a = Double.valueOf(str);
        }catch (Exception e){}
        return a;
    }

    /**
     * 字符串转日期
     * @param str
     * @return
     */
    public static Date strToDate(String str) {
        if(StringUtils.isNullOrEmpty(str)){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
            return date;
        } catch (ParseException e) {
        }
        format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(str);
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * 日期转字符串
     */
    public static String dateToStr(Date date){
        if(date==null){
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

	private static int n = 10000000;

    /**
     * 获取id，根据时间和五位随机数
     * @return
     */
    public static String getRandomIdByTime() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        n++;
        if(n> (1<<26) ){
            n=10000000;
        }
        return str+n;
    }
    public static String newId(){
//        return UUID.randomUUID().toString().replaceAll("-","");
        return getRandomIdByTime();
    }


}

