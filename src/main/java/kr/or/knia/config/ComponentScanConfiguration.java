package kr.or.knia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * <pre>
 * 	&#64;ComponentScan 은 &#64;Component 종류의 annotation 들.
 * 	즉, &#64;Controller, &#64;Service, &#64;Repository 가 달려있는
 *	class 들을 Spring Bean 으로 등록해 줘요.
 *	Spring Bean 으로 등록 되어야지만 URL 호출을 했을때,
 *	그에 맞는 &#64;RequestMapping 이 실행 되니까요.
 *  &#64;Autowired 또는 &#64;Inject 를 이용해서 객체를 받아올 수 있구요.
 *
 *	basePackages 에 명시된 패키지 아래에 있는 것들만 읽어와요.
 *	그리고 &#64;Configuration 이 달려있는 class 는 제외하고 읽어오죠.
 * </pre>
 * 
 * @author zkyz 2014. 12. 10.
 */
@Configuration
@ComponentScan(
	basePackages = {"kr.or.knia"}, 
	excludeFilters = {
			@ComponentScan.Filter(value = Configuration.class, type = FilterType.ANNOTATION)
	}/*,
	lazyInit = true*/
)
public class ComponentScanConfiguration {
}