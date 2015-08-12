package com.ai.frame.dubbo.test.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ai.frame.dubbo.common.exception.AinbException;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.test.core.start.Container;
import com.ai.frame.dubbo.test.core.start.plugins.ClassLoadUtil;

public class BusiPluginsHelper {
	private static final Logger log = LoggerFactory.getApplicationLog(BusiPluginsHelper.class);
	
	/**加截所有业务插件包到当前classpath中*/
	public static void loadPluginsClass(String pluginDir){
		if(!StringUtil.isEmpty(pluginDir)){
			File pluginHome    = new File(pluginDir);
			if(pluginHome.exists() && pluginHome.isDirectory()){//插件根据目录
				File[] plugins   = BusiPluginsHelper.getPluginsDir(pluginHome);
				if(plugins == null) return;
				
				for(File plugin:plugins){
					ClassLoadUtil.loadClass(plugin);
				}
			}else{
				log.error(pluginDir, "can't has create {} path.");
			}
		}else{
			log.error(Constant.pluginHome.getVal(), "can't has configed {} properites.");
		}
	}
	/**取得业务插件包根据目录*/
	public static String getPluginHome(){
		String appHome = getAppHome();
		File appHomeF  = verifyHome(appHome, Container.NB_FRAME_CONFIG);
		File pluginFile   = new File(appHomeF,Container.NB_FRAME_CONFIG);
		Properties prop   = new Properties();
		String pluginHome = null;
		try {
			prop.load(new FileInputStream(pluginFile));
			
			pluginHome = prop.getProperty(Constant.pluginHome.getVal());
		} catch (Exception e) {
			throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), Container.NB_FRAME_CONFIG+" file not found.");
		} 
		
		return pluginHome;
	}
	/**取得应用的根据目录*/
	public static String getAppHome(){
		String pluginDir = System.getProperty(Constant.appHome.getVal());

		if(StringUtil.isEmpty(pluginDir)){
			pluginDir = ".";
		}
		
		return pluginDir;
	}
	/**验证目录的合法性*/
	public static File verifyHome(String homeGuess,String configFileName){
		File appHome = new File(homeGuess);
		File configFile = new File(appHome, configFileName);
		if (!configFile.exists()) {
			throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), configFileName+" not found.");
		}
		
		try {
			return new File(appHome.getCanonicalPath());
		} catch (Exception e) {
			throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), "homepath["+homeGuess+"] not found.");
		}
	}
	/**取得所有插件包的目录,@pluginDir 业务插件包根据目录*/
	public static String[] getPluginsConfig(String pluginHome){
		System.out.println("getPluginsConfig:"+pluginHome);
		if(!StringUtil.isEmpty(pluginHome)){
			File[] pluginsDir = getPluginsDir(pluginHome);
			if(pluginsDir!=null){
				final List<String> configLs = new ArrayList<String>();
				for(File pluginDir:pluginsDir){
					pluginDir.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							System.out.println("plugins.name="+name);
							if(name!=null && name.toLowerCase().endsWith(".xml")){
								
								try {
									String xmlpath = "file:" + dir.getCanonicalPath() + File.separator + name;
									configLs.add(xmlpath);
								} catch (IOException e) {
								}
								
								return true;
							}
							return false;
						}
					});
					
				}
				
				return configLs.toArray(new String[configLs.size()]);
			}
		}
		return null;
	}
	/**取得所有插件包的目录,@pluginDir 业务插件包根据目录*/
	public static File[] getPluginsDir(String pluginDir){
		if(!StringUtil.isEmpty(pluginDir)){
			File pluginHome    = new File(pluginDir);
			
			return getPluginsDir(pluginHome);
		}
		return null;
	}
	/**取得所有插件包的目录,@pluginHome 业务插件包根据目录*/
	public static File[] getPluginsDir(File pluginHome){
		if(pluginHome.exists() && pluginHome.isDirectory()){//插件根据目录
			File[] plugins   = pluginHome.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					if(pathname.isDirectory()){
						return true;
					}
					return false;
				}
			});
			
			return plugins;
		}
		return null;
	}
}
