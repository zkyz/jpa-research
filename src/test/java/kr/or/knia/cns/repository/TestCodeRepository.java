package kr.or.knia.cns.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import kr.or.knia.cns.domain.Code;
import kr.or.knia.cns.test.TestRepoEnv;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCodeRepository extends TestRepoEnv {

	@Autowired
	private CodeRepository repo;
	
	@Before
	@Rollback(false)
	public void setup() {
		assertNotNull(repo);
	}

	@Test
	public void _1_부모코드목록_불러오기() {
		// when
		List<String> parents = repo.findParents();
		List<Code> corps = repo.findAllByKeyParent("CORPS");

		// then
		for(String p : parents) {
			System.out.println(p);
		}

		for(Code corp : corps) {
			System.out.println(corp);
		}
	}
	
	@Test
	public void _2_수정하기() {
		String[] test = {
			"CORPS",
			"N00",
			"새로운회사",
		};

		// test
		Code corporation = repo.getCode(test[0], test[1]);
		corporation.setKey(new Code.Key(test[0], test[1]));
		corporation.setValue(test[2]);
		repo.saveAndFlush(corporation);

		// when
		Code someCorp = repo.getCode(test[0], test[1]);

		// then
		assertEquals(someCorp.getValue(), test[2]);
	}
	
	@Test
	public void _3_삭제하기() {
		// test
		Code association = repo.getCode("CORPS", "N00");
		repo.delete(association);

		// when
		association = repo.getCode("CORPS", "N00");

		// then
		assertNull(association);
	}

	@Test
	public void _4_여러개의_코드_불러오기() {
		// when
		List<Code> codes = repo.getCodes("CORPS", "N00", "N01", "N02");
		for(Code code : codes) {
			System.out.println(code);
		}
		
		assertSame(codes.size(), 3);
		
	}

	
	
	
	
	
	@Test
	@Rollback(false)
	public void _0_initialize() {
		if(repo.getCode("CORPS", "N00") == null) {
			repo.save(new Code("CORPS", "N00", 0, "손해보험협회"));
			repo.save(new Code("CORPS", "N01", 1, "메리츠화재해상보험"));
			repo.save(new Code("CORPS", "N02", 2, "한화손해보험"));
			repo.save(new Code("CORPS", "N03", 3, "롯데손해보험"));
			repo.save(new Code("CORPS", "N04", 4, "MG손해보험"));
			repo.save(new Code("CORPS", "N05", 5, "흥국화재해상보험"));
			repo.save(new Code("CORPS", "N06", 6, "제일화재해상보험"));
			repo.save(new Code("CORPS", "N08", 7, "삼성화재해상보험"));
			repo.save(new Code("CORPS", "N09", 8, "현대해상화재보험"));
			repo.save(new Code("CORPS", "N10", 9, "KB손해보험"));
			repo.save(new Code("CORPS", "N11", 10, "동부화재해상보험"));
			repo.save(new Code("CORPS", "N16", 11, "AXA손해보험"));
			repo.save(new Code("CORPS", "N17", 12, "더케이손해보험"));
			repo.save(new Code("CORPS", "N51", 13, "AIG손해보험"));
			repo.save(new Code("CORPS", "N66", 14, "BNP파리바카디프손해보험"));
			repo.save(new Code("CORPS", "N71", 15, "농협손해보험"));
			
			repo.save(new Code("JOBS", "J01", 1, "다중이용업소"));
			repo.save(new Code("JOBS", "J02", 2, "대리점관리"));
			repo.save(new Code("JOBS", "J03", 3, "보험가입조회"));
			repo.save(new Code("JOBS", "J04", 4, "보험다모아"));
			repo.save(new Code("JOBS", "J05", 5, "설계사관리"));
			repo.save(new Code("JOBS", "J06", 6, "세금우대"));
			repo.save(new Code("JOBS", "J07", 7, "퇴직연금"));
			repo.save(new Code("JOBS", "J08", 8, "휴면보험"));
		}
	}
}
