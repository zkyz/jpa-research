package kr.or.knia.system;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system/info")
public class ServerInfoController {

	@RequestMapping
	public void setLogLevel(HttpServletRequest request, HttpServletResponse response) throws Exception {

		StringBuilder info = new StringBuilder();
		
		info.append("<strong> Local IP (request.getLocalAddr())</strong><br>")
			.append(request.getLocalAddr())
			.append("<hr>");
		info.append("<strong> Local Name (request.getLocalName(), request.getLocalPort())</strong><br>")
			.append(request.getLocalName()).append(":").append(request.getLocalPort())
			.append("<hr>");
		info.append("<strong> Server Name (request.getServerName(), request.getServerPort())</strong><br>")
			.append(request.getServerName()).append(":").append(request.getServerPort())
			.append("<hr>");
		info.append("<strong> Remote IP (request.getRemoteAddr())</strong><br>")
			.append(request.getRemoteAddr())
			.append("<hr>");
		info.append("<strong> Remote Host (request.getRemoteHost(), request.getRemotePort())</strong><br>")
			.append(request.getRemoteHost()).append(":").append(request.getRemotePort())
			.append("<hr>");
		info.append("<strong> Remote User (request.getRemoteUser())</strong><br>")
			.append(request.getRemoteUser())
			.append("<hr>");
		info.append("<strong> Character Encoding (request.getCharacterEncoding())</strong><br>")
			.append(request.getCharacterEncoding())
			.append("<hr>");
		info.append("<strong> Context Path (request.getContextPath())</strong><br>")
			.append("".equals(request.getContextPath()) ? "(empty)" : request.getContextPath())
			.append("<hr>");
		
		info.append("<fieldset>")
			.append("<legend>System Properties</legend>")
			.append("<table>");

		for(Object key : System.getProperties().keySet()) {
			info.append("<tr><th>")
				.append(key)
				.append("</th><td>")
				.append(System.getProperty((String)key))
				.append("</td></tr>");
		}

		info.append("</table>")
			.append("</fieldset>");
		
		response.setContentType("text/html; charset=utf-8");
		
	    OutputStream os = response.getOutputStream();
	    os.write(new String(info.toString().getBytes()).getBytes());
	    os.flush();
	    os.close();
	}
}
