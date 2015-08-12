package com.ai.frame.dubbo.action.spring;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.frame.dubbo.action.BaseAction;

public class SpringBaseAction extends BaseAction {
	private ThreadLocal<HttpServletRequest> loRequest   = new ThreadLocal<HttpServletRequest>();
	private ThreadLocal<HttpServletResponse> loResponse = new ThreadLocal<HttpServletResponse>();
	
	@Override
	public HttpServletRequest getRequest() {
		return loRequest.get();
	}

	@Override
	public HttpServletResponse getResponse() {
		return loResponse.get();
	}

	@Override
	public ServletContext getServletContext() {
		return loRequest.get().getSession().getServletContext();
	}

	/**
	 * 通用ajax调用方法入口
	 * @param request
	 * @param response
	 * @return
	 */
	public String excute(HttpServletRequest  request,  HttpServletResponse  response){
		initLocalRequestAndResponse(request,response);
		
		return excute();
	}
	/**用于页面跳转用,@forwordTo 跳转的url,@isDirect 是否是direct跳转*/
	public String forwordTo(HttpServletRequest  request,  HttpServletResponse  response){
		initLocalRequestAndResponse(request,response);
		
		return forwordTo();
	}
	/**从session中获取用户信息,默认为map数据*/
	public String getLoginUserInfo(HttpServletRequest  request,  HttpServletResponse  response){
		initLocalRequestAndResponse(request,response);
		
		return getLoginUserInfo();
	}
	/**用户登录用,保存output.bean中的数据到session中*/
	public String userLogin(HttpServletRequest  request,  HttpServletResponse  response){
		initLocalRequestAndResponse(request,response);
		
		return userLogin();
	}
	/**调用action则service获取数据,适用于调用cms接口*/
	public String excuteService(HttpServletRequest  request,  HttpServletResponse  response){
		initLocalRequestAndResponse(request,response);
		
		return excuteService();
	}
	/**调用core则service后再调用action则service,适用于前台需要进行数据处理*/
	public String excuteCoreAndService(HttpServletRequest  request,  HttpServletResponse  response){
		initLocalRequestAndResponse(request,response);
		
		return excuteCoreAndService();
	}
	private void initLocalRequestAndResponse(HttpServletRequest  request,  HttpServletResponse  response){
		loRequest.remove();
		loResponse.remove();
		loRequest.set(request);
		loResponse.set(response);
	}
}
