package kr.or.knia.config;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.or.knia.config.spring.converter.NumbersHttpMessageConverter;
import kr.or.knia.config.spring.converter.ToDoubleConverter;
import kr.or.knia.config.spring.converter.ToFloatConverter;
import kr.or.knia.config.spring.converter.ToIntegerConverter;
import kr.or.knia.config.spring.converter.ToLongConverter;
import kr.or.knia.config.spring.converter.ToNumberConverter;
import kr.or.knia.config.spring.converter.ToShortConverter;
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
		Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();
		converters.add(new NumbersHttpMessageConverter());
		converters.add(new MappingJackson2HttpMessageConverter(mapperBuilder.build()));
	}
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new ToIntegerConverter());
		registry.addConverter(new ToLongConverter());
		registry.addConverter(new ToShortConverter());
		registry.addConverter(new ToDoubleConverter());
		registry.addConverter(new ToFloatConverter());
		registry.addConverter(new ToNumberConverter());
		registry.addFormatterForFieldAnnotation(new TrueFormatAnnotationFormatterFactory());
		registry.addFormatterForFieldAnnotation(new TextFormatAnnotationFormatterFactory());
	}

//	@Override
//	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
//		returnValueHandlers.add(new HandlerMethodReturnValueHandler() {
//			@Override
//			public boolean supportsReturnType(MethodParameter returnType) {
//				return List.class.isAssignableFrom(returnType.getParameterType());
//			}
//			
//			@Override
//			public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
//					NativeWebRequest webRequest) throws Exception {
//				HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//				objectMapper().getObject().writeValue(response.getOutputStream(), returnValue);
//			}
//		});
//	}
	
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
	}
	
	@Bean
	public MappingJackson2JsonView jsonView() {
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.setObjectMapper(objectMapper().getObject());
		return view;
	}

	@Bean
	public Jackson2ObjectMapperFactoryBean objectMapper() {
		Jackson2ObjectMapperFactoryBean factory = new Jackson2ObjectMapperFactoryBean();
		factory.setFailOnEmptyBeans(false);
		return factory;
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
}