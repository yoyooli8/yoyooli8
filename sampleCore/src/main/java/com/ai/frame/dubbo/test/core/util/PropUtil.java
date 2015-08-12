package com.ai.frame.dubbo.test.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.IOUtil;

/**属性文件读写操作*/
public class PropUtil {
	private static final Logger log = LoggerFactory.getUtilLog(PropUtil.class);
	private static Map<String,Properties> properties = new HashMap<String,Properties>();
	private static PropUtil instance = new PropUtil();
	private PropUtil(){}
	public static PropUtil getInstance(){
		return instance;
	}
	public String getValue(String propfile,String propname){
		Properties prop = properties.get(propfile);
		if(prop == null){
			InputStream in = null;
			try {
				prop = new Properties();
				in = new FileInputStream(propfile);
				prop.load(in);
				properties.put(propfile, prop);
				
				return prop.getProperty(propname);
			} catch (IOException e) {
				log.error("[{}] file not found.:{}.", e, propfile);
				return null;
			}
		}else{
			try {
				return prop.getProperty(propname);
			} catch (Exception e) {
				log.error("[{}] file can't config the [{}] property value:{}.", e, propfile,propname);
				return null;
			}
		}
	}
	public boolean setValue(String propfile,String propname,String propvalue){
		Properties prop = properties.get(propfile);
		if(prop == null){
			InputStream in   = null;
			try {
				prop = new Properties();
				in = new FileInputStream(propfile);
				prop.load(in);
				
				prop.setProperty(propname, propvalue);
				properties.put(propfile, prop);
				
			} catch (Exception e) {
				log.error("[{}] file not found.:{}.", e, propfile);
				return false;
			}finally{
				IOUtil.closeInputStream(in);
			}
			
			return write2prop(prop,propfile,propname);
		}else{
			try {
				prop.setProperty(propname, propvalue);
				
				return write2prop(prop,propname,propvalue);
			} catch (Exception e) {
				log.error("add the prop[{}={}] value to propfile error:{}", e, propname,propvalue);
				return false;
			}
		}
	}
	
	private boolean write2prop(Properties prop,String propfile,String propname){
		if(prop !=null){
			OutputStream out = null;
			try {
				out = new FileOutputStream(propfile);
				prop.store(out, "add prop["+propname+"] value.");
				
				return true;
			} catch (Exception e) {
				log.error("add prop[{}] to propfile[{}] error:", e, propname,propfile);
				return false;
			}finally{
				IOUtil.closeOutputStream(out);
			}
		}
		return false;
	}
}
