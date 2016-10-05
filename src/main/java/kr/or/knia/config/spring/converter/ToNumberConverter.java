package kr.or.knia.config.spring.converter;

import java.math.BigDecimal;

import org.springframework.core.convert.converter.Converter;

public class ToNumberConverter implements Converter<String, Number> {

	@Override
	public Number convert(String source) {
		if(source != null) {
			source = source.trim();

			boolean minus = source.startsWith("-");

			source = source.replaceAll("[^0-9]", "");
			if("".equals(source)) {
				return null;
			}
			else {
				return new BigDecimal((minus ? "-" : "") + source);
			}
		}

		return null;
	}
}
