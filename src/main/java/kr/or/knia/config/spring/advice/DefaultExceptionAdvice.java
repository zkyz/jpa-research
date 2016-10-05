package kr.or.knia.config.spring.advice;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class DefaultExceptionAdvice {
	private static final String defaultErrorHtml = "<!doctype HTML><html><head><title>오류</title><script>alert('시스템 오류가 발생하였습니다. 관리자에게 문의 해주시거나, 잠시 후 다시 시도해 주시기 바랍니다.')</script></head></html>";
	
	@Autowired
	private ObjectMapper objectMapper;

	@ExceptionHandler(Exception.class)
	public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Throwable {
		if("XmlHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
				|| (request.getContentType() != null && request.getContentType().indexOf("application/json") > -1)) {

			response.addHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("text/plain; charset=utf-8");
			response.setStatus(550);

			try {
				Map<String, Object> exInfo = new HashMap<String, Object>(2);
				exInfo.put("status", 550);
				exInfo.put("exception", ex);

				OutputStream os = response.getOutputStream();
				os.write(objectMapper.writeValueAsString(exInfo).getBytes());
				os.flush();
				os.close();
			}
			catch(Throwable t) {
				throw t;
			}

			return;
		}
		
		response.setContentType("text/html; charset=utf-8");

		PrintWriter html = new PrintWriter(new OutputStreamWriter(response.getOutputStream()));
		html.write(defaultErrorHtml .toString());
		html.write("<!--");
		ex.printStackTrace(html);
		html.write(" --> ");
		html.close();
	}
}
