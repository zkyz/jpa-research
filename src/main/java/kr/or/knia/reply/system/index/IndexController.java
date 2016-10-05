package kr.or.knia.reply.system.index;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.knia.domain.User;
import kr.or.knia.reply.system.user.dao.UserDao;

@Controller
public class IndexController {
	
	@Autowired
	private IndexService service;
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/logout-async")
	@ResponseBody
	public String logout(HttpSession session) {
		session.invalidate();
		return "ok";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(HttpSession session,
			@RequestParam String id, @RequestParam String password) throws Exception {

		User user = userDao.getUserById(id);
		
		if(user == null || !password.equals(user.getPin())) {
			throw new IllegalAccessException();
		}

		session.setAttribute(User.SESS_NAME, user);
		return "success";
	}

	@RequestMapping(value = {"", "/index"})
	public String index(Model model, HttpSession session) {
		return "index";
	}

	@RequestMapping(value = "/help", method = RequestMethod.GET)
	@ResponseBody
	public String help(@RequestParam String id) {
		return service.getHelp(id);
	}

	@RequestMapping(value = "/help", method = RequestMethod.POST)
	@ResponseBody
	public int help(
			@RequestParam String id,
			@RequestParam String content) {
		return service.saveHelp(id, content);
	}
}
