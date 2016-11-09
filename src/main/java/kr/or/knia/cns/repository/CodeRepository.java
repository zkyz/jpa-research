package kr.or.knia.cns.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.or.knia.cns.domain.Code;

@Repository
public interface CodeRepository extends JpaRepository<Code, Code.Key> {

	@Query("SELECT DISTINCT key.parent FROM Code WHERE use = true ORDER BY key.parent")
	List<String> findParents();

	@Query("SELECT COALESCE(MAX(c.order), 0)+1 FROM Code c WHERE key.parent = :parent")
	Integer getNextOrder(@Param("parent") String parent);

	@Query("FROM Code WHERE key.parent = :parent AND key.code = :code")
	Code getCode(@Param("parent") String parent, @Param("code") String code);

	@Query("FROM Code c WHERE key.parent = :parent AND key.code IN (:code)")
	List<Code> getCodes(@Param("parent") String parent, @Param("code") String ...code);

	@Query("SELECT c.value FROM Code c WHERE key.parent = :parent AND key.code = :code")
	String getValue(@Param("parent") String parent, @Param("code") String code);

	List<Code> findAllByKeyParent(String parent);
	List<Code> findAllByKeyParentAndUse(String parent, Boolean use);
}