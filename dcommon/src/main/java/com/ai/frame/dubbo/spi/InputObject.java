package com.ai.frame.dubbo.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ai.frame.dubbo.common.util.StringUtil;

/**
 * 统一的参数对象
 * @since 20150701
 */
public class InputObject implements Serializable {
	private static final long serialVersionUID = -1678980662008969902L;
	private String service;//调用后台服务类的名称
	private String method;//服务类中的方法名
	private String serverIp;//调用服务的主机IP
	private String userId;//用户信息
	private Map<String, String> params = new HashMap<String, String>();//参数信息
	private List<Map<String, String>> beans = new ArrayList<Map<String, String>>();//参数集
	private boolean isMultiParams = false; // 是否混合型参数，如果为true则 files应该不为空
	
	private Map<String, FileParam> file = new HashMap<String, FileParam>();//文件信息
	private List<Map<String,FileParam>> filels = new ArrayList<Map<String,FileParam>>();//文件列表

//	private Logger log = LoggerFactory.getCustomerLog(getClass(), "Input.params");
	
	/** 无参构造器 **/
	public InputObject() {
	}

	/** 构造器 **/
	public InputObject(String service, String method) {
		this.service = service;
		this.method = method;
	}

	/** 构造器 **/
	public InputObject(String service, String method, Map<String, String> params) {
		this.service = service;
		this.method = method;
		this.params = params;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<Map<String, String>> getBeans() {
		return beans;
	}

	public void setBeans(List<Map<String, String>> beans) {
		this.beans = beans;
	}

	public Map<String, FileParam> getFile() {
		return file;
	}

	public void setFile(Map<String, FileParam> file) {
		this.file = file;
	}

	public List<Map<String, FileParam>> getFilels() {
		return filels;
	}
	public int getFileSize(){
		return filels.size();
	}
	public FileParam getFilels(int index) {
		if(filels.size() <= 0)return null;
		
		if(index >= filels.size()){
			index = filels.size() -1;
		}else if(index < 0){
			index = 0;
		}
		
		Map<String, FileParam> filemp = filels.get(index);
		Iterator<Map.Entry<String, FileParam>> it = filemp.entrySet().iterator();
		Map.Entry<String, FileParam> entry = it.next();
		
		return entry.getValue();
	}

	public void setFilels(List<Map<String, FileParam>> filels) {
		this.filels = filels;
	}

	/**
	 * 往bean属性中放入键值对
	 */
	public void addParams(String key, String toKey, String value) {
		if (toKey != null && !"".equals(toKey)) {
			params.put(toKey, value);
		} else {
			params.put(key, value);
		}
	}
	/**添加多值参数*/
    public void addParams(String paramKey, String toKey, String[] paramValues) {
    	addMapValues(beans,paramKey,toKey,paramValues);
    }

    /**
	 * 往beans中的index处的Map中放入键值对
	 */
	public void addBeans(String key, String toKey, int index,String value) {
		if (beans.size() <= index) {
			beans.add(new HashMap<String, String>());
		}
		index = beans.size()-1;
		
		if (toKey != null && !"".equals(toKey)) {
			beans.get(index).put(toKey, value);
		} else {
			beans.get(index).put(key, value);
		}
	}

	/**添加多值File参数*/
    public void addFiles(String paramKey, String toKey, FileParam[] fins) {
    	if(fins !=null){
    		for(int i=0;i<fins.length;i++){
    			addFiles(paramKey,toKey,i,fins[i]);
    		}
    	}
    }
    
    
//    /**添加多值File参数*/
//    public void addFiles(String paramKey, String toKey, File[] files) {
//    	if(files ==null)return;
//    	InputStream fin = null;
//    	int i = 0;
//    	try {
//			InputStream[] fins = new InputStream[files.length];
//			for(i=0;i<files.length;i++){
//				fin = new FileInputStream(files[i]);
//				fins[i] = fin;
//			}
//			
//			addFiles(paramKey,toKey,fins);
//		} catch (Exception e) {
////			log.error("add the inedx[{}] file error:{}", e, String.valueOf(i+1));
//			IOUtil.closeInputStream(fin);
//		}
//    }
	/**
	 * 往file属性中放入键值对
	 */
	public void addFile(String key, String toKey, FileParam fin) {
		if (toKey != null && !"".equals(toKey) && fin !=null) {
			file.put(toKey, fin);
		} else if(fin !=null){
			file.put(key, fin);
		}
	}
	/**
	 * 往files中的index处的Map中放入键值对
	 */
	public void addFiles(String key, String toKey, int index,FileParam fin) {
		if (filels.size() <= index) {
			filels.add(new HashMap<String, FileParam>());
		}
		index = filels.size()-1;
		
		if (toKey != null && !"".equals(toKey) && fin !=null) {
			filels.get(index).put(toKey, fin);
		} else if(fin !=null){
			filels.get(index).put(key, fin);
		}else{
			filels.remove(index);
		}
	}
	
	@SuppressWarnings({ "rawtypes"})
	private void addMapValues(List beans,String paramKey,String toKey,Object[] values){
		if(values ==null)return;
		String mapKey = paramKey;
        if (!StringUtil.isEmpty(toKey)) {
            mapKey = toKey;
        }
        
        if (beans.size() == 0) {
        	for (int i = 0; i < values.length; i++) {
            	Object obj = values[i];
            	addObj2List(mapKey,obj,beans);
            }
        }else{
        	//删除已经存在的值
        	for (int i = 0; i < beans.size(); i++) {
                Map map = (Map)beans.get(i);
                if(map.containsKey(mapKey)){
                	beans.remove(i);
                }
            }
        	
        	//添加新值进去
        	for (int i = 0; i < values.length; i++) {
        		Object obj = values[i];
        		
        		addObj2List(mapKey,obj,beans);
            }
        }
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addObj2List(String mapKey,Object obj,List beans){
		if(obj!=null){
			if(obj instanceof String){
				Map map = new HashMap();
    			map.put(mapKey, obj);
    			beans.add(map);
			}
		}else if(obj instanceof String[]){
			String[] tmp = (String[]) obj;
			for(String str:tmp){
				Map map = new HashMap();
    			map.put(mapKey, str);
    			beans.add(map);
			}
		}
	}
	public boolean isMultiParams() {
        return isMultiParams;
    }

    public void setMultiParams(boolean isMultiParams) {
        this.isMultiParams = isMultiParams;
    }

}
