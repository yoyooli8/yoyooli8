package com.ai.frame.dubbo.test.core.dao;

import java.util.HashMap;
import java.util.Map;

import com.ai.frame.dubbo.orm.mybatis.MybatisBsDao;
import com.ai.frame.dubbo.test.core.bean.User;

public class TestDao extends MybatisBsDao{
	public User login(String name,String pwd){
		Map<String,String> param = new HashMap<String,String>();
		param.put("userName", name);
		param.put("userpwd",  pwd);
		
		return executeQueryForObject("login", param);
	}
}
