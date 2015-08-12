package com.ai.frame.dubbo.test.core.start.plugins;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;

public class ContainerLoader extends URLClassLoader{
	private static final Logger log = LoggerFactory.getApplicationLog(ContainerLoader.class);
	public ContainerLoader(ClassLoader parent, File libDir) throws MalformedURLException{
		super(new URL[] { libDir.toURI().toURL() }, parent);
		
		File[] jars = libDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean accept = false;
				String smallName = trim(name,"").toLowerCase();
				if (smallName.endsWith(".jar")) {
                    accept = true;
                }
				return accept;
			}
			
		});
		
		//Do nothing if no jar files were found
		if (jars == null) {
            return;
        }
		
		//add jar to current classLoad
		for (int i = 0; i < jars.length; i++) {
            if (jars[i].isFile()) {
                addURL(jars[i].toURI().toURL());
            }
        }
	}
	public String trim(String str,String defStr) {
        return str == null || "null".equals(str) ? defStr : str.trim();
    }
	/**添加一个目录到classpath中.*/
	public void addURL(String path){
		try {
			File addpath = new File(path);
			if(addpath.exists() && addpath.isDirectory()){
				addURL(addpath.toURI().toURL());
			}
		} catch (Exception e) {
			log.error("load the [{}] path to classpath error:{}.", e, path);
		}
	}
}
