package com.ai.frame.dubbo.action.struts2;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.ai.frame.dubbo.action.ThreadLocalCache;
import com.ai.frame.dubbo.action.interceptor.RequestInterceptor;
import com.ai.frame.dubbo.action.interceptor.paramProcess.ParamProcessResult;
import com.ai.frame.dubbo.common.helper.ContextHelper;
import com.ai.frame.dubbo.common.helper.SpringUtil;
import com.ai.frame.dubbo.common.util.Constants;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

@SuppressWarnings("serial")
public class Struts2RequestInterceptor extends RequestInterceptor implements Interceptor {
	 
	@Override
	public void destroy() {
		logger.info("destroy", "RequestInterceptor.{} called.");
	}

	@Override
	public void init() {
		logger.info("init", "RequestInterceptor.{} called.");
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		Struts2HttpRequestWrapper requestWrapper = new Struts2HttpRequestWrapper(request);
		String requestProccess = intercept(requestWrapper);
		
		if(RequestInterceptor.ERROR.equals(requestProccess)){
			//处理验证失败
			ParamProcessResult processResult = ThreadLocalCache.getInstance().getParamProcessErrorResult();
			if(requestWrapper.isMultiPartRequest()){
				PrintWriter out = null;
				try{
					out = ServletActionContext.getResponse().getWriter();
					out.println("<html><head></head>");
		            out.println("<Script LANGUAGE='javascript'> function upcallback(rtncode,msg){alert(msg);} upcallback(0,'"+processResult.getRtnMsg()+"');</script>");
		            out.println("</head>");
		            out.println("<body>");
		            out.println("</body></html>");
				}catch(Exception ex){
					logger.error("request validate", "{} error:{}",ex);
				}finally{
					if (out != null) out.close();
				}
			}else{
				ContextHelper contextHelper = SpringUtil.getBean(Constants.SPRINGUTIL_ID.getValue(),ContextHelper.class);
				ServletActionContext.getResponse().getWriter().print(contextHelper.getJsonHelper().convertObject2Json(processResult));
			}
			return null;
		}
		
		return invocation.invoke();
	}

}
