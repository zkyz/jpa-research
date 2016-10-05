package kr.or.knia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * XML 을 이용한 Spring Bean 을 설정할 수 있어요.
 * <code>@ImportResource</code> 에 정의된 경로에 있는 XML을
 * 자동으로 읽어 가거든요. 
 * 
 * @author zkyz 2014. 12. 10.
 */
@Configuration
@ImportResource("classpath:kr/or/knia/config/**/*.xml")
public class XMLSpringBeanConfiguration {
}
