package com.ai.frame.dubbo.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * 时间日期工具类
 * @author yiyun
 *
 */
public class DateUtil {
	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static String YYYY_MM_DD = "yyyy-MM-dd";
	public static String YYYYMMDD = "yyyyMMdd";
	public static String YYYYMMDDHHMMSS  = "yyyyMMddHHmmss";
	public static String YYYYMMDDHHMMSS1 = "yyyyMMdd HHmmss";

	/**比较两个日期之前的差值,date1比date2早则返回负数*/
	public static long compareDate(Date date1,Date date2){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		
		return c1.getTimeInMillis() - c2.getTimeInMillis();
	}
	/**比较给定日期与当前日期之间的差值,比当前日期早返回正数*/
	public static long compareDate(Date date){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date);
		
		return c1.getTimeInMillis() - c2.getTimeInMillis();
	}
	/**
	 * 取得当前日期时间
	 * @return 返回yyyyMMdd HHmmss格式日期字符串
	 */
	public static String getCurrentTime1(){
//        Calendar cl = Calendar.getInstance();
//        return date2String(cl.getTime(),YYYYMMDDHHMMSS1);
        return getCurrentDate(YYYYMMDDHHMMSS1);
    }
	/**
	 * 取得当前日期时间
	 * @return 返回yyyyMMddHHmmss格式日期字符串
	 */
	public static String getCurrentTime(){
//	    Calendar cl = Calendar.getInstance();
//	    return date2String(cl.getTime(),YYYYMMDDHHMMSS);
	    return getCurrentDate(YYYYMMDDHHMMSS);
	}
	/**
	 * 取得当前日期
	 * @pattern 默认yyyy-MM-dd HH:mm:ss
	 * @return 返回pattern格式日期字符串
	 */
	public static String getCurrentDate(String pattern){
		if(StringUtil.isEmpty(pattern)){pattern = YYYY_MM_DD_HH_MM_SS;}
		Calendar cl = Calendar.getInstance();
        
		return date2String(cl.getTime(),pattern);
	}
	
	/**
	 * 日期转换成日期字符串
	 * @param date
	 * @param pattern 要转成的日期字符串格式
	 * @return
	 */
	public static String date2String(Date date, String pattern) {
		return date2String(date,pattern,null);
	}
	
	/**
	 * 日期转换成日期字符串
	 * @param date
	 * @param pattern 要转成的日期字符串格式
	 * @param defaultDate 转失败后的默认字符串日期
	 * @return
	 */
	public static String date2String(Date date, String pattern,String defaultDate) {
		try{
			pattern = pattern == null ? YYYY_MM_DD_HH_MM_SS : pattern;
			return new SimpleDateFormat(pattern).format(date);
		}catch(Exception e){
			return defaultDate;
		}
	}
	/**
	 * 日期字符串转成Calendar日期对象
	 * @param strtime
	 * @return
	 */
	public static Calendar string2Calendar(String strtime){
        Date d = string2Date(strtime);
        if(d != null){
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c;
        }
        return null;
    }
	
	/**
	 * 日期字符串转成日期对象
	 * @param date    日期字符串
	 * @return
	 */
	public static Date string2Date(String date) {
		return string2Date(date,YYYY_MM_DD_HH_MM_SS);
	}
	/**
	 * 日期字符串转成日期对象
	 * @param date    日期字符串
	 * @param pattern 日期字符串的格式
	 * @return
	 */
	public static Date string2Date(String date,String pattern) {
		return string2Date(date,pattern,null);
	}
	/**
	 * 日期字符串转成日期对象
	 * @param date        日期字符串
	 * @param pattern     日期字符串的格式
	 * @param defaultDate 转失败后的默认日期
	 * @return
	 */
	public static Date string2Date(String date,String pattern,Date defaultDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			return defaultDate;
		}
	}
}
