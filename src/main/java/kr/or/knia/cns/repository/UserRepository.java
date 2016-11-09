package kr.or.knia.cns.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.or.knia.cns.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findById(String id);

	@Query("FROM User u "
			+ "WHERE corporation.key.code = COALESCE(:code, corporation.key.code) "
			+ "AND name LIKE %:name% ")
	Page<User> findAll(
			@Param("code") String corpCode,
			@Param("name") String name,
			Pageable page);

	@Query("SELECT DISTINCT u "
			+ "FROM User u LEFT JOIN u.jobs j "
			+ "WHERE u.corporation.key.code = COALESCE(:code, u.corporation.key.code) "
			+ "AND u.name LIKE %:name% "
			+ "AND j.key.code IN :job ")
	Page<User> findAll(
			@Param("code") String corpCode,
			@Param("name") String name,
			@Param("job") Set<String> job,
			Pageable page);
}
