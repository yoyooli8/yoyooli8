package com.ai.frame.dubbo.action.spring;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ai.frame.dubbo.action.interceptor.RequestInterceptor;
import com.ai.frame.dubbo.action.interceptor.paramProcess.ParamProcessResult;
import com.ai.frame.dubbo.common.helper.ContextHelper;

public class SpringReqInterceptor extends RequestInterceptor implements AsyncHandlerInterceptor {
	private ContextHelper   contextHelper;
	public void setContextHelper(ContextHelper contextHelper) {
		this.contextHelper = contextHelper;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		SpringHttpRequestWrapper requestWrapper = new SpringHttpRequestWrapper(request);
		
		ParamProcessResult processResult = createInputObject(requestWrapper);
		if(processResult!=null&&processResult.getRtnCode() == ParamProcessResult.RTN_ERROR){
			Map<String,String> errorRtn = new HashMap<String,String>();
			errorRtn.put("rtnCode", String.valueOf(processResult.getRtnCode()));
			errorRtn.put("rtnMsg", processResult.getRtnMsg());
			
			response.getWriter().println(contextHelper.getJsonHelper().convertObject2Json(errorRtn));
			
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//TODO clear upload File
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	}

}
