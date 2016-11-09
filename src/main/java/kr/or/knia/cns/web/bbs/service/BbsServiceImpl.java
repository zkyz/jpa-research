package kr.or.knia.cns.web.bbs.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.or.knia.cns.domain.Article;
import kr.or.knia.cns.domain.Article.Group;
import kr.or.knia.cns.domain.ArticleFile;
import kr.or.knia.cns.domain.User;
import kr.or.knia.cns.domain.User.UserType;
import kr.or.knia.cns.repository.ArticleFileRepository;
import kr.or.knia.cns.repository.ArticleRepository;

@Service
public class BbsServiceImpl implements BbsService {

	@Autowired
	private ArticleRepository repo;
	
	@Autowired
	private ArticleFileRepository fileRepo;
	
	@Value("${application.file.storage}")
	private String fileStorage;

	public Page<Article> getList(Pageable page, User user, Group group, String corpCode, String jobCode) {
		if (user.getType() == UserType.SYSOP) {
			corpCode = "";
			jobCode = "";
		}
		
		return repo.getList(group, corpCode, jobCode, page);
	}

	@Override
	public Article save(Article article) {
		return repo.save(article);
	}

	@Override
	public Article delete(Integer no) {
		return null;
	}

	@Override
	public ArticleFile deleteFile(ArticleFile file) {
		String filePath = fileStorage + "/" + file.getPath();

		File attachFile = new File(filePath);

		if (attachFile.exists() && attachFile.isFile()) {
			attachFile.delete();
		}

		fileRepo.delete(file);
		return file;
	}
}
