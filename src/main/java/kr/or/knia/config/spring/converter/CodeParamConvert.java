package kr.or.knia.config.spring.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import kr.or.knia.cns.domain.Code;
import kr.or.knia.cns.repository.CodeRepository;

public class CodeParamConvert implements Converter<String, Code> {

	@Autowired
	private CodeRepository repo;

	@Override
	public Code convert(String source) {

		if (source.contains(":")) {
			String[] codes = source.split(":");
			return repo.getCode(codes[0], codes[1]);
		}

		return null;
	}
}
