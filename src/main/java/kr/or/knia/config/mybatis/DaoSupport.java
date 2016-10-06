package kr.or.knia.config.mybatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import kr.or.knia.config.mybatis.page.Paginate;

public abstract class DaoSupport extends SqlSessionDaoSupport {

	@Autowired
	public void setSuperSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	public <T> List<T> list(String queryId, Object param, RowBounds rowBounds) {
		return getSqlSession().<T>selectList(queryId, param, rowBounds);
	}
	public <T> List<T> list(String queryId, Object param) {
		return getSqlSession().<T>selectList(queryId, param);
	}
	public <T> List<T> list(String queryId) {
		return list(queryId, null);
	}

	public <T> T item(String queryId, Object param) {
		T t = null;
		if(!(param instanceof Paginate)) return getSqlSession().<T>selectOne(queryId, param);
		else {
			Paginate pagination = (Paginate)param;
			pagination.setEnabled(false);
			t = getSqlSession().<T>selectOne(queryId, param);
			pagination.setEnabled(true);

			return t;
		}
	}
	
	public <T> T item(String queryId) {
		return item(queryId, null);
	}
	
	public int insert(String queryId, Object param) {
		return getSqlSession().insert(queryId, param);
	}
	public int insert(String queryId) {
		return insert(queryId, null);
	}
	
	public int update(String queryId, Object param) {
		return getSqlSession().update(queryId, param);
	}
	public int update(String queryId) {
		return update(queryId, null);
	}
	
	public int merge(String queryId, Object param) {
		return getSqlSession().update(queryId, param);
	}
	public int merge(String queryId) {
		return update(queryId, null);
	}

	public int delete(String queryId, Object param) {
		return getSqlSession().delete(queryId, param);
	}
	public int delete(String queryId) {
		return delete(queryId, null);
	}
}