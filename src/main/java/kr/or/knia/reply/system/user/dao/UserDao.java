package kr.or.knia.reply.system.user.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.or.knia.domain.User;
import kr.or.knia.et.supervision.ListInquiry;

@Repository
public interface UserDao {
	User getUserById(String id);
	
	List<User> getUser(ListInquiry inquiry);
	int save(User user);
	int remove(User user);

	List<Map<String, String>> getCorps();
}
