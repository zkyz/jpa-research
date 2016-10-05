package kr.or.knia.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Placeholder 를 이용하게 설정했어요.
 * XML에서 이용할 때는 ${프로퍼티에 지정한 키 이름} 으로 사용하고,
 * Java 에서 사용할 때는 다음과 같이 하면 돼요.
 * 
 * <pre>
 *	&#64;Value("${프로퍼티에 지정한 키 이름}")
 *	String someprop; 
 * </pre>
 * 
 * resources 아래의 property 경로에 있는 XML 또는 properites 파일은
 * 모조리 읽어 들이게 되어있어요.
 * 
 * @author zkyz 2014. 12. 10.
 */
@Configuration
public class PropertyConfiguration {
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholder() throws IOException {
		String discrimination = System.getProperty("jeus.home");
		boolean dev = "true".equalsIgnoreCase(System.getProperty("dev"));

		PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
		Resource[] resources = resourceLoader.getResources("classpath*:property/*.xml");

		List<Resource> result = new ArrayList<Resource>(resources.length);
		for(int i = 0; i < resources.length; i++) {
			if(discrimination == null || "".equals(discrimination) || dev) {
				if(resources[i].getFilename().endsWith(".dev.xml")) {
					result.add(resources[i]);
				}
			}
			else {
				if(!resources[i].getFilename().endsWith(".dev.xml")) {
					result.add(resources[i]);
				}
				
			}				
		}

		PropertySourcesPlaceholderConfigurer placeholder = new PropertySourcesPlaceholderConfigurer();
		placeholder.setLocations(result.toArray(new Resource[0]));
		
		return placeholder;
	}
}
