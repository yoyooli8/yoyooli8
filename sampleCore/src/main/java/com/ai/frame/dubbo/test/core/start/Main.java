package com.ai.frame.dubbo.test.core.start;

import java.io.File;
import java.lang.reflect.Method;

import com.ai.frame.dubbo.common.exception.AinbException;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.DateUtil;
import com.ai.frame.dubbo.test.core.start.plugins.ClassLoadUtil;
import com.ai.frame.dubbo.test.core.util.BusiPluginsHelper;

public class Main {
	private static volatile boolean running = true;
	private static final String NB_FRAME_CONFIG = "config/system.properties";
	private static final String NB_FRAME_LIB    = "lib";
	private static final String NB_FRAME_PATH = "./";
	private static final Logger log = LoggerFactory.getApplicationLog(Main.class);
	public static void main(String[] args) {
		ClassLoader classloader = init();
		Class<?> ServerClass = null;
		try {
			ServerClass = classloader.loadClass("com.ai.frame.dubbo.test.core.start.StartServer");
		} catch (Exception e) {
			throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), "com.ai.frame.dubbo.test.core.start.StartServer 加载失败.",e);
		}
		Object serverInstance = null;
		try {
			serverInstance = ServerClass.newInstance();
			Class<?>[] parameterTypes = new Class<?>[]{};
			Method startMth = serverInstance.getClass().getMethod("start", parameterTypes);
			Object[] parameters = new Object[]{};
			startMth.invoke(serverInstance, parameters);
		} catch (Exception e) {
			throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), "StartServer 启动失败.",e);
		} 
		final Object server = serverInstance;
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					if(server!=null){
						Class<?>[] parameterTypes = new Class<?>[]{};
						Method stopMth = server.getClass().getMethod("stop", parameterTypes);
						Object[] parameters = new Object[]{};
						stopMth.invoke(server, parameters);
					}
				}catch (Exception e) {
					log.info("stop {} server error:{}", e, "dubbo");
				}
				
				synchronized (Main.class) {
                    running = false;
                    
                    log.info("StartServer", "Dubbo {} stopped!");
                    Main.class.notify();
                }
			}
		});
		
		log.info(DateUtil.getCurrentTime1(),"StartServer", "{} Dubbo {} started!");
		
		synchronized (Main.class) {
            while (running) {
                try {
                    Main.class.wait();
                } catch (Throwable e) {
                }
            }
        }
	}
	private static ClassLoader init(){
		String appHomepath = BusiPluginsHelper.getAppHome();
		File   appHome = BusiPluginsHelper.verifyHome(appHomepath, NB_FRAME_CONFIG);
		
		File applib = new File(appHome,NB_FRAME_LIB);
		
		ClassLoader classloader = ClassLoadUtil.loadClass(applib, NB_FRAME_PATH);
		return classloader;
	}
}
