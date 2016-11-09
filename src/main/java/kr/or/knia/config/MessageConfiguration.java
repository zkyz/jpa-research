package kr.or.knia.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 국제화(i18n) 메세지를 지원하는 것이죠.
 * <pre>
 * 예) test 라는 메세지 프로퍼티 파일이 있으면
 * 다음의 순서대로 파일을 뒤져보게 됩니다.
 * 1. test_ko_kr.xml 
 * 2. test_ko_KR.xml
 * 3. test_ko.xml
 * 4. test.xml
 *
 * 언어에 맞는 메세지를 순차적으로 읽어 들여주게 되어있어요.
 * resources 아래의 message 경로 아래 쪽에 XML 또는 properties 파일을 넣어두면
 * 모조리 다 읽어 들이게 되어있어요.
 * 
 * 읽어 들인 메세지를 가져오고 싶으면 이렇게 하세요.
 *
 * 	&#64;Autowired
 * 	MessageSource messages;
 * 
 *	...
 *  
 *	messages.getMessage("메세지코드", new Object[]{"첫번째 파라미터"}, new Locate("ko"));
 * 
 * @author zkyz 2014. 12. 10.
 */
@Configuration
public class MessageConfiguration {

	@Value("${message.reload.interval:60}")
	private int reloadInterval;

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() throws IOException {
		Set<String> resources = new HashSet<String>();

		PathMatchingResourcePatternResolver resourceFinder = new PathMatchingResourcePatternResolver();
		for(Resource resource : Arrays.asList(resourceFinder.getResources("classpath*:message/**/*"))) {
			if(resource.getFile().isFile()) {
				String name = resource.getURL().toString();
				name = name.replaceAll("\\.[a-zA-Z]+$", "");
				resources.add(name);
			}
		}

		ReloadableResourceBundleMessageSource message = new ReloadableResourceBundleMessageSource();
		message.setBasenames(resources.toArray(new String[0]));
		message.setCacheSeconds(reloadInterval);

		return message;
	}
}
