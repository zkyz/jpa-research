package kr.or.knia.et.supervision.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.or.knia.et.supervision.ListInquiry;
import kr.or.knia.et.supervision.user.service.UserService;

@RestController
@RequestMapping("/supervision/user")
public class UserController {

	@Autowired
	private UserService service;

	@RequestMapping(method = RequestMethod.POST)
	public List<User> getUser(ListInquiry inquiry) {
		return service.getUser(inquiry);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public int save(@RequestBody User user) {
		return service.save(user);
	}

	@RequestMapping("/groups")
	public List<Group> getGroups() {
		return service.getGroups();
	}
}
