package kr.or.knia.config.spring.converter;

import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class NumbersHttpMessageConverter extends AbstractHttpMessageConverter<Number> {

	public NumbersHttpMessageConverter() {
		super(new MediaType("text", "plain"), MediaType.ALL);
	}
	
	protected boolean supports(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz);
	}

	protected Number readInternal(Class<? extends Number> clazz,
			HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		throw new UnsupportedOperationException();
	}

	protected void writeInternal(Number t,
			HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		outputMessage.getBody().write(t.toString().getBytes());
	}
}