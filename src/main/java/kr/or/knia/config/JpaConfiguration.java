package kr.or.knia.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
		basePackages = "kr.or.knia.cns.repository",
		namedQueriesLocation = "classpath:sql/**/*"
		)
@EnableTransactionManagement
public class JpaConfiguration {
	
	@Autowired
	private DataSource dataSource;

	@Value("${application.base.package}")
	public String basePackage;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(dataSource);
		bean.setPackagesToScan(basePackage + ".cns.domain");
		
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		bean.setJpaVendorAdapter(adapter);
		
		Properties prop = new Properties();
		prop.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		//prop.put("hibernate.implicit_naming_strategy", "org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl");
		prop.put("hibernate.physical_naming_strategy", "kr.or.knia.config.jpa.strategy.ImprovedNamingStrategy");
		prop.put("hibernate.hbm2ddl.auto", "none");
		prop.put("hibernate.show_sql", "true");
		prop.put("hibernate.format_sql", "true");
		//prop.put("hibernate.use_sql_comments", "true");
		bean.setJpaProperties(prop);

		return bean;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager tx = new JpaTransactionManager(emf);
		return tx;
	}
}
