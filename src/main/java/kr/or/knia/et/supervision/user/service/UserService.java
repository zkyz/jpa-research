package kr.or.knia.et.supervision.user.service;

import java.util.List;

import kr.or.knia.et.supervision.ListInquiry;
import kr.or.knia.et.supervision.user.Group;
import kr.or.knia.et.supervision.user.User;

public interface UserService {
	List<User> getUser(ListInquiry inquiry);
	int save(User user);
	int remove(User user);
	List<Group> getGroups();
}
