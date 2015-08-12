package com.ai.frame.dubbo.action.interceptor.paramProcess;

import javax.servlet.http.HttpServletRequest;

import com.ai.frame.dubbo.action.interceptor.HttpRequestWrapper;
import com.ai.frame.dubbo.action.interceptor.RequestInterceptor;
import com.ai.frame.dubbo.common.util.ClassUtil;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.dconfig.control.bean.Parameter;
import com.ai.frame.dubbo.spi.InputObject;

public class SessionParamProcess extends AbstractParamProcess{

	public SessionParamProcess(InputObject inputObject,
			HttpRequestWrapper request, RequestInterceptor reqInterceptor) {
		super(inputObject, request, reqInterceptor);
	}
	public ParamProcessResult excute(Parameter param){
		HttpServletRequest req = request.getRequest();
		String keyVal = getValueFromSession(req,param.getKey());
		if(keyVal!=null){
            inputObject.addParams(param.getKey(), param.getToKey(),keyVal);
        }
		
		return getSucResult();
	}
	
	/**从session中取值*/
    protected String getValueFromSession(HttpServletRequest request,String key){
        String[] keys = key.split("[.]");
        if (keys != null && keys.length == 2) {
            Object sessionVal = request.getSession().getAttribute(keys[0]);
            if (sessionVal != null) {
                Object keyVal = ClassUtil.invokFieldGetMethodVal(sessionVal, keys[1]);
                return StringUtil.obj2Str(keyVal);
            }
        }else if(keys != null && keys.length == 1){
            Object sessionVal = request.getSession().getAttribute(keys[0]);
            return StringUtil.obj2Str(sessionVal);
        }
        return null;
    }
}
