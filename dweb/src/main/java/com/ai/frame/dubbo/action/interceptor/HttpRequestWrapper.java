package com.ai.frame.dubbo.action.interceptor;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public abstract class HttpRequestWrapper {
	protected HttpServletRequest request;
	public HttpRequestWrapper(HttpServletRequest request){
		this.request = request;
	}
	/**
	 * 所有请求参数包含文件上传属性
	 * 文件上传的特有属性 1.${fieldName}FileName    文件上传属性名称
	 *             2.${fieldName}ContentType 文件上传的类型
	 *             这两个属性在请求处理中文件上传请求处理中有处理文件类型，
	 *             请参考:com.ai.frame.frameWeb.interceptor.param.RequestInputParameters
	 *                  com.ai.frame.frameWeb.interceptor.paramProcess.FileParamProcess
	 * @return
	 */
	public abstract Map<String, Object> getParameters();
	/**判断是否是文件上传**/
	public abstract boolean isMultiPartRequest();
	
	@SuppressWarnings("unchecked")
	public Enumeration<String> getParameterNames(){
		return request.getParameterNames();
	}
	public String getRequestURI(){
		return request.getRequestURI();
	}
	public String getContextPath(){
		return request.getContextPath();
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public String getParameter(String paramKey){
		String reqval = request.getParameter(paramKey);
		return reqval;
	}
	public String[] getParameterValues(String paramKey){
		String[] vas = request.getParameterValues(paramKey);
		
		return vas;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
}
