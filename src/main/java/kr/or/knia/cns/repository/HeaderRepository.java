package kr.or.knia.cns.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.knia.cns.domain.Header;

public interface HeaderRepository extends JpaRepository<Header, Integer> {

}
