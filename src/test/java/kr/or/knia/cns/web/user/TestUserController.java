package kr.or.knia.cns.web.user;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.knia.cns.domain.User;
import kr.or.knia.cns.domain.User.EnableSystem;
import kr.or.knia.cns.domain.User.UserStatus;
import kr.or.knia.cns.repository.CodeRepository;
import kr.or.knia.cns.repository.UserRepository;
import kr.or.knia.cns.test.TestCtrlEnv;

@ContextConfiguration(classes = {
	TestUserController.Config.class
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserController extends TestCtrlEnv {

	@Configuration @ComponentScan("kr.or.knia.cns.web.user")
	public static class Config {}

	@Autowired
	public UserRepository repo;

	@Autowired
	public CodeRepository codeRepo;

	@Before
	public void setup() {
		assertNotNull(ctx);
	}

	@Test
	public void _01_로그인_성공() throws Exception {
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				.param("pin", "@dm1n1$tr@t0r")
				.param("enableSystem", "OPERATION")
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("id").value("admin"))
		.andExpect(jsonPath("pinErrorCount").value(0));
	}
	@Test
	public void _02_로그인_아이디_입력_안했다면() throws Exception {
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				//.param("id", "admin")
				.param("pin", "admin")
				.param("enableSystem", "OPERATION")
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("NotEmpty.user.id"));
	}
	@Test
	public void _03_로그인_비번_입력_안했다면() throws Exception {
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				//.param("pin", "admin")
				.param("enableSystem", "OPERATION")
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("NotEmpty.user.pin"));
	}
	@Test
	public void _04_로그인_사용시스템_선택을_안했다면() throws Exception {
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				.param("pin", "admin")
				//.param("enableSystem", "OPERATION")
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("NotEmpty.user.enableSystem"));
	}
	@Test
	public void _05_로그인_계정도없으면서_한번_건드려봄() throws Exception {
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "@#$!$#$@$#@ㅁㅇㄹ")
				.param("pin", "ADFADSF#$@asdfas")
				.param("enableSystem", "OPERATION")
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("login.deny.anonymous"));
	}
	@Test
	public void _06_로그인_비번을_잃어버렸다면() throws Exception {
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				.param("pin", "ADFADSF#$@asdfas")
				.param("enableSystem", "OPERATION")
				)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("login.deny.anonymous"));
	}
	@Test
	public void _07_로그인_비번을_많이_틀렸다면() throws Exception {
		User admin = repo.findById("admin");
		admin.setPinErrorCount(5);
		repo.save(admin);

		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				.param("pin", "!@#ADAD3")
				.param("enableSystem", "OPERATION"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("login.deny.locked"));
	}
	@Test
	public void _08_로그인_사용할수없는_시스템_선택() throws Exception {
		// given
		User admin = repo.findById("admin");
		admin.setEnableSystem(EnableSystem.NONE);
		repo.save(admin);

		// when
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				.param("pin", "@dm1n1$tr@t0r")
				.param("enableSystem", "OPERATION")
				)

		// then
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("login.deny.enable-system"));
	}
	@Test
	public void _09_로그인_미승인상태() throws Exception {
		// given
		User admin = repo.findById("admin");
		admin.setStatus(UserStatus.REQUEST);
		repo.save(admin);

		// when
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				.param("pin", "@dm1n1$tr@t0r")
				.param("enableSystem", "OPERATION")
				)

		// then
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("login.deny.not-approved"));
	}
	@Test
	public void _10_로그인_비밀번호변경일_초과() throws Exception {
		// given
		Calendar someday = Calendar.getInstance();
		someday.add(Calendar.DATE, -90);

		User admin = repo.findById("admin");
		admin.setPinChanged(someday);
		repo.save(admin);

		// when
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				.param("pin", "@dm1n1$tr@t0r")
				.param("enableSystem", "OPERATION")
				)

		// then
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("login.deny.fusty-pin"));
	}
	@Test
	public void _11_로그인_장기미사용() throws Exception {
		// given
		Calendar someday = Calendar.getInstance();
		someday.add(Calendar.DATE, -90);
		
		User admin = repo.findById("admin");
		admin.setAccessed(someday);
		repo.save(admin);


		// when
		mvc.perform(
				post("/user/login")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.param("id", "admin")
				.param("pin", "@dm1n1$tr@t0r")
				.param("enableSystem", "OPERATION")
				)

		// then
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[0].code").value("login.deny.sleep"));
	}
	
	@Test
	public void _12_사용자_등록_실패() throws Exception {
		mvc.perform(put("/user")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content("{}")
				)

		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("error[*].field").exists());
	}
	@Test
	public void _13_사용자_등록_성공() throws Exception {
		// given
		mvc.perform(put("/user")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(
						  "{\"id\": \"test\","
						+ "\"pin\": \"check$1301\","
						+ "\"name\": \"tester\","
						+ "\"corporation\": " + new ObjectMapper().writeValueAsString(codeRepo.getCode("CORPS", "N00")) + ","
						+ "\"enableSystem\": \"TEST\","
						+ "\"jobs\": [" + new ObjectMapper().writeValueAsString(codeRepo.getCode("JOBS", "J01")) + ","
								+ " " + new ObjectMapper().writeValueAsString(codeRepo.getCode("JOBS", "J02")) + "]}"
						))

		// then
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("id").value("test"));
	}
	@Test
	public void _14_사용자_수정() throws Exception {
		// given
		mvc.perform(put("/user")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{  \"no\" : 0,  \"id\" : \"admin\",  \"name\" : \"통치자\",  \"corporation\" : {    \"key\" : {      \"parent\" : \"CORPS\",      \"code\" : \"N00\"    },    \"value\" : \"손해보험협회\",    \"desc\" : \"손보협회\",    \"order\" : 0,    \"use\" : true  },  \"enableSystem\" : \"ALL\",  \"status\" : \"APPROVE\",  \"type\" : \"SYSOP\",  \"pinErrorCount\" : 0,  \"pinChanged\" : 1478141251000,  \"accessed\" : null,  \"requested\" : 1478141251000,  \"approved\" : 1478141251000,  \"jobs\" : [ {    \"key\" : {      \"parent\" : \"JOBS\",      \"code\" : \"J01\"    },    \"value\" : \"다중이용업소\",    \"desc\" : null,    \"order\" : 1,    \"use\" : true  }, {    \"key\" : {      \"parent\" : \"JOBS\",      \"code\" : \"J02\"    },    \"value\" : \"대리점관리\",    \"desc\" : null,    \"order\" : 2,    \"use\" : true  }, {    \"key\" : {      \"parent\" : \"JOBS\",      \"code\" : \"J03\"    },    \"value\" : \"보험가입조회\",    \"desc\" : null,    \"order\" : 3,    \"use\" : true  } ]}"))

		// then
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("id").value("admin"));
	}
}
