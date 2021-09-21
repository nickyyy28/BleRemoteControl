package com.example.rm.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTool {
    /**
     * 时间字符串
     * @return
     */
    public static String getTime(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date =  new Date(System.currentTimeMillis());
        return sdf.format(date);

    }
    /**
     * 格式化时间
     *
     */
    public static String getStandardTime(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date =  new Date(System.currentTimeMillis());
        return sdf.format(date);

    }


    /**
     * 格式化时间,精确到毫秒
     *
     */
    public static String getPrecisionStandardTime(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Date date =  new Date(System.currentTimeMillis());
        return sdf.format(date);

    }



    /**
     * 获取当前的时间戳
     */
    public static long getTimestamp(){
        return System.currentTimeMillis();
    }



}