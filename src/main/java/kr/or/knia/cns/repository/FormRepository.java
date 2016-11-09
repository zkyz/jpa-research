package kr.or.knia.cns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.knia.cns.domain.Form;

public interface FormRepository extends JpaRepository<Form, Integer> {
	List<Form> findAllByTransactionCodeOrderByNoAsc(String code);
	List<Form> findAllByTransactionTestCode(String testCode);
}
