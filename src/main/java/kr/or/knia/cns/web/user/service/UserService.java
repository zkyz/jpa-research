package kr.or.knia.cns.web.user.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.or.knia.cns.domain.User;

public interface UserService {
	Page<User> getUsers(Pageable page, String corpCode, String name, Set<String> job);
	User save(User user);
	User delete(Integer no);

	User login(User user);
}
