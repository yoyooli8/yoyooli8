package com.ai.frame.dubbo.test.core.service.mbbzpack;

import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

public interface BusiPackService {
	/**业务Jar包上传*/
	public OutputObject uploadBusiJar(InputObject inobj);
	/**业务Jar包合检验,查看是否有更新*/
	public OutputObject checkBusiJar(InputObject inobj);
	/**把新业务Jar加载到当前的classload里面*/
	public OutputObject loadBusiJar(InputObject inobj);
	/**通知后台应用更新业务Jar包*/
	public OutputObject notifyWebUpdateBusiPack(InputObject inobj);
}
