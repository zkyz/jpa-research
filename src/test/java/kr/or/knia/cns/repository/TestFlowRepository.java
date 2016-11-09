package kr.or.knia.cns.repository;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import kr.or.knia.cns.domain.Attribute;
import kr.or.knia.cns.domain.Form;
import kr.or.knia.cns.domain.Transaction;
import kr.or.knia.cns.test.TestRepoEnv;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestFlowRepository extends TestRepoEnv {

	@Autowired
	private FlowRepository repo;

	@Autowired
	private TransactionRepository txRepo;
	
	@Autowired
	private FormRepository typeRepo;

	@Before
	private void setup() {
		assertNotNull(repo);
		assertNotNull(txRepo);
		assertNotNull(typeRepo);

		Form form = new Form("1000", "210");
		form.addAttribute(new Attribute("거래구분", form));
		form.addAttribute(new Attribute("아브라카타브라", form));
		form.addAttribute(new Attribute("얄라리얄라셩", form));

		Transaction tx = new Transaction(12, "KNTAXONL");
		tx.addForm(form);

	}
}
