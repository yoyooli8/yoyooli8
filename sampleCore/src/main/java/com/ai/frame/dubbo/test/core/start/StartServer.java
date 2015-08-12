package com.ai.frame.dubbo.test.core.start;

import java.io.File;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.frame.dubbo.common.exception.AinbException;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.test.core.util.BusiPluginsHelper;

public class StartServer implements Container{
	private static final Logger log = LoggerFactory.getApplicationLog(StartServer.class);
	public static final String DEFAULT_SPRING_CONFIG = "spring-all.xml";
	static ClassPathXmlApplicationContext context;
	private static final String LOGNAME = "StartServer";
	private static File HOME = null;
	private static String PLUGINHOME = null;
	
	public StartServer(){
	}
	@Override
	public void start() {
		log.info(LOGNAME, "{} starting ...");
		getHome();
		loadPluginsClass();
		String[] configPath = getSpringConfigs();
		context = new ClassPathXmlApplicationContext(configPath);
		context.start();
		
	}
	private void loadPluginsClass(){
		String pluginHome = BusiPluginsHelper.getPluginHome();
		PLUGINHOME = pluginHome;
		BusiPluginsHelper.loadPluginsClass(pluginHome);
	}
	public String[] getSpringConfigs(){
		final String[] pluginxms = BusiPluginsHelper.getPluginsConfig(PLUGINHOME);
		try {
			final String ppath = HOME.getCanonicalPath() + File.separator + "config";
			SpringAll_xmlDefine define = new SpringAll_xmlDefine(){
				@Override
				protected void setCustomerXml(StringBuffer xmlBuf) {
					//setPropertyHolder("file:" + ppath + File.separator + "system.properties");
					xmlBuf.append("<import resource=\"").append("file:").append(ppath).append(File.separator);
					xmlBuf.append("spring-dao.xml\"/>");
					xmlBuf.append("<import resource=\"").append("file:").append(ppath).append(File.separator);
					xmlBuf.append("spring-services.xml\"/>");
					if(pluginxms!=null && pluginxms.length>0){
						for(String plugin:pluginxms){
							xmlBuf.append("<import resource=\"").append(plugin).append("\"/>");
						}
					}
				}
				
			};
			define.setPropertyHolder("file:" + ppath + File.separator + "system.properties");
			String springAllPath = ppath + File.separator + DEFAULT_SPRING_CONFIG;
			IOUtil.inputStream2File(IOUtil.str2InputStream(define.getXml()),springAllPath);
			File config = new File(springAllPath);
			if(config.exists() && config.isFile()){
				return new String[]{"file:"+springAllPath};
			}else{
				throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), " get spring configs error.");
			}
		} catch (Exception e) {
			throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), " get spring configs error.",e);
		}
		
//		if(pluginxms!=null && pluginxms.length>0){
//			String[] configs = new String[pluginxms.length + 1];
//			configs[0] = DEFAULT_SPRING_CONFIG;
//			System.arraycopy(pluginxms, 0, configs, 1, pluginxms.length);
//			
//			return configs;
//		}
//		return new String[]{DEFAULT_SPRING_CONFIG};
	}
	
	@Override
	public void stop() {
		try {
            if (context != null) {
                context.stop();
                context.close();
                context = null;
            }
            log.info(LOGNAME, "{} stopped success.");
        } catch (Throwable e) {
        	log.error("{} stopped error:{}", e, LOGNAME);
        }
	}
	public File getHome(){
		if(HOME == null){
			String appHome = BusiPluginsHelper.getAppHome();
			HOME = BusiPluginsHelper.verifyHome(appHome, NB_FRAME_CONFIG);
		}
		return HOME;
	}
}
