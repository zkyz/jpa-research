package kr.or.knia.config.spring.converter;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import kr.or.knia.cns.domain.User;

public class UserIdParamConvert implements Converter<String, User> {

	@Autowired
	private HttpSession session;

	@Override
	public User convert(String source) {
		Object obj = session.getAttribute(User.class.getName());

		if (obj != null) {
			return (User)obj;
		}

		return null;
	}
}
