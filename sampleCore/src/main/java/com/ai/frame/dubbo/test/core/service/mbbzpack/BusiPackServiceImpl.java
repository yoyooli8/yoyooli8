package com.ai.frame.dubbo.test.core.service.mbbzpack;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.ai.frame.dubbo.common.util.ClassUtil;
import com.ai.frame.dubbo.common.util.Constants;
import com.ai.frame.dubbo.common.util.IOUtil;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.ai.frame.dubbo.common.util.SystemPropUtil;
import com.ai.frame.dubbo.orm.BsService;
import com.ai.frame.dubbo.spi.FileParam;
import com.ai.frame.dubbo.spi.InputObject;
import com.ai.frame.dubbo.spi.OutputObject;
import com.ai.frame.dubbo.test.core.service.DynamicBusiService.DynamicBean;
import com.ai.frame.dubbo.test.core.service.DynamicBusiService.DynamicBeanReader;
import com.ai.frame.dubbo.test.core.start.plugins.ClassLoadUtil;
import com.ai.frame.dubbo.test.core.util.BusiPluginsHelper;
import com.ai.frame.dubbo.test.core.util.Constant;
import com.ai.frame.dubbo.test.core.util.PropUtil;

public class BusiPackServiceImpl extends BsService implements BusiPackService {
	private DynamicBeanReader dynamicBeanReader;
	public void setDynamicBeanReader(DynamicBeanReader dynamicBeanReader) {
		this.dynamicBeanReader = dynamicBeanReader;
	}

	@Override
	public OutputObject uploadBusiJar(InputObject inobj) {
		String buziId = inobj.getParams().get("buziId");
		OutputObject outobj = new OutputObject();
		int fileSize = inobj.getFileSize();
		if(fileSize == 0){
			outobj.setReturnCode(Constants.RTNCODE_ERR.getValue());
			outobj.setReturnMessage("请上传["+buziId+"]业务实现包.");
		}else{
			FileParam fileParam = inobj.getFilels(0);
			byte[] filebytes    = fileParam.getFileIn();
			String fileType     = fileParam.getFileType();
			String pluginHome   = BusiPluginsHelper.getPluginHome();
			//生成临时文件
			String tmpFileName  = pluginHome + File.separator + "tmp" + File.separator + StringUtil.getUUID() + fileType;
			IOUtil.bytes2File(filebytes, tmpFileName, IOUtil.BUFFER);
			//计算jar包md5
			String jarmd5  = null;
			InputStream in = null;
			try{
				in     = new FileInputStream(new File(tmpFileName));
				jarmd5 = IOUtil.md5File(in, IOUtil.BUFFER);
				//获取业务包当前的md5值
				String versionFile = pluginHome + File.separator + "version.properties";
				String oldmd5 = PropUtil.getInstance().getValue(versionFile, buziId);
				//如果有更新则更新本地的jar包和版本信息
				if(!jarmd5.equals(oldmd5)){//不相等说明有更新
					//TODO 接入分布式文件系统存储方式
					String jarfileName = pluginHome + File.separator + buziId + File.separator + buziId + fileType;
					IOUtil.bytes2File(filebytes, jarfileName, IOUtil.BUFFER);
					
					File jarFile = new File(jarfileName);
					if(jarFile.exists() && jarFile.isFile()){
						PropUtil.getInstance().setValue(versionFile, buziId, jarmd5);
					}
					
					outobj.setReturnCode(Constants.RTNCODE_SUC.getValue());
					outobj.setReturnMessage("更新["+buziId+"]业务实现包成功.");
				}else{
					outobj.setReturnCode(Constants.RTNCODE_SUC.getValue());
					outobj.setReturnMessage("["+buziId+"]业务实现包不需要更新.");
				}
				
				//删除临时包
				File tmpFile = new File(tmpFileName);
				if(tmpFile.exists() && tmpFile.isFile()){
					tmpFile.delete();
				}
			}catch (Exception e) {
				outobj.setReturnCode(Constants.RTNCODE_ERR.getValue());
				outobj.setReturnMessage("上传["+buziId+"]业务实现包失败.");
			}finally{
				IOUtil.closeInputStream(in);
			}
			
		}
		
		return outobj;
	}

	@Override
	public OutputObject checkBusiJar(InputObject inobj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputObject loadBusiJar(InputObject inobj) {
		String beanName    = inobj.getParams().get("beanName");
		String dynamicBean = inobj.getParams().get("dynamicBean");
		String busiId      = inobj.getParams().get("busiId");
		String pluginDir   = SystemPropUtil.getString(Constant.pluginHome.getVal()) + "/" + busiId;
		//加载pluginJar到当前环境中
		ClassLoadUtil.loadClass(new File(pluginDir));
		
		//动态加载plugin service's bean
		DynamicBean pluginDynamicBean = ClassUtil.getInstance(dynamicBean, DynamicBean.class);
		pluginDynamicBean.setBeanName(beanName);
		dynamicBeanReader.loadBean(pluginDynamicBean);
		
		//write xml to disk
		String xmlpath = pluginDir + "/" + busiId + ".xml";
		IOUtil.inputStream2File(IOUtil.str2InputStream(pluginDynamicBean.getXml()), xmlpath);
		
		OutputObject output = new OutputObject();
		output.setReturnCode(Constants.RTNCODE_SUC.getValue());
		output.setReturnMessage("动态加载业务组件成功.");
		return output;
	}

	@Override
	public OutputObject notifyWebUpdateBusiPack(InputObject inobj) {
		// TODO Auto-generated method stub
		return null;
	}

}
