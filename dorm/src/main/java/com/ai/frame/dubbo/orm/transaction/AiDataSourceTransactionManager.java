package com.ai.frame.dubbo.orm.transaction;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.ai.frame.dubbo.common.encryption.EncryptionUtil;
import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;

@SuppressWarnings("serial")
public class AiDataSourceTransactionManager extends DataSourceTransactionManager{
	private Logger logger = LoggerFactory.getDaoLog(this.getClass());
	private static final String DESENCRYP = "{des}";
	private static final String AESENCRYP = "{aes}";
	
	public void setDataSource(DataSource dataSource) {
		String getPwdMethod = "getPassword";
		Class<?>[] parameterTypes = null;
		String decryPassword = null;
		String password      = null;
		try {
			Method method = dataSource.getClass().getMethod(getPwdMethod, parameterTypes);
			Object[] args = null;
			password = (String)method.invoke(dataSource, args);
		} catch (SecurityException e) {
			logger.error("the {} method in DataSource not permission Access.erro:{}", e, getPwdMethod);
		} catch (NoSuchMethodException e) {
			logger.error("the {} method in DataSource does't exist.erro:{}", e, getPwdMethod);
		}catch (Exception e) {
			logger.error("the {} method in DataSource invoke erro:{}", e, getPwdMethod);
		} 
		try {
			if(password!=null && password.startsWith(DESENCRYP)){
				decryPassword = new String(EncryptionUtil.desDecode(EncryptionUtil.hexStrDecode2Bytes(password.substring(DESENCRYP.length())), EncryptionUtil.DEFAULTKEY.getBytes()));
			}else if(password!=null && password.startsWith(AESENCRYP)){
				decryPassword = new String(EncryptionUtil.aesDecode(EncryptionUtil.hexStrDecode2Bytes(password.substring(AESENCRYP.length())), EncryptionUtil.DEFAULTAESKEY.getBytes()));
			}else{
				decryPassword = password;
			}
		} catch (IllegalArgumentException e) {
			logger.error("setDataSource", "{} dataSource pwd deConvert error:{}", e);
		} catch (Exception e) {
			logger.error("setDataSource", "{} dataSource pwd deConvert error:{}", e);
		}
		Class<?>[] setParameterTypes = new Class<?>[]{String.class};
		String setPassword = "setPassword";
		try {
			Method method = dataSource.getClass().getMethod(setPassword, setParameterTypes);
			Object[] args = new Object[]{decryPassword};
			method.invoke(dataSource, args);
		} catch (SecurityException e) {
			logger.error("the {} method in DataSource not permission Access.erro:{}", e, setPassword);
		} catch (NoSuchMethodException e) {
			logger.error("the {} method in DataSource does't exist.erro:{}", e, setPassword);
		} catch (Exception e) {
			logger.error("the {} method in DataSource invoke erro:{}", e, setPassword);
		} 
		super.setDataSource(dataSource);
	}
}
