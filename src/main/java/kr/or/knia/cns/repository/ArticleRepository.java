package kr.or.knia.cns.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.or.knia.cns.domain.Article;
import kr.or.knia.cns.domain.Article.Group;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
	Page<Article> findAll(Pageable page);
	Page<Article> findAllByTitleLike(Pageable page);
	Page<Article> findAllByTextLike(Pageable page);
	Page<Article> findAllByUserNameLike(Pageable page);

	@Query(  "SELECT a.no, a.group, a.user, a.title, a.read, a.updated "
			+ " FROM Article a "
			+ "WHERE a.group = :group "
			+ "  AND a.user.corporation.key.code = COALESCE(:corpCode, a.user.corporation.key.code) "
			+ "  AND a.job.key.code = COALESCE(:jobCode, a.job.key.code) "
			)
	Page<Article> getList(
			@Param("group") Group group,
			@Param("corpCode") String corpCode,
			@Param("jobCode") String jobCode,
			Pageable page);

	void read(Article artible);
}
