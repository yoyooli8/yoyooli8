package com.ai.frame.dubbo.action.interceptor;

import java.util.List;

import com.ai.frame.dubbo.action.ThreadLocalCache;
import com.ai.frame.dubbo.action.interceptor.paramProcess.ParamProcessResult;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.common.util.SystemPropUtil;
import com.ai.frame.dubbo.dconfig.control.bean.Action;
import com.ai.frame.dubbo.dconfig.control.bean.Control;
import com.ai.frame.dubbo.dconfig.control.bean.Input;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;
import com.ai.frame.dubbo.dconfig.control.xml.ControlFactory;
import com.ai.frame.dubbo.spi.InputObject;

public class RequestInterceptor {
	protected Logger logger = LoggerFactory.getActionLog(this.getClass());
	public static final String ERROR = "action_error";
	public static final String SUCCS = "action_SUCCS";
	public String intercept(HttpRequestWrapper req){
		ParamProcessResult processResult = createInputObject(req);
		if(processResult!=null&&processResult.getRtnCode() == ParamProcessResult.RTN_ERROR){
			ThreadLocalCache.getInstance().setParamProcessErrorResult(processResult);
			req.getRequest().setAttribute(Constants.ACTION_ERR_KEY.getValue(), processResult);
			
			return ERROR;
		}
		
		return SUCCS;
	}
	
	protected ParamProcessResult createInputObject(HttpRequestWrapper req){
		ParamProcessResult processResult = null;
		Control control = ControlFactory.getControl(null);
		int i = 0;
        while (control == null && i < 5) {
            control = ControlFactory.getControl(null);
            i++;
        }
        
        if(control == null){
            logger.error("createInputObject", "{} init application's full control error .");
            processResult = new ParamProcessResult(ParamProcessResult.RTN_ERROR,"init application's full control error .");
            return processResult;
        }
        
        String path = getRequestPath(req);
        Action action = control.getAction(path);
        
        String uid = req.getParameter(Constants.UID.getValue());
        if (uid == null || uid.trim().equals(""))
            uid = null;
        
        InputObject inputObject = new InputObject();
        if (action != null && !StringUtil.isEmpty(uid)){
        	Input input = action.getInput(uid);
        	if (input != null) {
        		processResult = processRequestParams(req,input);
        		if(processResult.getRtnCode() == ParamProcessResult.RTN_ERROR)return processResult;
        		
        		inputObject = processResult.getInputObject();
        		if (input.getService() != null && !"".equals(input.getService().trim())) {
                    inputObject.setService(input.getService());
                }else{
                    inputObject.setService(action.getService());
                }
                
                if (input.getMethod() != null && !"".equals(input.getMethod().trim())) {
                    inputObject.setMethod(input.getMethod());
                }else{
                    inputObject.setMethod(action.getMethod());
                }
                
                ThreadLocalCache.getInstance().setInput(input);
        	}
        	ThreadLocalCache.getInstance().setOutput(action.getOutput(uid));
        }
        
        ThreadLocalCache.getInstance().setInputObject(inputObject);
        if(processResult == null){
        	processResult = new ParamProcessResult(ParamProcessResult.RTN_SUCESS,"action path["+path+"] cant't find or uid["+uid+"] can't find.");
        }
        return processResult;
	}
	
	private ParamProcessResult processRequestParams(HttpRequestWrapper request,Input input){
		InputObject inputObject = new InputObject();
		String ip = StringUtil.getLocalHostIp();
		inputObject.setServerIp(ip);
		
		logger.info("processRequestParams", ip, "{} called.get LocalHost IP is {}");
//		try {
//            String ip = InetAddress.getLocalHost().getHostAddress().toString(); // 获取服务器IP地址
//            inputObject.setServerIp(ip);
//        } catch (Exception e) {
//            logger.error("processRequestParams", "{} Exception Occured When Try To Get IP Address ! error:{}", e);
//        }
		
		InputObjectFactory factory = new InputObjectFactory(inputObject,this);
        List<Parameter> parameters = factory.getParameters(request, input);
        //System.out.println("---------------parameters--------------"+parameters.size());
        
        for(int i=0;i<parameters.size();i++){
        	Parameter param = parameters.get(i);
        	//System.out.println("---------------parameter.param_"+i+"--------------"+param);
            if(param == null) continue;
        	String key   = param.getKey();
            if(StringUtil.isEmpty(key)) continue;
            if(StringUtil.isEmpty(param.getScope())){
                param.setScope(Constants.PARAM_SCOPE_REQ.getValue());
            }
            
            ParamProcessResult processResult = factory.excute(param,request);
            if(ParamProcessResult.RTN_ERROR == processResult.getRtnCode()){
            	return processResult;
            }

        }
        
        ParamProcessResult processResult = new ParamProcessResult(ParamProcessResult.RTN_SUCESS,"",inputObject);
        return processResult;
	}
	
	/**从属性文件中取得值*/
    public String getValueFromProperties(String propKey){
    	return SystemPropUtil.getString(propKey);
    }
    
    /**从常量类中取得值*/
    public String getValueFromConstants(String consKey){
        return "";
    }
    
	private String getRequestPath(HttpRequestWrapper req){
		String path = req.getRequestURI();
        String contextpath = req.getContextPath();

        int index = path.lastIndexOf(".");
        String suffix = path.substring(index + 1);
        suffix = "." + suffix;
        if (path.endsWith(suffix)) {
            path = path.substring(0, path.indexOf(suffix));
        }
        if(path.startsWith(contextpath)){
            path = path.substring(contextpath.length(),path.length());
        }

        return path;
	}
}
