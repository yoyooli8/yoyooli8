package com.ai.frame.dubbo.test.services;

import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

public interface TestServiceI {
	public OutputObject login(InputObject inobj);
}
