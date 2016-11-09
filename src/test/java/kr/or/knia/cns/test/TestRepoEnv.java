package kr.or.knia.cns.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import kr.or.knia.config.DataSourceConfiguration;
import kr.or.knia.config.JpaConfiguration;
import kr.or.knia.config.PropertyConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		PropertyConfiguration.class,
		DataSourceConfiguration.class,
		JpaConfiguration.class
})
@Transactional
public class TestRepoEnv {
}
