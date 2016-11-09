package kr.or.knia.cns.web.user;

import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.knia.cns.domain.User;
import kr.or.knia.cns.web.InvalidDataException;
import kr.or.knia.cns.web.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping
	public Page<User> getUser(Pageable page,
			@RequestParam String corpCode,
			@RequestParam String name,
			@RequestParam Set<String> job) {
		return service.getUsers(page, corpCode, name, job);
	}

	@PutMapping
	public User save(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}

		return service.save(user);
	}
	
	@DeleteMapping
	public User delete(User no) {
		return service.delete(no.getNo());
	}
	
	@PostMapping("/login")
	public User login(User user) {
		if (StringUtils.isEmpty(user.getId())) {
			throw new InvalidDataException("id", "NotEmpty.user.id");
		}
		else if (StringUtils.isEmpty(user.getPin())) {
			throw new InvalidDataException("pin", "NotEmpty.user.pin");
		}
		else if (user.getEnableSystem() == null) {
			throw new InvalidDataException("enableSystem", "NotEmpty.user.enableSystem");
		}

		return service.login(user);
	}

	@PutMapping("/approve")
	public User approve(User user) {
		return service.save(user);
	}
}
