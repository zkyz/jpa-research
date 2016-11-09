package kr.or.knia.cns.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.knia.cns.domain.Code;
import kr.or.knia.cns.domain.User;
import kr.or.knia.cns.domain.User.EnableSystem;
import kr.or.knia.cns.domain.User.UserStatus;
import kr.or.knia.cns.test.TestRepoEnv;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserRepository extends TestRepoEnv {

	@Autowired
	private UserRepository repo;

	@Autowired
	private CodeRepository codeRepo;

	@Before
	public void setup() {
		assertNotNull(repo);
		assertNotNull(codeRepo);
	}

	@Test
	public void _1_등록() {
		// given
		Code corporation = codeRepo.getCode("CORPS", "N01");
		User user = new User("test", "lov2$4321", "tester", corporation);
		user.setEnableSystem(EnableSystem.TEST);
		repo.save(user);

		// when
		User someone = repo.findById("test");

		// then
		assertNotNull(someone);
	}

	@Test
	public void _2_수정() throws Throwable {
		// given
		Calendar now = Calendar.getInstance();

		User someone = repo.findById("admin");
		someone.setName("testerer");
		someone.setEnableSystem(EnableSystem.TEST);
		someone.setStatus(UserStatus.APPROVE);
		someone.setApproved(now);
		someone.addJob(codeRepo.getCode("JOBS", "J06"));
		someone.addJob(codeRepo.getCode("JOBS", "J07"));
		someone.addJob(codeRepo.getCode("JOBS", "J08"));
		repo.saveAndFlush(someone);

		// when
		someone = repo.findById("admin");

		// then
		assertSame(someone.getStatus(), UserStatus.APPROVE);
		assertEquals(someone.getApproved().getTime(), now.getTime());
		
		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(someone));
	}
	
	@Test
	public void _3_검색() throws Throwable {
		// given
		Pageable pagable = new PageRequest(0, 10);
		Page<User> users = repo.findAll("", "치", pagable);

		// then
		assertTrue(users.getContent().size() > 0);

		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(users));
	}

	@Test
	public void _4_검색_업무를검색조건으로() throws Throwable {
		// given
		Pageable pagable = new PageRequest(0, 10);
		Page<User> users = repo.findAll("", "치", 
				new HashSet<String>(Arrays.asList("J01", "J02")), 
				pagable);

		// then
		assertTrue(users.getContent().size() > 0);

		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(users));
	}

	@Test
	public void _5_회원과_연결된_업무() {
		// when
		User user = repo.findById("admin");

		System.out.println(user.getJobs());

		// then
		//assertTrue(user.getJob().size() >= 8);

//		for(UserJob j : user.getJobs()) {
//			System.out.println(j);
//		}
	}
}
