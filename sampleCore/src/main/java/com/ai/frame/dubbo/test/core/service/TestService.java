package com.ai.frame.dubbo.test.core.service;

import com.ai.frame.dubbo.common.helper.ContextHelper;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;
import com.ai.frame.dubbo.test.core.bean.User;
import com.ai.frame.dubbo.test.core.dao.TestDao;

public class TestService {
	private TestDao testDao;
	private ContextHelper context;
	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}
	public void setContext(ContextHelper context) {
		this.context = context;
	}
	public OutputObject testLogin(InputObject inobj){
		String userName = inobj.getParams().get("userName");
		String userPwd  = inobj.getParams().get("userPwd");

		User user = testDao.login(userName, userPwd);
		OutputObject outobj = new OutputObject();
		if(user!=null){
			outobj.setReturnCode(Constants.RTNCODE_SUC.getValue());
			outobj.setBean(context.getJsonHelper().convertBean2Map(user));
		}else{
			outobj.setReturnCode(Constants.RTNCODE_ERR.getValue());
			outobj.setReturnMessage("用户名或密码错误");
		}

		return outobj;
	}
}
