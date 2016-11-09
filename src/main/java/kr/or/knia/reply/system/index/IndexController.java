package kr.or.knia.reply.system.index;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	@Autowired
	private IndexService service;
	
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
