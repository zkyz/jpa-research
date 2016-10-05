package kr.or.knia.et.supervision.user.service;

import java.util.List;
import java.util.Map;

import kr.or.knia.domain.User;
import kr.or.knia.et.supervision.ListInquiry;

public interface UserService {
	List<User> getUser(ListInquiry inquiry);
	int save(User user);
	int remove(User user);
	List<Map<String, String>> getCorps();
}
