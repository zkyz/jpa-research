package kr.or.knia.cns.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserWhetherValidOrNot {

	private User user;
	
	@Before
	public void setup() {
		user = new User();
		user.setId("test");
	}
	
	@Test
	public void _1_비밀번호_자릿수_모자랄때() {
		// @Size 로 대체
	}
	@Test
	public void _2_비밀번호_숫자_누락() {
		user.setPin("abcd!@#$");
		assertTrue(user.isPinHasNotNumber());
	}
	@Test
	public void _3_비밀번호_특수문자_누락() {
		user.setPin("kkk1111");
		assertTrue(user.isPinHasNotSpecialCharacter());
	}
	@Test
	public void _4_비밀번호_영문자_누락() {
		user.setPin("1231!@#!");
		assertTrue(user.isPinHasNotAlphabet());
	}
	@Test
	public void _5_비밀번호_동일한문자_반복() /* 4번 */ { 
		user.setPin("aaaa");
		assertTrue(user.isPinRepeatSameCharacters());
		user.setPin("9999");
		assertTrue(user.isPinRepeatSameCharacters());
		user.setPin("!!!!");
		assertTrue(user.isPinRepeatSameCharacters());
	}
	@Test
	public void _6_비밀번호_순차적인문자() /* 3개 */ {
		user.setPin("1234");
		assertTrue(user.isPinSequencialCharacters());

		user.setPin("abcd");
		assertTrue(user.isPinSequencialCharacters());
	}
	@Test
	public void _7_비밀번호에_아이디_포함() /* 3개 */ {
		user.setPin("test112~@$^");
		assertTrue(user.isPinContainsId());
	}
	@Test
	public void _8_비밀번호_자주쓰는_단어포함() /* 3개 */ {
		user.setPin("112love~@$^");
		assertTrue(user.isPinContainsWeakWords());
	}
}
