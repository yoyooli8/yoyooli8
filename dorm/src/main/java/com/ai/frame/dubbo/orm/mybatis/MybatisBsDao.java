package com.ai.frame.dubbo.orm.mybatis;

import java.util.List;
import java.util.Map;

import com.ai.frame.dubbo.common.log.Logger;
import com.ai.frame.dubbo.common.log.LoggerFactory;
import com.ai.frame.dubbo.orm.BsDao;

public class MybatisBsDao implements BsDao {
	private String namespace = this.getClass().getName();
	private Logger logger = LoggerFactory.getDaoLog(this.getClass());
	private org.apache.ibatis.session.SqlSession sqlSession;
	
	public void setSqlSession(org.apache.ibatis.session.SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	private String getFullStatementName(String statementName){
		String fullStatementName = namespace + "." + statementName;
		logger.info(fullStatementName, "the full statementName is :{}");
		return fullStatementName;
	}
	@Override
	public <T> T executeQueryForObject(String statement,Object parameter) {
		return sqlSession.selectOne(getFullStatementName(statement), parameter);
	}

	@Override
	public int executeQueryForInteger(String statement,Object parameter) {
		Integer rtn = executeQueryForObject(statement, parameter);
		if(rtn == null)return 0;
        return rtn.intValue();
	}

	@Override
	public <T> List<T> executeQueryForList(String statement,Object parameter) {
		
		return sqlSession.selectList(getFullStatementName(statement), parameter);
	}

	@Override
	public <K, V>Map<K,V> executeQueryForMap(String statement, Object parameter,String key) {
		return sqlSession.selectMap(getFullStatementName(statement), parameter, key);
	}

	@Override
	public int executeUpdate(String statement, Object parameter) {
		return sqlSession.update(getFullStatementName(statement), parameter);
	}

	@Override
	public int executeInsert(String statement, Object parameter) {
		return sqlSession.insert(getFullStatementName(statement), parameter);
	}

	@Override
	public int executeInsertByNotKey(String statement,Object parameter) {
		return sqlSession.insert(getFullStatementName(statement), parameter);
	}

	@Override
	public int executeDelete(String statement, Object parameter) {
		return sqlSession.delete(getFullStatementName(statement), parameter);
	}

	@Override
	public int executeBatchDelete(String statement, List<?> parameterList,int num) {
		return sqlSession.delete(getFullStatementName(statement), parameterList);
	}

	@Override
	public int executeBatchInsert(String statement, List<?> parameterList,int num) {
		return sqlSession.insert(getFullStatementName(statement), parameterList);
	}

	@Override
	public int executeBatchUpdate(String statement, List<?> parameterList,int num) {
		return sqlSession.update(getFullStatementName(statement), parameterList);
	}

	@Override
	public int executeInsertOrUpdate(String countStatement,
			String insertStatement, String updateStatement,Object parameter) {
		Integer count = (Integer) this.executeQueryForInteger(countStatement, parameter);
		if (null != count && count.intValue() > 0) {
            int u = this.executeUpdate(updateStatement, parameter);
            return new Integer(u);
        }
		return this.executeInsert(insertStatement, parameter);
	}

}
