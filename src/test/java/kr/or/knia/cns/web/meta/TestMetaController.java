package kr.or.knia.cns.web.meta;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.knia.cns.domain.Header;
import kr.or.knia.cns.domain.HeaderDetail;
import kr.or.knia.cns.domain.Transaction;
import kr.or.knia.cns.repository.CodeRepository;
import kr.or.knia.cns.test.TestCtrlEnv;

@ContextConfiguration(classes = {
		TestMetaController.Config.class
	})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMetaController extends TestCtrlEnv {

	@Configuration @ComponentScan("kr.or.knia.cns.web.meta")
	public static class Config {}
	
	@Autowired
	private CodeRepository codeRepo;

	@Test
	public void _01_거래코드_저장() throws Exception {
		Header hd = new Header("아름다운해더#1");
		hd.addDetail(new HeaderDetail(hd, "거래코드"));
		hd.addDetail(new HeaderDetail(hd, "최순실"));
		hd.addDetail(new HeaderDetail(hd, "정유라"));

		Transaction tx = new Transaction(12, "KNTAXONL");
		tx.setTestNo(13);
		//tx.setTestCode("KNTAXOTS");
		tx.setType(Transaction.Type.ONLINE);
		tx.setJob(codeRepo.getCode("JOBS", "J01"));
		tx.setHeader(hd);
		
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tx);
		
		System.out.println(json);
		
		mvc.perform(put("/meta/transaction")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json)
			)
		.andDo(print())
		.andExpect(status().isOk());

	}
}
