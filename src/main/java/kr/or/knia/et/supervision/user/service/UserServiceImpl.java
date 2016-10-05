package kr.or.knia.et.supervision.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.knia.domain.User;
import kr.or.knia.et.supervision.ListInquiry;
import kr.or.knia.reply.system.user.dao.UserDao;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao dao;
	
	public List<User> getUser(ListInquiry inquiry) {
		return dao.getUser(inquiry);
	}

	public int save(User user) {
		return dao.save(user);
	}

	public int remove(User user) {
		return dao.remove(user);
	}

	public List<Map<String, String>> getCorps() {
		return dao.getCorps();
	}
}
