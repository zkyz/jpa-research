package kr.or.knia.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

import kr.or.knia.config.mybatis.SQLLoggingInterceptor;
import kr.or.knia.config.mybatis.page.PaginateForOracleInterceptor;
import kr.or.knia.config.mybatis.typehandler.FileTypeHandler;
import kr.or.knia.config.mybatis.typehandler.YES2BooleanTypeHandler;

@Configuration
public class MybatisConfiguration implements ResourceLoaderAware {
	private ResourceLoader resourceLoader;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private DataSource dataSource;

	@Value("${application.base.package:kr.or.knia}")
	private String basePackage;
	
	@PostConstruct
	public void setup() {
		mapperScanner(basePackage);
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
		PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		Resource[] mappers = patternResolver.getResources("classpath:" + basePackage.replaceAll("\\.", "/") + "/**/*Dao.xml");

		//MudyoSqlSessionFactoryBean sqlSessionFactoryBean = new MudyoSqlSessionFactoryBean();
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
		sqlSessionFactoryBean.setMapperLocations(mappers);
		sqlSessionFactoryBean.setTypeAliasesPackage(basePackage);

		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setJdbcTypeForNull(JdbcType.VARCHAR);
		config.setMapUnderscoreToCamelCase(true);
		config.addInterceptor(new SQLLoggingInterceptor());
		config.addInterceptor(new PaginateForOracleInterceptor());

		TypeHandlerRegistry typeHandlerRegistry = config.getTypeHandlerRegistry();
		typeHandlerRegistry.register(new YES2BooleanTypeHandler());
		typeHandlerRegistry.register(new FileTypeHandler());

		sqlSessionFactoryBean.setConfiguration(config);

		SqlSessionFactory ssf = sqlSessionFactoryBean.getObject();
		return ssf;
	}
	
	private void mapperScanner(String basePackage) {
		BeanDefinitionRegistry registry = ((BeanDefinitionRegistry) ctx.getAutowireCapableBeanFactory());
		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

		if (resourceLoader != null) {
			scanner.setResourceLoader(resourceLoader);
		}

		scanner.setAnnotationClass(Repository.class);
		scanner.registerFilters();
		scanner.doScan(new String[]{basePackage});
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
