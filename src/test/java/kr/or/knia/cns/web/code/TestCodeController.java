package kr.or.knia.cns.web.code;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import kr.or.knia.cns.repository.CodeRepository;
import kr.or.knia.cns.test.TestCtrlEnv;

@ContextConfiguration(classes = {
		TestCodeController.Config.class
	})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCodeController extends TestCtrlEnv {

	@Configuration @ComponentScan("kr.or.knia.cns.web.code")
	public static class Config {}
	
	@Autowired
	private CodeRepository repo;

	@Test
	public void _01_코드읽기_파라미터로() throws Throwable {
		mvc.perform(get("/code")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.param("code", "CORPS:N00"))
		
		.andDo(print())
		.andExpect(jsonPath("key.code").value("N00"));
	}

	@Test
	public void _02_코드읽기_URL로() throws Throwable {
		mvc.perform(get("/code/CORPS/N00")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andDo(print())
		.andExpect(jsonPath("key.code").value("N00"));
	}

	@Test
	public void _03_코드읽기_URL에_부모코드로_코드목록을() throws Throwable {
		mvc.perform(get("/code/-")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andDo(print())
		.andExpect(jsonPath("$").isArray());
	}

	@Test
	public void _04_코드저장() throws Throwable {
		mvc.perform(put("/code")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"key\": "
						+ "{\"parent\": \"-\","
						+ " \"code\": \"TEST\"},"
						+ "\"value\": \"testing\","
						+ "\"desc\": \"It's test for code management\","
						+ "\"order\": 12"
						+ "}"))
		.andDo(print())
		;
	}

	@Test
	public void _05_코드저장_오류() throws Throwable {
		mvc.perform(put("/code")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"key\": "
						+ "{\"parent\": \"-\","
						+ " \"code\": null},"
						+ "\"value\": \"testing\","
						+ "\"desc\": \"It's test for code management\","
						+ "\"order\": 12"
						+ "}"))
		.andDo(print())
		.andExpect(jsonPath("error[0].code").value("NotNull"))
		;
	}

	@Test
	public void _06_코드수정() throws Throwable {
		String codeValue = repo.getValue("CORPS", "N00");

		mvc.perform(put("/code")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"key\": "
						+ "{\"parent\": \"CORPS\","
						+ " \"code\": \"N00\"},"
						+ "\"value\": \"testing\","
						+ "\"desc\": \"It's test for code management\","
						+ "\"order\": -12"
						+ "}"))
		.andDo(print())
		.andExpect(jsonPath("value").value(not(codeValue)));
		;

		assertNotEquals(repo.getValue("CORPS", "N00"), codeValue);
	}

	@Test
	public void _07_코드삭제() throws Throwable {
		mvc.perform(delete("/code")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"parent\": \"CORPS\","
						+ " \"code\": \"N01\""
						+ "}"))
		.andDo(print())
		;
	}
}
