package kr.or.knia.et.supervision.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.knia.et.supervision.ListInquiry;
import kr.or.knia.et.supervision.user.Group;
import kr.or.knia.et.supervision.user.GroupRepository;
import kr.or.knia.et.supervision.user.User;
import kr.or.knia.et.supervision.user.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private GroupRepository groupRepo;
	
	
	public List<User> getUser(ListInquiry inquiry) {
		List<User> list = userRepo.findAll();
		return list;
	}

	public int save(User user) {
		userRepo.save(user);
		return 1;
	}

	public int remove(User user) {
		userRepo.delete(user);
		return 1;
	}

	public List<Group> getGroups() {
		return groupRepo.findAll();
	}
}
