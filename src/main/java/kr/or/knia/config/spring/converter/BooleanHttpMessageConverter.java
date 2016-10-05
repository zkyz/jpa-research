package kr.or.knia.config.spring.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class BooleanHttpMessageConverter extends AbstractHttpMessageConverter<Boolean> {

	public BooleanHttpMessageConverter() {
		super(new MediaType("text", "plain"), MediaType.ALL);
	}
	
	protected boolean supports(Class<?> clazz) {
		return Boolean.class.isAssignableFrom(clazz) || boolean.class == clazz;
	}

	protected Boolean readInternal(Class<? extends Boolean> clazz,
			HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		StringBuilder input = new StringBuilder();
		String line = null;

		BufferedReader br = new BufferedReader(new InputStreamReader(inputMessage.getBody()));
		while((line = br.readLine()) != null) {
			input.append(line);
		}

		return input.toString().toUpperCase().matches("Y|T|TRUE|YES|ON");
	}

	protected void writeInternal(Boolean t,
			HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		outputMessage.getBody().write(t.toString().getBytes());
	}
}