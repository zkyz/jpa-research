package kr.or.knia.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {
	@Value("${application.db.driver:org.h2.Driver}")
	private String driver;

	@Value("${application.db.url:jdbc:h2:./db;AUTO_SERVER=TRUE;MODE=Oracle}")
	private String url;

	@Value("${application.db.username:sa}")
	private String username;

	@Value("${application.db.password:}")
	private String password;

	@Bean(destroyMethod = "close")
	public DataSource dataSource() throws SQLException {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setMaxActive(100);
		ds.setMinIdle(5);
		ds.setRemoveAbandoned(true);
		ds.setRemoveAbandonedTimeout(60 * 10 * 1000); // 10min
		ds.setTestOnBorrow(true);
		ds.setTestOnReturn(false);
		ds.setValidationQueryTimeout(5);
		ds.setValidationQuery("SELECT 1 FROM DUAL");
		ds.setTimeBetweenEvictionRunsMillis(60 * 10 * 1000); // 10min
		ds.setNumTestsPerEvictionRun(10);
		ds.setMinEvictableIdleTimeMillis(60 * 60 * 1000); // 1h
		return ds;
	}
}
