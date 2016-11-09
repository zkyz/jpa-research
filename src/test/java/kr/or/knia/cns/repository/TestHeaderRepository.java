package kr.or.knia.cns.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import kr.or.knia.cns.domain.Header;
import kr.or.knia.cns.domain.HeaderDetail;
import kr.or.knia.cns.test.TestRepoEnv;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHeaderRepository extends TestRepoEnv {

	@Autowired
	private HeaderRepository repo;
	
	@Autowired
	private HeaderDetailRepository detailRepo;
	
	@Before
	public void setup() {
		assertNotNull(repo);
		assertNotNull(detailRepo);

		Header h = new Header("해더#1(온라인전용)");

		h.addDetail(new HeaderDetail(h, "거래코드"));
		h.addDetail(new HeaderDetail(h, "개시문자"));
		h.addDetail(new HeaderDetail(h, "전문종별코드"));
		h.addDetail(new HeaderDetail(h, "업무구분코드"));
		h.addDetail(new HeaderDetail(h, "전문구분코드"));
		repo.save(h);
	}

	@Test
	public void _1_공통정보부_불러오기() {
		List<Header> h = repo.findAll();

		assertNotNull(h);
		assertSame(h.size(), 1);

		assertNotNull(h.get(0).getDetails());
		assertSame(h.get(0).getDetails().size(), 5);
		

		for(Header header : h) {
			System.out.println(header);
		}
	}
}
