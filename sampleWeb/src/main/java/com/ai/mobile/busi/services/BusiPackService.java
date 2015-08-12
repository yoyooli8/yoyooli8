package com.ai.mobile.busi.services;

import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;

/**业务包处理业务*/
public interface BusiPackService {
	/**业务包上传*/
	public OutputObject uploadBusiZipPack(InputObject inobj);
	/**业务包合法性检验*/
	public OutputObject checkBusiZipPack(InputObject inobj);
	/**解压业务包*/
	public OutputObject unPackBusiZipPack(InputObject inobj);
	/**生成业务包和业务包中的文件版本*/
	public OutputObject makeBusiPackFileVersion(InputObject inobj);
	/**上传业务Jar文件到App*/
	public OutputObject uploadBusiJar(InputObject inobj);
	/**通知前台应用更新业务包*/
	public OutputObject notifyWebUpdateBusiPack(InputObject inobj);
}
