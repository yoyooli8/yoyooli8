package com.ai.frame.dubbo.common.tools.ftp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;

public class FtpClientFactory {
	private Logger log = LoggerFactory.getUtilLog(FtpClientFactory.class);
	private static final String LOGIN_METHOD  = "login";
	private static final String LOGOUT_METHOD = "logout";
	private FtpClientProxy clientProxy;
	private static FtpClientFactory instance = null;
	private FtpClientFactory(FtpClientAdapter ftpClient){
		clientProxy = new FtpClientProxy(ftpClient);
	}
	public static synchronized FtpClientFactory getInstance(FtpClientAdapter ftpClient){
		if(instance == null){
			instance = new FtpClientFactory(ftpClient);
		}
		return instance;
	}
	public FtpClientI getFtpClient(){
		return (FtpClientI)Proxy.newProxyInstance(clientProxy.getClass().getClassLoader(),FtpClientAdapter.class.getInterfaces(),clientProxy);
	}
	public class FtpClientProxy implements InvocationHandler{
		private FtpClientAdapter ftpClient;
		public FtpClientProxy(FtpClientAdapter ftpClient){
			this.ftpClient = ftpClient;
		}
		public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {
			String methodName = method.getName();
			log.debug(methodName, "{} invoke started.");
			if(methodName!=null && !(LOGIN_METHOD.equals(methodName)||LOGOUT_METHOD.equals(methodName))){
				ftpClient.getFtpConnection();
			}
			Object result = method.invoke(ftpClient, args);
			
			log.debug(methodName, result!=null?result.toString():"no return", "{} invoke ended,the result is :{}");
			return result;
		}
		
	}
}
