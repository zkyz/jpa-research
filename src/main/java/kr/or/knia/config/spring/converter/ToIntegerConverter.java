package kr.or.knia.config.spring.converter;

import org.springframework.core.convert.converter.Converter;

public class ToIntegerConverter implements Converter<String, Integer> {
	@Override
	public Integer convert(String source) {
		if(source != null) {
			source = source.trim();

			boolean minus = source.startsWith("-");

			source = source.replaceAll("[^0-9.]", "");
			if("".equals(source)) {
				return null;
			}
			else {
				return Integer.parseInt((minus ? "-" : "") + source);
			}
		}

		return null;
	}
}
