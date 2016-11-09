package kr.or.knia.config.spring.advice;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE) 
public class DefaultExceptionAdvice {
	private static final String defaultErrorHtml = "<!doctype HTML><html><head><title>오류</title><script>alert('시스템 오류가 발생하였습니다. 관리자에게 문의 해주시거나, 잠시 후 다시 시도해 주시기 바랍니다.')</script></head></html>";
	
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler({
		Exception.class
	})
	public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Throwable {
		ex.printStackTrace();

		if("XmlHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
			|| (request.getHeader("Accept") != null
				&& request.getHeader("Accept").contains(MediaType.APPLICATION_JSON_VALUE))
			|| (request.getHeader("Content-Type") != null
				&& request.getHeader("Content-Type").contains(MediaType.APPLICATION_JSON_VALUE))) {

			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.addHeader("Access-Control-Allow-Origin", "*");

			String message = null;

			try {
				message = messageSource.getMessage(ex.getMessage(), null, null);
			}
			catch(NoSuchMessageException e) {
				message = messageSource.getMessage("UNKNOWN", null, null);
			}

			Map<String, Object> exInfo = new HashMap<String, Object>(3);
			exInfo.put("exceptionClass", ex.getClass());
			exInfo.put("exceptionMessage", ex.getMessage());
			exInfo.put("message", message);

			OutputStream os = response.getOutputStream();
			os.write(objectMapper.writeValueAsString(exInfo).getBytes());
			os.flush();
			os.close();

			return;
		}
		
		response.setContentType("text/html; charset=utf-8");

		PrintWriter html = new PrintWriter(new OutputStreamWriter(response.getOutputStream()));
		html.write(defaultErrorHtml.toString());
		html.write("<!--");
		ex.printStackTrace(html);
		html.write(" --> ");
		html.close();
	}
}
