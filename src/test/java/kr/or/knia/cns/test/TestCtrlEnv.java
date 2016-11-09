package kr.or.knia.cns.test;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import kr.or.knia.config.DataSourceConfiguration;
import kr.or.knia.config.DefaultWebAppConfiguration;
import kr.or.knia.config.JpaConfiguration;
import kr.or.knia.config.MessageConfiguration;
import kr.or.knia.config.PropertyConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		PropertyConfiguration.class,
		DataSourceConfiguration.class,
		JpaConfiguration.class,
		DefaultWebAppConfiguration.class,
		MessageConfiguration.class,
		TestCtrlEnv.Config.class
})
@WebAppConfiguration
@Transactional
public abstract class TestCtrlEnv {
	@Configuration
	@ComponentScan({"kr.or.knia.config.spring"})
	public static class Config {}

	@Autowired
	protected WebApplicationContext ctx;

	protected MockMvc mvc = null;	

	@PostConstruct
	public void createMvc() {
		mvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
}
