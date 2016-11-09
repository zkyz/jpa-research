package kr.or.knia.cns.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.knia.cns.domain.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
	//List<Attribute> findAllDataType(DataType dataType);
	//List<Attribute> findAllDataTypeOrderByNoAsc(DataType dataType);
}
