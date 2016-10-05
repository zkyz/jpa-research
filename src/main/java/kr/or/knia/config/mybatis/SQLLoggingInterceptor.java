package kr.or.knia.config.mybatis;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class, CacheKey.class, BoundSql.class }) })
public class SQLLoggingInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger("--- Sql Query ---");

	public Object intercept(Invocation invocation) throws Throwable {
		if(logger.isDebugEnabled() || logger.isTraceEnabled()) {
			MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
			Object param = invocation.getArgs()[1];

			BoundSql boundSql = ms.getBoundSql(param);
			String sql = boundSql.getSql();
			List<ParameterMapping> parameterMapping = boundSql.getParameterMappings();
			
			int parameterIndex = 0;
			boolean inStatement = true;
			StringBuilder out = new StringBuilder();

			try {
				for(char c : sql.toCharArray()) {
					if(c == '\'') {
						inStatement = !inStatement;
					}
					
					if(inStatement && c == '?') {
						if(parameterMapping.size() < parameterIndex) {
							out.append("''");
						}
						else {
							out.append(value(param, parameterMapping.get(parameterIndex ++), ms));
						}
					}
					else {
						out.append(c);
					}
				}
	
				logger.debug("\n    {}\n", out.toString().replaceAll("[\n]+(\\s+\n)?", "\n"));
			}
			catch(Throwable t) {
				// not to do..
			}
		}

		return invocation.proceed();
	}
	
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}
	
	private String value(Object param, ParameterMapping mapper, MappedStatement mappedStatement) {
		Object val = null;

		try {
			val = Ognl.getValue(mapper.getProperty(), param);
		}
		catch(Exception e) {
			val = "/* Needs type handler for " + mapper.getProperty() + "*/";
		}
		
		if(val != null) {
			if(val.getClass().isPrimitive()) {
				return val + "";
			}
			else if(val instanceof Number) {
				return val.toString(); 
			}
			else if(val instanceof CharSequence) {
				return "'" + val + "'";
			}
			else {
				return "/* [" + mapper.getProperty() + "] needs handler*/";
			}
		}

		return "''";
	}
}