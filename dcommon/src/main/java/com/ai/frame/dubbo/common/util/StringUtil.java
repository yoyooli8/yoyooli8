package com.ai.frame.dubbo.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 * 字符串工具类
 * @author  yiyun
 * @version 20150701
 */
public class StringUtil {
	public static final String EMPTYSTR = "";
	/**
	 * OBJ对象转换成str对象
	 * @param obj
	 * @return
	 */
	public static String obj2Str(Object obj) {
		if(obj == null)return null;
		if(obj instanceof String){
			return obj.toString().trim();
		}else if(obj instanceof Integer || 
				obj instanceof Double||
				obj instanceof Long||
				obj instanceof Boolean||
				obj instanceof Byte){
			
			return String.valueOf(obj);
		}else if(obj instanceof Date){
			String dateStr = DateUtil.date2String((Date)obj,DateUtil.YYYY_MM_DD_HH_MM_SS);
			if(StringUtil.isEmpty(dateStr)){
				dateStr    = DateUtil.date2String((Date)obj,DateUtil.YYYY_MM_DD);
			}
			return dateStr;
		}else if(obj instanceof List){
			@SuppressWarnings("rawtypes")
			List array = (List)obj;
			
			return arrayJoin(array.toArray(),null);
		}else if(obj.getClass().isArray()){
			Object[] array = (Object[])obj;
			
			return arrayJoin(array,null);
		}else{
			return null;
		}
	}
	public static String arrayJoin(Object[] array,String join){
		if(isEmpty(join)){
			join = ",";
		}
		StringBuffer rtn = new StringBuffer();
		if(array!=null){
			int j=0;
			for(int i=0;i<array.length;i++){
				Object obj = array[i];
				if(!obj.getClass().isArray()){
					String str = obj2Str(obj);
					if(!isEmpty(str)){
						if(j>0)rtn.append(join);
						rtn.append("\"").append(str).append("\"");
						j++;
					}
				}
			}
		}
		return rtn.toString();
	}
	/**字符串按字节长度截取**/
	public static String subStrWithBytes(String str ,int len){
		try {
			if(str.getBytes("UTF-8").length <= len){
				return str;
			}
			String tmp = str;
			for(int i = 0;i<str.length();i++){
				if(tmp.getBytes("UTF-8").length <= len){
					break;
				}
				tmp = tmp.substring(0,tmp.length() - 1);
			}
			return tmp;
		} catch (Exception e) {
			return str;
		}
	}
	
	/**
	 * OBJ对象转换成str对象,去除字符串前后空格
	 * @param obj
	 * @return
	 */
	public static String obj2TrimStr(Object obj) {
		return trim(obj2Str(obj));
	}
	/**
	 * OBJ对象转换成Boolean对象
	 * @param obj
	 * @return
	 */
	public static boolean obj2Boolean(Object obj) {
		String str = obj2TrimStr(obj);
		return str2Boolean(str);
	}
	/**
	 * str转换成Boolean对象
	 * @param str
	 * @return
	 */
	public static boolean str2Boolean(String str){
		try{
			return isEmpty(str) ? false : Boolean.valueOf(str);
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * str转换成Int
	 * @param str
	 * @return
	 */
	public static int str2Int(String str){
		return str2Int(str,0);
	} 
	/**
	 * str转换成Int,出错后返回defaultVal
	 * @param str
	 * @param defaultVal
	 * @return
	 */
	public static int str2Int(String str,int defaultVal){
		try{
			return isEmpty(str) ? defaultVal : Integer.parseInt(str);
		}catch(Exception e){
			return defaultVal;
		}
	}
	/**
	 * str转换成Long
	 * @param str
	 * @return
	 */
	public static long str2Long(String str){
		return str2Long(str,0);
	}
	/**
	 * str转换成Long,出错后返回defaultVal
	 * @param str
	 * @param defaultVal
	 * @return
	 */
	public static long str2Long(String str,long defaultVal){
		try{
			return isEmpty(str) ? defaultVal : Long.parseLong(str);
		}catch(Exception e){
			return defaultVal;
		}
	}
	/**
	 * str转换成Double
	 * @param str
	 * @return
	 */
	public static double str2Double(String str){
		return str2Double(str,0);
	}
	/**
	 * str转换成Double,出错后返回defaultVal
	 * @param str
	 * @param defaultVal
	 * @return
	 */
	public static double str2Double(String str,double defaultVal){
		try{
			return isEmpty(str) ? defaultVal : Double.parseDouble(str);
		}catch(Exception e){
			return defaultVal;
		}
	}
	
	/**
	 * 去除字符串前后空格
	 * @param str
	 * @param defStr 当str为null或是"null"时的默认值
	 * @return
	 */
	public static String trim(String str,String defStr) {
        return str == null || "null".equals(str) ? defStr : str.trim();
    }
	/**
	 * 去除字符串前后空格
	 * @param str
	 * @return 当str为null或是"null"时，返回空字符串
	 */
	public static String trim(String str){
		return trim(str,EMPTYSTR);
	}
	/**
	 * 判断是否为null对象
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
        return str == null;
    }
	/**
	 * 判断是否为空字符串,null也被视为空字符串
	 * @param str
	 * @return
	 */
    public static boolean isEmpty(String str){
        str = trim(str);
        return str.length() < 1;
    }
    /**
     * 返回一个随机字符串
     * @return
     */
    public static String getUUID(){
    	String uuid = UUID.randomUUID().toString();
    	uuid = uuid.replaceAll("-", EMPTYSTR);
    	return uuid;
    }
    /**取得本机IP，默认先按Linux方式获取*/
    public static String getLocalHostIp(){
    	try {
			Enumeration<NetworkInterface> allnetInface = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while(allnetInface.hasMoreElements()){
				NetworkInterface netInface = allnetInface.nextElement();
				Enumeration<InetAddress> addresses = netInface.getInetAddresses();
				while(addresses.hasMoreElements()){
					ip = addresses.nextElement();
					if(ip !=null && ip instanceof Inet4Address){
						break;
					}
				}
				if(ip !=null && ip instanceof Inet4Address){
					break;
				}
			}
			if(ip !=null && ip instanceof Inet4Address){
				return ip.getHostAddress();
			}
			return EMPTYSTR;
		} catch (Exception e) {
			try {//windows下有效
	            return InetAddress.getLocalHost().getHostAddress().toString(); // 获取服务器IP地址
	        } catch (Exception ex) {
	        	return EMPTYSTR;
	        }
		}
    }
}
