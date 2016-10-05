package kr.or.knia.et.supervision.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<Group, String> {
	@Query("select max(a.order)+1 from Group a")
	Integer getCurrentMaxOrder();
}
