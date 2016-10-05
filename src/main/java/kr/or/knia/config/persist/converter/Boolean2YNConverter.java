package kr.or.knia.config.persist.converter;

import javax.persistence.AttributeConverter;

public class Boolean2YNConverter implements AttributeConverter<Boolean, Character> {

	@Override
	public Boolean convertToEntityAttribute(Character attribute) {
		return attribute == 'Y';
	}

	@Override
	public Character convertToDatabaseColumn(Boolean dbData) {
		return dbData == null || !dbData ? 'N' : 'Y';
	}

}
