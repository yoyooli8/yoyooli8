package com.ai.frame.dubbo.action.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.frame.dubbo.common.helper.JsonHelper;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;
import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

public class CommonConvert {
	public static final String DEFAULT_METHOD = "convertSingleData";
	public static final String JSONCONVERTKEY = "jsonConvert";
	
	protected static final String NOTRTNKEY = "unknow key";
	protected static final String RTNCODEKEY = "returnCode";
	protected static final String RTNMSGKEY  = "returnMessage";
    protected static final String OMGGRID_TOTALKEY = "total";
    protected static final String OMGGRID_ROWSKEY  = "rows";
    protected static String BEANSKEY = "beans";
    protected static String BEANKEY  = "bean";
	
    protected Logger logger = LoggerFactory.getUtilLog(CommonConvert.class);
    protected JsonHelper jsonHelper;
    public CommonConvert(){}
    public CommonConvert(JsonHelper jsonHelper){
    	this.jsonHelper = jsonHelper;
    }
    public void setJsonHelper(JsonHelper jsonHelper) {
		this.jsonHelper = jsonHelper;
	}
    /**错误json的统一输出*/
    public String rtnError(List<Parameter> parameters){
    	StringBuffer rtn = new StringBuffer();
    	rtn.append("{\"").append(getToKey(RTNCODEKEY,parameters));
    	rtn.append("\":").append(Constants.RTNCODE_ERR).append("}");
    	return rtn.toString();
    }
    
    /**输出Map形式的json加上rtnCode和rtnMsg*/
	public String convert(InputObject input,OutputObject output,List<Parameter> parameters){
    	try{
	    	Map<String,Object> outMap = new HashMap<String,Object>();
    		addRtnInfo(outMap,output,parameters);
    		addMpInfo(outMap,output,parameters);

    		return jsonHelper.convertObject2Json(outMap);
    	}catch(Exception e){
    		logger.error("{}convert output to json error:{}", e, "");
    		
    		return rtnError(parameters);
    	}
    }
	
	/**输出map形式json并带上beans内嵌数组数据*/
    public String convert2List(InputObject input,OutputObject output,List<Parameter> parameters){
    	Map<String,Object> gridMp = new HashMap<String,Object>();
    	try{
	    	addRtnInfo(gridMp,output,parameters);
	    	addBeansInfo(gridMp,output,parameters);
	    	return jsonHelper.convertObject2Json(gridMp);
    	}catch(Exception e){
    		return rtnError(parameters);
    	}
    }
    
    /**输出map形式json并带上beans内嵌数组数据，并附加total属性，此值从returnMessge中获取*/
    public String convert2omGrid(InputObject input,OutputObject output,List<Parameter> parameters){
    	int totalSize = StringUtil.str2Int(output.getReturnCode());
    	Map<String,Object> gridMp = new HashMap<String,Object>();
    	try{
    		if (parameters != null && parameters.size() > 0) {
        		gridMp.put(getToKey(RTNCODEKEY,parameters), Constants.RTNCODE_SUC.getValue());
        		gridMp.put(getToKey(RTNMSGKEY,parameters),  output.getReturnMessage());
        	}else{
        		gridMp.put(RTNCODEKEY, output.getReturnCode());
        		gridMp.put(RTNMSGKEY,  output.getReturnMessage());
        	}
    		gridMp.put(OMGGRID_TOTALKEY, totalSize);
    		
	    	addBeansInfo(gridMp,output,parameters);
	    	
	    	return jsonHelper.convertObject2Json(gridMp);
    	}catch(Exception e){
    		return rtnError(parameters);
    	}
    }
    
    /**输出map形式json并带上bean内嵌map数据*/
    public String convertSingleData(InputObject input,OutputObject output,List<Parameter> parameters){
    	Map<String,Object> gridMp = new HashMap<String,Object>();
    	try{
	    	addRtnInfo(gridMp,output,parameters);
	    	addBeanInfo(gridMp,output,parameters);
	    	
	    	return jsonHelper.convertObject2Json(gridMp);
    	}catch(Exception e){
    		return rtnError(parameters);
    	}
    }
    
    /**输出map形式json并带上bean内嵌map数据和beans内嵌数组数据*/
    public String convertAllData(InputObject input,OutputObject output,List<Parameter> parameters){
    	Map<String,Object> gridMp = new HashMap<String,Object>();
    	try{
	    	addRtnInfo(gridMp,output,parameters);
	    	addBeanInfo(gridMp,output,parameters);
	    	addBeansInfo(gridMp,output,parameters);
	    	
	    	return jsonHelper.convertObject2Json(gridMp);
    	}catch(Exception e){
    		return rtnError(parameters);
    	}
    }
    //convert returnCode和returnMessage字段
    protected void addRtnInfo(Map<String,Object> gridMp,OutputObject output,List<Parameter> parameters){
		if (parameters != null && parameters.size() > 0) {
    		gridMp.put(getToKey(RTNCODEKEY,parameters), output.getReturnCode());
    		gridMp.put(getToKey(RTNMSGKEY,parameters),  output.getReturnMessage());
    	}else{
    		gridMp.put(RTNCODEKEY, output.getReturnCode());
    		gridMp.put(RTNMSGKEY,  output.getReturnMessage());
    	}
	}
    protected void addBeansInfo(Map<String,Object> gridMp,OutputObject output,List<Parameter> parameters){
		//beans convert
		List<Map<String,String>> datas = output.getBeans();
    	List<Map<String,String>> rows  = convertListMap(datas,parameters);
    	
    	gridMp.put(getToKey(BEANSKEY,parameters), rows);
	}
	protected void addBeanInfo(Map<String,Object> gridMp,OutputObject output,List<Parameter> parameters){
		//bean convert
		Map<String,String> rtnMap = convertMap(output.getBean(),parameters);
		gridMp.put(getToKey(BEANKEY,parameters), rtnMap);
	}
	protected void addMpInfo(Map<String,Object> gridMp,OutputObject output,List<Parameter> parameters){
		//bean convert
		Map<String,String> rtnMap = convertMap(output.getBean(),parameters);
		gridMp.putAll(rtnMap);
	}
	
    protected List<Map<String,String>> convertListMap(List<Map<String,String>> datas,List<Parameter> parameters){
    	List<Map<String,String>> rows  = new ArrayList<Map<String,String>>();
    	for(Map<String,String> data:datas){
    		Map<String,String> rtnMap = convertMap(data,parameters);
    		
    		rows.add(rtnMap);
    	}
    	return rows;
    }
    protected Map<String,String> convertMap(Map<String,String> data,List<Parameter> parameters){
    	Map<String,String> rtnMap = new HashMap<String,String>();
		if (parameters != null && parameters.size() > 0) {
			for(Parameter param:parameters){
				String key = param.getKey();
                String toKey = param.getToKey();
                String value = getSingleDataVal(data,key);
                if(!NOTRTNKEY.equals(value)){
                    rtnMap.put(toKey, value);
                }
			}
		}else{
			rtnMap.putAll(data);
		}
		return rtnMap;
    }
    protected String getSingleDataVal(Map<String,String> values,String key){
    	if(values.containsKey(key)){
            return values.get(key);
        }else{
            return NOTRTNKEY;
        }
    }
    protected String getToKey(String key,List<Parameter> parameters){
    	try {
			String rtnKey = key;
			for(Parameter parameter:parameters){
				if(key.equals(parameter.getKey())){
					return parameter.getToKey();
				}
			}
			return rtnKey;
		} catch (Exception e) {
		}
		return key;
    }
}
