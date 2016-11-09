package kr.or.knia.cns.web.bbs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.or.knia.cns.domain.Article;
import kr.or.knia.cns.domain.Article.Group;
import kr.or.knia.cns.domain.ArticleFile;
import kr.or.knia.cns.domain.User;

public interface BbsService {
	Page<Article> getList(Pageable page, User user, Group group, String corpCode, String jobCode);

	Article save(Article article);
	Article delete(Integer no);

	ArticleFile deleteFile(ArticleFile file);
}
