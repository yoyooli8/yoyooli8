package com.ai.frame.dubbo.orm;

import java.util.List;
import java.util.Map;

/**dao父接口类*/
public interface BsDao {
	int BATCHNUM = 500;
	/***
	 * 查找单个对象，可以统计记录的个数
	 * @param <T>
	 * @param statementName
	 * @param parameterObject
	 * @return
	 */
	<T> T executeQueryForObject(String statementName, Object parameterObject);
	/**
	 * 查询,返回int类型数据
	 * @param statementName
	 * @param parameterObject
	 * @return
	 * @throws DaoException
	 */	
	int executeQueryForInteger(String statementName, Object parameterObject);
	/**
	 * 查询,返回list
	 * @param <T>
	 * @param statementName
	 * @param parameterObject
	 * @return
	 */
	<T> List<T> executeQueryForList(String statementName, Object parameterObject);
	/**
	 * 查询,返回MAP
	 * @param statementName
	 * @param parameterObject
	 * @param key
	 * @return
	 */
	<K, V>Map<K,V> executeQueryForMap(String statementName, Object parameterObject, String key);
	/**
	 * 更新数据库，可以插入一条记录，也可以删除一条记录 返回受影响的条数
	 * @param statementName
	 * @param parameterObject
	 * @return
	 */
	int executeUpdate(String statementName, Object parameterObject);
	/**
	 * 插入一条记录
	 * @param statementName
	 * @param parameterObject
	 * @return 新增加的记录主键
	 */
	int executeInsert(String statementName, Object parameterObject);
	/**
	 * 中间表或者表没有sequence的调用该方法执行插入操作
	 * @param statementName
	 * @param parameterObject
	 * @return
	 */
	int executeInsertByNotKey(String statementName, Object parameterObject);
	/**
	 * 删除记录
	 * @param statementName
	 * @param parameterObject
	 * @return
	 */
	int executeDelete(String statementName, Object parameterObject);
	/**
	 * 批量删除
	 * @param statementName
	 * @param parameterList
	 * @param num          多少条数据执行删除一次
	 * @return             1:删除成功，0：删除失败
	 */
	int executeBatchDelete(String statementName, List<?> parameterList, int num);
	/**
	 * 批量插入记录
	 * @param statementName
	 * @param parameterList    需要插入的记录数
	 * @param num              多少条数据插入一次
	 * @return                 1:插入成功，0：插入失败
	 */
	int executeBatchInsert(String statementName, List<?> parameterList, int num);
	/**
	 * 批量更新记录
	 * @param statementName
	 * @param parameterList        需要更新的记录
	 * @param num                  多少条数据更新一次
	 * @return                     1:更新成功，0：失败
	 */
	int executeBatchUpdate(String statementName, List<?> parameterList, int num);
	/**
	 * 插入或是更新一条记录
	 * @param countStatementName  用来查询是否存在记录的
	 * @param insertStatementName 用来插入记录的
	 * @param updateStatementName 用来更新记录的
	 * @param parameterObject     参数列表
	 * @return
	 */
	int executeInsertOrUpdate(String countStatementName, String insertStatementName, String updateStatementName, Object parameterObject);
}
