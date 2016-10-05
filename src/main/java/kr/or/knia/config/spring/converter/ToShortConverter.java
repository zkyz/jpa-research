package kr.or.knia.config.spring.converter;

import org.springframework.core.convert.converter.Converter;

public class ToShortConverter implements Converter<String, Short> {
	@Override
	public Short convert(String source) {
		if(source != null) {
			source = source.trim();

			boolean minus = source.startsWith("-");

			source = source.replaceAll("[^0-9]", "");
			if("".equals(source)) {
				return null;
			}
			else {
				return Short.parseShort((minus ? "-" : "") + source);
			}
		}

		return null;
	}
}
