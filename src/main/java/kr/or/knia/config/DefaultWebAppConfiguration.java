package kr.or.knia.config;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.knia.cns.domain.Code;
import kr.or.knia.cns.domain.User;
import kr.or.knia.config.spring.converter.CodeParamConvert;
import kr.or.knia.config.spring.converter.UserIdParamConvert;
import kr.or.knia.config.spring.formatter.bool.TrueFormatAnnotationFormatterFactory;
import kr.or.knia.config.spring.formatter.text.TextFormatAnnotationFormatterFactory;

/**
 * Spring MVC로 구성된 웹 어플리케이션에 기본적인 설정을
 * 진행하게 됩니다.
 *  
 * @author zkyz 2014. 12. 10.
 */
@Configuration
@EnableWebMvc
@EnableScheduling
//@EnableAsync
public class DefaultWebAppConfiguration extends WebMvcConfigurerAdapter {

	@Value("${multipart.max.size:10240000}") // 1024 * 10 (10MB) 
	private long multipartMaxSize;

	String discrimination = System.getProperty("jeus.home");
	boolean dev = "true".equalsIgnoreCase(System.getProperty("dev"));

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/img/favicon.ico");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeInterceptor());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jsonMessageConverter());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new SortHandlerMethodArgumentResolver());
		argumentResolvers.add(new PageableHandlerMethodArgumentResolver());		
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(codeParamConverter());
		registry.addConverter(userIdParamConverter());
		registry.addFormatterForFieldAnnotation(new TrueFormatAnnotationFormatterFactory());
		registry.addFormatterForFieldAnnotation(new TextFormatAnnotationFormatterFactory());
	}

	@Bean
	public MappingJackson2HttpMessageConverter jsonMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper());
		return converter;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
				.indentOutput(discrimination == null || dev)
				.build();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(multipartMaxSize);
		return resolver;
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(1);
		return resolver;
	}

	@Bean
	public SessionLocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.KOREA);
		return resolver;
	}

	@Bean
	public LocaleChangeInterceptor localeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	@Bean
	public Converter<String, Code> codeParamConverter() {
		return new CodeParamConvert();
	}

	@Bean
	public Converter<String, User> userIdParamConverter() {
		return new UserIdParamConvert();
	}
}