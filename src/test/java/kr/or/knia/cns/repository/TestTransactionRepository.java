package kr.or.knia.cns.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.knia.cns.domain.Form;
import kr.or.knia.cns.domain.Header;
import kr.or.knia.cns.domain.Transaction;
import kr.or.knia.cns.domain.Transaction.Type;
import kr.or.knia.cns.test.TestRepoEnv;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTransactionRepository extends TestRepoEnv {

	@Autowired
	private TransactionRepository repo;

	@Autowired
	private HeaderRepository headerRepo;

	@Autowired
	private CodeRepository codeRepo;
	
	@Before
	public void setup() throws Exception {
		assertNotNull(repo);
		assertNotNull(headerRepo);

		Header header = new Header();
		header.setName("테스트해더#1");

		headerRepo.save(header);
	}

	@Test
	public void _1_등록_조회() throws Throwable {
		// given
		Transaction tx = new Transaction(12, "KNTAXONL");
		tx.setTestNo(13);
		tx.setTestCode("KNTAXOTS");
		tx.setHeader(headerRepo.findAll().get(0));
		tx.setDesc("설명이다!");
		tx.setJob(codeRepo.getCode("JOBS", "J01"));
		tx.setType(Type.ONLINE);

		tx.addForm(new Form(tx, "1000", "210"));
		tx.addForm(new Form(tx, "1015", "210"));
		
		// when
		repo.saveAndFlush(tx);
		
		// then
		tx = repo.findAll().get(0);

		assertEquals(tx.getCode(), "KNTAXONL");
		assertEquals(tx.getHeader().getName(), "테스트해더#1");
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tx));
	}
}
