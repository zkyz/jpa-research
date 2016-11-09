package kr.or.knia.cns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.knia.cns.domain.Flow;

public interface FlowRepository extends JpaRepository<Flow, Integer> {
	List<Flow> findAllByJobKeyCode(String code);
	List<Flow> findAllByJobKeyCodeOrderByJobKeyCodeAsc(String code);
}
