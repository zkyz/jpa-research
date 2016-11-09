package kr.or.knia.cns.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import kr.or.knia.cns.domain.Form;
import kr.or.knia.cns.domain.Transaction;
import kr.or.knia.cns.test.TestRepoEnv;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFormRepository extends TestRepoEnv {

	@Autowired
	private FormRepository repo;

	@Autowired
	private TransactionRepository txRepo;

	@Before
	public void setup() throws Exception {
		assertNotNull(repo);
		assertNotNull(txRepo);

		txRepo.save(new Transaction(12, "KNTAXONL"));
		txRepo.save(new Transaction(14, "TAXCUT"));
	}

	@Test
	public void _1_등록_조회() throws Throwable {
		// given
		repo.save(new Form(txRepo.findByCode("KNTAXONL"), "1000", "200"));
		repo.save(new Form(txRepo.findByCode("TAXCUT"), "1000", "200"));
		repo.save(new Form(txRepo.findByCode("KNTAXONL"), "1015", "210"));
		repo.save(new Form(txRepo.findByCode("KNTAXONL"), "1000", "320"));

		// when
		List<Form> types = repo.findAllByTransactionCodeOrderByNoAsc("KNTAXONL");
		
		// then
		assertSame(types.size(), 3);
	}
}
