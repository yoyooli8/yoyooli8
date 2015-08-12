package com.ai.frame.dubbo.common.tools.cache;

import java.util.Date;

import com.ai.frame.dubbo.common.helper.CacheHelper;
import com.ai.frame.dubbo.common.util.StringUtil;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

@SuppressWarnings("serial")
public class OscacheHelper extends GeneralCacheAdministrator implements CacheHelper {
	private int refreshPeriod;//过期时间(单位为秒)
	private static final String CACHE_PATH_KEY = "cache.path"; 
	private static final String KEYPREFIX = "com.ai.frame.dubbo.oscache";
	
	public OscacheHelper(){
		this(86400);//86400秒，24小时
	}
	public OscacheHelper(int refreshPeriod){
		super();
		String cachePath = this.getClass().getClassLoader().getResource("/").getPath();
		if(StringUtil.isEmpty(cachePath)){
			cachePath = this.getClass().getClassLoader().getResource("com/ai/frame/dubbo/common/helper").getPath();
		}
		this.setOverflowPersistence(true);
		this.config.set(GeneralCacheAdministrator.CACHE_BLOCKING_KEY, true);
		this.config.set(CACHE_PATH_KEY, cachePath);
		
		this.refreshPeriod = refreshPeriod;
	}
	
	public <T> void put(String key, T value) {
		this.putInCache(makCaheKey(key),value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		try{
			return (T)this.getFromCache(makCaheKey(key),this.refreshPeriod);
		}catch (NeedsRefreshException e) {
			this.cancelUpdate(makCaheKey(key));
		}
		
		return null;
	}

	public void remove(String key) {
		this.flushEntry(makCaheKey(key));
	}

	public void clear() {
		this.flushAll();
	}

	public void clear(Date date) {
		this.flushAll(date);
	}
	private String makCaheKey(String key){
		return KEYPREFIX + "_" + key;
	}
}
