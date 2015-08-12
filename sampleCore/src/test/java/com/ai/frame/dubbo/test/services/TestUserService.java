package com.ai.frame.dubbo.test.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;
import com.ai.frame.dubbo.test.core.service.mbbzpack.BusiPackService;
import com.ai.frame.dubbo.test.core.service.user.UserServiceI;
import com.ai.frame.dubbo.test.core.util.BusiPluginsHelper;

public class TestUserService{
	private ApplicationContext context;
	@Before
	public void setUp(){
		String paths[] = new String[]{"spring/spring-all.xml"};
		context = new ClassPathXmlApplicationContext(paths);
	}
	public <T>T getBean(String beanName,Class<T> rtnType){
		return context.getBean(beanName, rtnType);
	}
	
	@Test
	public void testGetPluginsConfig(){
		String[] configs = BusiPluginsHelper.getPluginsConfig(BusiPluginsHelper.getPluginHome());
		Assert.assertNotNull(configs);
		
		for(String config:configs){
			System.out.println(config);
		}
	}
	@Test
	public void testDynamicBeanLoad(){
		BusiPackService busiService = getBean("busiPackService",BusiPackService.class);
		
		InputObject inobj = new InputObject();
		inobj.getParams().put("beanName",   "testBusiService");
		inobj.getParams().put("dynamicBean","com.ai.frame.dubbo.test.services.plugins.TestPluginDynamicBean");
		inobj.getParams().put("busiId",     "testPlugin1");
		OutputObject out = busiService.loadBusiJar(inobj);
		Assert.assertNotNull(out);
		Assert.assertEquals(Constants.RTNCODE_SUC.getValue(), out.getReturnCode());
		
		
		//测试动态加载的bean
		InputObject inobj1 = new InputObject();
		inobj1.getParams().put("userName", "测试名字");
		inobj1.getParams().put("userPwd",  "1234560");
		
		TestServiceI testBusiService = getBean("testBusiService",TestServiceI.class);
		
		OutputObject outobj = testBusiService.login(inobj1);
		Assert.assertNotNull(outobj);
		Assert.assertEquals(Constants.RTNCODE_SUC.getValue(), outobj.getReturnCode());
		System.out.println("------user--------"+outobj.getBean());
	}
	@Test
	public void testLogin(){
		
		UserServiceI service = getBean("userService",UserServiceI.class);
		
		InputObject inobj = new InputObject();
		inobj.getParams().put("userName", "测试名字");
		inobj.getParams().put("userPwd",  "1234560");
		
		OutputObject outobj = service.testLogin(inobj);
		Assert.assertNotNull(outobj);
		Assert.assertEquals(Constants.RTNCODE_SUC.getValue(), outobj.getReturnCode());
		System.out.println("------user--------"+outobj.getBean());
	}
}
