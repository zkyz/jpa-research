package kr.or.knia.config.spring.converter;

import org.springframework.core.convert.converter.Converter;

public class ToFloatConverter implements Converter<String, Float> {
	@Override
	public Float convert(String source) {
		if(source != null) {
			source = source.trim();

			boolean minus = source.startsWith("-");

			source = source.replaceAll("[^0-9.]", "");
			if("".equals(source)) {
				return null;
			}
			else {
				return Float.parseFloat((minus ? "-" : "") + source);
			}
		}

		return null;
	}
}
