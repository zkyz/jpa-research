package kr.or.knia.config.spring.converter;

import org.springframework.core.convert.converter.Converter;

public class ToLongConverter implements Converter<String, Long> {
	@Override
	public Long convert(String source) {
		if(source != null) {
			source = source.trim();

			boolean minus = source.startsWith("-");

			source = source.replaceAll("[^0-9]", "");
			if("".equals(source)) {
				return null;
			}
			else {
				return Long.parseLong((minus ? "-" : "") + source);
			}
		}

		return null;
	}
}
