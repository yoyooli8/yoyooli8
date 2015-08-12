package com.ai.frame.dubbo.action;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.common.util.StringUtil;

public class Log4JinitListener implements ServletContextListener{
	private static final String LOG4JCONFIG = "log4jConfig";
	private static final String LOG4JDEFCONFIG = "com/ai/logger/log4j.properties";
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String log4jConfig = event.getServletContext().getInitParameter(LOG4JCONFIG);
		
		initLog4j(log4jConfig);
	}
	private void initLog4j(String log4jConfig){
		System.out.println("----- Log4Jinit Start ----");
		InputStream in = null;
		try{
			if(StringUtil.isEmpty(log4jConfig)){
				log4jConfig = LOG4JDEFCONFIG;
			}
			Resource rs = new ClassPathResource(log4jConfig);
			in = rs.getInputStream();
			
			Properties ps = new Properties();
			ps.load(in);
			PropertyConfigurator.configure(ps);
		}catch(Exception e){
			System.err.println("----- Log4Jinit load error ----"+e.getMessage());
		}finally{
			IOUtil.closeInputStream(in);
		}
		System.out.println("----- Log4Jinit End ----");
	}
}
