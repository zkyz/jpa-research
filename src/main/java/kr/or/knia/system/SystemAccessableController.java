package kr.or.knia.system;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemAccessableController {

	@Value("${dominator:knia:knia@8500}")
	private String dominator;

	@PostConstruct
	public void setup() {
		dominator = Base64Utils.encodeToString(dominator.getBytes());
	}

	@RequestMapping("/system")
	public void dash(HttpSession session, HttpServletResponse response,
			@RequestHeader(name = "Authorization", required = false) String authorization) throws IOException {

		if(authorization != null) {
			if(authorization.equals("Basic " + dominator)) {
				session.setAttribute("DOMINATOR", "I have ready for you, sir!");
				response.sendRedirect("/system/info#Hello,dominator!");
				return;
			}
		}

		response.addHeader("WWW-Authenticate", "Basic realm=\"Application Dominator\"");
		response.sendError(401, "Who r u ?");
	}
}
