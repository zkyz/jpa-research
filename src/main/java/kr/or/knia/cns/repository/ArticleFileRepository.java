package kr.or.knia.cns.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.knia.cns.domain.ArticleFile;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, Integer> {
}
