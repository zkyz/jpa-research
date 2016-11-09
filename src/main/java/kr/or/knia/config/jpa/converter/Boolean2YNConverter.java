package kr.or.knia.config.jpa.converter;

import javax.persistence.AttributeConverter;

public class Boolean2YNConverter implements AttributeConverter<Boolean, Character> {

	@Override
	public Boolean convertToEntityAttribute(Character attribute) {
		return attribute != null && attribute == 'Y';
	}

	@Override
	public Character convertToDatabaseColumn(Boolean dbData) {
		return dbData == null || !dbData ? 'N' : 'Y';
	}

}
