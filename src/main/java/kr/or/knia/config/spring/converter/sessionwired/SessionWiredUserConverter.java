package kr.or.knia.config.spring.converter.sessionwired;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import kr.or.knia.domain.User;

@Component
public class SessionWiredUserConverter implements Converter<String, User> {

	@Autowired
	private HttpSession session;
	
	public User convert(String source) {
		Object obj = session.getAttribute(User.SESS_NAME);
		return obj == null ? null : (User)obj;
	}
}
