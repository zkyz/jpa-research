package kr.or.knia.et.supervision.user;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
@Rollback(false)
public class TestUserRepository {

	@Autowired
	private UserRepository user;
	
	@Autowired
	private GroupRepository group;

	@Test
	public void _1_그룹_생성() {
		Integer maxOrder = group.getCurrentMaxOrder();
		if(maxOrder == null) 
			maxOrder = 1;

		group.save(new Group("N00", "손해보험협회", maxOrder ++));
		group.save(new Group("N01", "메리츠손해보험", maxOrder ++));
		//group.save(new Group("N02", "롯데손해보험", maxOrder ++));
	}
	
	
	@Test
	public void _2_유저_생성() {
		Group g = group.findOne("N00");
		
		User u = new User("check", "check1234", "checker", g);
		u.setNo(19231L);
		user.save(u);
	}
	
	
	//@Test
//	public void _2_모든_유저_검색() {
//		List<User> users = userRepo.findAll();
//		
//		for(User user : users) {
//			System.out.println(user);
//		}
//	}
}
