package com.ai.frame.dubbo.orm;

import com.ai.frame.dubbo.common.helper.ContextHelper;

/**service抽象父类*/
public abstract class BsService {
	protected ContextHelper context;
	public void setContext(ContextHelper context) {
		this.context = context;
	}
}
