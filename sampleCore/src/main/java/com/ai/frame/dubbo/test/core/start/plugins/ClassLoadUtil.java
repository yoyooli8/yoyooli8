package com.ai.frame.dubbo.test.core.start.plugins;

import java.io.File;

import org.springframework.util.ClassUtils;

import com.ai.frame.dubbo.common.exception.AinbException;
import com.ai.frame.dubbo.common.util.Constants;

public class ClassLoadUtil {
	public static ClassLoader loadClass(File jarFiles){
		final ClassLoader parent = ClassUtils.getDefaultClassLoader();
		
		try {
			if(jarFiles!=null && jarFiles.isDirectory()){
				ClassLoader loader = new ContainerLoader(parent,jarFiles);
				
				Thread.currentThread().setContextClassLoader(loader);
				return loader;
			}
		} catch (Exception e) {
			throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), "初始化classloader失败.", e);
		}
		return null;
	}
	
	public static ClassLoader loadClass(File jarFiles,String configPath){
		final ClassLoader parent = ClassUtils.getDefaultClassLoader();
		
		try {
			if(jarFiles!=null && jarFiles.isDirectory()){
				ContainerLoader loader = new ContainerLoader(parent,jarFiles);
				loader.addURL(configPath);
				
				Thread.currentThread().setContextClassLoader(loader);
				return loader;
			}
		} catch (Exception e) {
			throw new AinbException(Constants.ERR_FRAME_NAME.getValue(), "初始化带子目录["+configPath+"]的classloader失败.", e);
		}
		return null;
	}
}
