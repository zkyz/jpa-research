package kr.or.knia.et.system.login;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.knia.et.supervision.user.User;
import kr.or.knia.et.supervision.user.UserRepository;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private UserRepository user;
	
	@PostMapping
	public String login(HttpSession session,
			@RequestParam String id, @RequestParam String password) throws Exception {

		User selectedUser = user.findById(id);
		
		if(selectedUser == null || !password.equals(selectedUser.getPin())) {
			throw new IllegalAccessException();
		}

		session.setAttribute(User.SESS_NAME, user);
		return "ok";
	}

	@RequestMapping(value = "/out", consumes = "javascript/json")
	public String logout(HttpSession session) {
		session.invalidate();
		return "ok";
	}

	@RequestMapping(value = "/out", consumes = {"text/html", "text/plain"})
	public void logout(HttpSession session, HttpServletResponse response) throws Exception {
		logout(session);
		response.sendRedirect("/login");
	}
}
