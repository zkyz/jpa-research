package kr.or.knia.config.spring.converter;

import org.springframework.core.convert.converter.Converter;

public class ToDoubleConverter implements Converter<String, Double> {

	@Override
	public Double convert(String source) {
		if(source != null) {
			source = source.trim();

			boolean minus = source.startsWith("-");

			source = source.replaceAll("[^0-9.]", "");
			if("".equals(source)) {
				return null;
			}
			else {
				return Double.parseDouble((minus ? "-" : "") + source);
			}
		}

		return null;
	}
}
