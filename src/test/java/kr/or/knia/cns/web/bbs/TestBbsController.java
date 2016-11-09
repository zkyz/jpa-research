package kr.or.knia.cns.web.bbs;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.knia.cns.domain.Article;
import kr.or.knia.cns.domain.ArticleFile;
import kr.or.knia.cns.domain.User;
import kr.or.knia.cns.repository.ArticleRepository;
import kr.or.knia.cns.repository.UserRepository;
import kr.or.knia.cns.test.TestCtrlEnv;

@ContextConfiguration(classes = {
		TestBbsController.Config.class
	})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBbsController extends TestCtrlEnv {

	@Configuration @ComponentScan("kr.or.knia.cns.web.bbs")
	public static class Config {}

	@Autowired
	private ArticleRepository repo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private MockHttpSession session;

	private User admin;

	@Before
	public void setup() {
		assertNotNull(repo);
		assertNotNull(userRepo);

		admin = userRepo.findOne(0);
		session.setAttribute(User.class.getName(), admin);
	}

	@Test
	public void _1_글쓰기() throws Throwable {
		Article article = new Article();
		article.setUser(admin);
		article.setTitle("Deep dive into Test Driven Develope!");
		article.setText("Can you drive to keyboard into TDD world ?!");
		article.setJob(admin.getJobs().get(0));

		String body = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(article);

		mvc.perform(put("/bbs/notice")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(body))
		.andDo(print())
		;
	}

	@Test
	public void _2_파일첨부() throws Throwable {
		mvc.perform(fileUpload("/bbs/notice/file")
//				.file(new MockMultipartFile("files", new FileInputStream("D:/회의록-요구사항20160223.hwp")))
//				.file(new MockMultipartFile("files", new FileInputStream("D:/구상금추가개발완료보고서_v1.0.doc")))
				.file(new MockMultipartFile("files", new FileInputStream("/Users/zkyz/Desktop/견적대상목록.xlsx")))
				.file(new MockMultipartFile("files", new FileInputStream("/Users/zkyz/Desktop/다시사랑할수있을까.mp3")))
				);
	}

	@Test
	public void _3_파일삭제() throws Throwable {
		ArticleFile file = new ArticleFile();
		file.setArticleNo(1);
		file.setPath("D:/회의록-요구사항20160223.hwp");

		String body = new ObjectMapper().writeValueAsString(file);

		mvc.perform(delete("/bbs/notice/file")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(body)
				);
	}
	
	@Test
	public void _4_목록() throws Throwable {
		mvc.perform(post("/bbs/NOTICE")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.param("page", "0")
				.param("size", "10")
				)
		.andDo(print());
	}
}
