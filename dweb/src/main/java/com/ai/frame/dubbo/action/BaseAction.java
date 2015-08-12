package com.ai.frame.dubbo.action;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ai.frame.dubbo.action.convert.CommonConvert;
import com.ai.frame.dubbo.action.service.WebActionService;
import com.ai.frame.dubbo.common.helper.ContextHelper;
import com.ai.frame.dubbo.common.helper.JsonHelper;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.ClassUtil;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.dconfig.control.bean.Input;
import com.ai.frame.dubbo.dconfig.control.bean.Output;
import com.ai.frame.dubbo.spi.IControlService;
import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

public abstract class BaseAction {
	protected Logger logger = LoggerFactory.getActionLog(this.getClass());
	private IControlService  coreService;
	private ContextHelper    contextHelper;
	private WebActionService actionService;
	private String webActionRef;
	public void setWebActionRef(String webActionRef) {
		logger.info(webActionRef, "------------------config the web action service bean ref:{}-------------");
		this.webActionRef = webActionRef;
		if(!StringUtil.isEmpty(webActionRef)){
			try{
				actionService = contextHelper.getBean(this.webActionRef, WebActionService.class);
			}catch(Exception e){
				logger.error("the web actionService[{}] config error:{}",e,this.webActionRef);
			}
		}
	}
	public void setActionService(WebActionService actionService) {
		this.actionService = actionService;
	}
	public IControlService getCoreService() {
		return coreService;
	}
	public void setCoreService(IControlService coreService) {
		this.coreService = coreService;
	}
	public ContextHelper getContextHelper() {
		return contextHelper;
	}
	public void setContextHelper(ContextHelper contextHelper) {
		this.contextHelper = contextHelper;
	}

	/***http servlet 相关属性,需要在具体实现中实现这些方法**/
	public abstract HttpServletRequest getRequest();
	public abstract HttpServletResponse getResponse();
	public abstract ServletContext getServletContext();
	public HttpSession getSession(){
		return getSession(false);
	}
	public HttpSession getSession(boolean isNew){
		return getRequest().getSession(isNew);
	}
	public void sendJson(String respJson,boolean isGzip){
		if(isGzip){
			OutputStream writer = null;
			try{
				byte[] rtndata  = IOUtil.compressByteWithGzip(respJson.getBytes("UTF-8"));
				
				getResponse().setHeader("Content-Encoding", "gzip");
				getResponse().setContentLength(rtndata.length);
				
				writer = getResponse().getOutputStream();
				writer.write(rtndata);
				
				writer.flush();
			}catch(Exception e){
				logger.error("excute", respJson, "{} called response is:{} error:{}", e);
			}finally{
				if(writer!=null)IOUtil.closeOutputStream(writer);
				logger.debug(respJson,"send Json finally:{}");
			}
		}else{
			sendJson(respJson);
		}
	}
	public void sendJson(String json){
		PrintWriter writer = null;
		try{
			logger.debug(json,"send Json Start:{}");
			writer = getResponse().getWriter();
			writer.print(json);
			logger.debug(json,"send Json End:{}");
		}catch(Exception e){
			logger.error("sendJson", "{} Error:{}", e);
		}finally{
			if(writer!=null)writer.close();
			logger.debug(json,"send Json finally:{}");
		}
	}
	public void formCallback(String rtnCode,String rtnMsg){
		PrintWriter outp = null;
		try{
			outp = getResponse().getWriter();
			outp.println("<html><head></head>");
			outp.println("<Script LANGUAGE='javascript'> function formCallback(rtncode,msg){alert(msg);} formCallback("+rtnCode+",'"+rtnMsg+"')</script>");
			outp.println("</head>");
			outp.println("<body>");
			outp.println("</body></html>");
		}catch(Exception ex){
			if(outp!=null)outp.close();
		}
	}
	
	public InputObject getInputObject(){
		return ThreadLocalCache.getInstance().getInputObject();
	}
	public Output getOutput(){
		return ThreadLocalCache.getInstance().getOutput();
	}
	
	public OutputObject getOutputObject(){
		return getOutputObject(getInputObject());
	}
	/**从session中获取用户信息,默认为map数据*/
	@SuppressWarnings("unchecked")
	public String getLoginUserInfo(){
		HttpServletRequest  request = getRequest();
		
		HttpSession session = request.getSession();
		logger.info(session.getId(), "--------user login's session id is:{}-----------");
		OutputObject out = new OutputObject();
		out.setReturnCode(Constants.RTNCODE_ERR.getValue());
		out.setReturnMessage("你还没有登录");
		if (session != null) {
			Object userObj = session.getAttribute(Constants.LOGIN_USER_SESSIONID.getValue());
			if(userObj!=null && userObj instanceof Map){
				Map<String,String> userInfo = (Map<String,String>)userObj;
				out.setReturnCode(Constants.RTNCODE_SUC.getValue());
				out.setBean(userInfo);
				out.setReturnMessage("用户登录信息");
			}
		}
		
		sendJson(convertOutputObject2Json(out),true);
		return null;
	}
	/**用户登录用,保存output.bean中的数据到session中*/
	public String userLogin(){
		HttpServletRequest  request = getRequest();
		
		InputObject inputObject = getInputObject();
		OutputObject object = getOutputObject(inputObject);
		OutputObject out = new OutputObject();
		if(Constants.RTNCODE_SUC.getValue().equals(object.getReturnCode()) && object.getBean().size()>0){
			request.getSession(true).setAttribute(Constants.LOGIN_USER_SESSIONID.getValue(), object.getBean());
			out.setReturnCode(Constants.RTNCODE_SUC.getValue());
			out.setReturnMessage(object.getReturnMessage());
		}else{
			out.setReturnCode(Constants.RTNCODE_ERR.getValue());
			out.setReturnMessage(object.getReturnMessage());
		}
		sendJson(convertOutputObject2Json(out),true);
		
		return null;
	}
	/**用于页面跳转用,@forwordTo 跳转的url,@isDirect 是否是direct跳转*/
	public String forwordTo(){
		String forwordTo = getRequest().getParameter(Constants.PARAM_FORWORD_NAME.getValue());
		String isDirect  = getRequest().getParameter(Constants.PARAM_DIRECT_NAME.getValue());
		boolean direct  = StringUtil.str2Boolean(isDirect);
		
		try {
			if(direct){
				getResponse().sendRedirect(forwordTo);
			}else{
				getRequest().getRequestDispatcher(forwordTo).forward(getRequest(), getResponse());
			}
		} catch (Exception e) {
			logger.error("----forwordTo-->[{}]-----error:{}", e, StringUtil.trim(forwordTo,"unKnown url"));
		}
		
		return null;
	}
	/**默认action的执行方法*/
	public String excute(){
		InputObject inputObject = getInputObject();
		OutputObject object = getOutputObject(inputObject);
		sendJson(convertOutputObject2Json(object),true);
		
		return null;
	}
	/**
	 * 调用action则service获取数据,适用于调用cms接口
	 * uid即为actionService中的方法名称: OutputObject XXXX(InputObject inobj)
	 * */
	public String excuteService(){
		InputObject inobj = this.getInputObject();
		String uid = inobj.getParams().get(Constants.UID);
		Class<?>[] paramcls = new Class<?>[]{InputObject.class};
		
		OutputObject outputObject = ClassUtil.invokMethod(OutputObject.class, actionService, uid, paramcls, inobj);
		String rtnjson = convertOutputObject2Json(outputObject);
		sendJson(rtnjson,true);
		
		return null;
	}
	/**
	 * 调用core则service后再调用action则service,适用于前台需要进行数据处理
	 * uid即为actionService中的方法名称: OutputObject XXXX(InputObject inobj,OutputObject outobj)
	 * */
	public String excuteCoreAndService(){
		InputObject  inobj   = this.getInputObject();
		OutputObject outobj  = getOutputObject(inobj);
		
		String uid = inobj.getParams().get(Constants.UID);
		Class<?>[] paramcls = new Class<?>[]{InputObject.class,OutputObject.class};
		
		OutputObject outputObject = ClassUtil.invokMethod(OutputObject.class, actionService, uid, paramcls, inobj,outobj);
		String rtnjson = convertOutputObject2Json(outputObject);
		sendJson(rtnjson,true);
		
		return null;
	}
	
	public OutputObject getOutputObject(InputObject inputObject) {
		OutputObject object = null;
		Input input = ThreadLocalCache.getInstance().getInput();
		
		if(input != null && input.isCache()){//判断是否需要缓存,SpringContextHelper需要在springBean配置文件中注册为bean
			object = contextHelper.getCacheHelper().get(input.getUid());
			if(object == null){
				object = this.execute(inputObject);
				contextHelper.getCacheHelper().put(input.getUid(), object);
			}
		}else{
			object = this.execute(inputObject);
		}
		
		return object;
	}
	
	private OutputObject execute(InputObject inputObject){
		OutputObject outputObject = null;
		try{
			outputObject = getCoreService().execute(inputObject);
		}catch(Exception e){
			e.printStackTrace();
		}
		return outputObject;
	}
	
	public String convertOutputObject2Json(OutputObject outputObject){
		return convertOutputObject2Json(getInputObject(), outputObject);
	}
	
	@SuppressWarnings("rawtypes")
	public String convertOutputObject2Json(InputObject inputObject,OutputObject outputObject){
		String json = "";
		if (outputObject == null || inputObject == null)
			return json;
		
		Output output = getOutput();
		
		try {
			logger.debug("convertOutputObject2Json", outputObject.getReturnCode(), "{} start:{}");
			String convertor = null;
			String convtmeth = null;
			if(output == null){
				convertor = CommonConvert.class.getName();
				convtmeth = CommonConvert.DEFAULT_METHOD;
				
			}else{
				convertor = output.getConvertor();
				convtmeth = output.getMethod();
			}
			
			if(StringUtil.isEmpty(convertor)){
				convertor = CommonConvert.class.getName();
			}
			if(StringUtil.isEmpty(convtmeth)){
			    String jsConvert = getRequest().getParameter(CommonConvert.JSONCONVERTKEY);
			    if(StringUtil.isEmpty(jsConvert)){
			        convtmeth = CommonConvert.DEFAULT_METHOD;
			    }else{
			        convtmeth = jsConvert;
			    }
            }
			Object[] paramVal = new Object[]{contextHelper.getJsonHelper()};
			
			Object convert = ClassUtil.getInstance(convertor,paramVal,JsonHelper.class);
			
			Class[]  paramcls = {InputObject.class,OutputObject.class,List.class};
		    Object[] paramvals= {inputObject,outputObject,output == null? null:output.getParameters()};
		    
		    json = ClassUtil.invokMethod(String.class, convert, convtmeth, paramcls, paramvals);
		    
		    logger.debug("{} end:{} - {}", null,"convertOutputObject2Json",convertor , convtmeth);
		}catch(Exception e){
			logger.debug("{} error:{}", e,"convertOutputObject2Json");
		}
		
		return json;
	}
	
}
