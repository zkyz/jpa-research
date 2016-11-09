package kr.or.knia.config.spring.advice;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.knia.cns.web.InvalidDataException;

@ControllerAdvice
@Order(1)
public class InvalidDataExceptionAdvice {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler({
		InvalidDataException.class
	})
	public void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Throwable {
		InvalidDataException exception = (InvalidDataException)ex;

		if("XmlHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))
			|| (request.getHeader("Accept") != null
				&& request.getHeader("Accept").contains(MediaType.APPLICATION_JSON_VALUE))
			|| (request.getHeader("Content-Type") != null
					&& request.getHeader("Content-Type").contains(MediaType.APPLICATION_JSON_VALUE))){

			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.addHeader("Access-Control-Allow-Origin", "*");

			try {
				OutputStream os = response.getOutputStream();
				os.write(objectMapper.writeValueAsString(jsonErrorMessage(exception.getErrors())).getBytes());
				os.flush();
				os.close();
			}
			catch(Throwable t) {
				throw t;
			}
		}
		else {
			throw ex;
		}
	}

	private Map<String, Object> jsonErrorMessage(List<FieldError> errors) {
		errors.sort(new Comparator<FieldError>() {
			@Override
			public int compare(FieldError o1, FieldError o2) {
				return o1.getField() == null || o2.getField() == null ? -1 : (o1.getField().compareTo(o2.getField()));
			}
		});

		Field field = null;

		try {
			field = DefaultMessageSourceResolvable.class.getDeclaredField("defaultMessage");
			field.setAccessible(true);
			
			for(FieldError err : errors) {
				field.set(err, findMessage(err.getDefaultMessage(), err.getCodes(), err.getArguments()));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> errorMap = new HashMap<String, Object>(1);
		errorMap.put("error", errors);
		return errorMap;
	}

	private String findMessage(String defaultMessage, String[] codes, Object[] args) {
		if (defaultMessage != null && defaultMessage.matches("^\\{(.+?)\\}$")) {
			return messageSource.getMessage(defaultMessage.replaceAll("^\\{|\\}$", ""), args, null);
		}

		for (String code : codes) {
			if(code.startsWith("Size") && args[1] != null && args[1].equals(Integer.MAX_VALUE)) {
				args[1] = "* (unlimited)";
			}

			try {
				return messageSource.getMessage(code, args, null);
			}
			catch(NoSuchMessageException e) {
			}
		}

		return  defaultMessage;
	}
}
