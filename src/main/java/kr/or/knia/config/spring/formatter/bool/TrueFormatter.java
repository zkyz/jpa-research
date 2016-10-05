package kr.or.knia.config.spring.formatter.bool;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class TrueFormatter implements Formatter<Boolean>{
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	private String value;

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public Boolean parse(String text, Locale locale) throws ParseException {
		return text.matches(value);
	}

	public String print(Boolean object, Locale locale) {
		return object ? TRUE : FALSE;
	}
}
