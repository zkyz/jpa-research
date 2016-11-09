package kr.or.knia.cns.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.knia.cns.domain.HeaderDetail;

public interface HeaderDetailRepository extends JpaRepository<HeaderDetail, Integer> {
	
}
