package com.ai.frame.dubbo.test.core.service.user;

import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

public interface UserServiceI {
	public OutputObject testLogin(InputObject inobj);
}
