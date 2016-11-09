package kr.or.knia.cns.web.bbs;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtworks.selenium.webdriven.commands.AttachFile;

import kr.or.knia.cns.domain.Article;
import kr.or.knia.cns.domain.Article.Group;
import kr.or.knia.cns.domain.ArticleFile;
import kr.or.knia.cns.domain.User;
import kr.or.knia.cns.web.InvalidDataException;
import kr.or.knia.cns.web.bbs.service.BbsService;

@RestController
@RequestMapping("/bbs")
public class BbsController {
	
	@Value("${application.file.storage}")
	private String fileStorage;
	
	@Autowired
	private BbsService service;

	@Autowired
	private HttpSession session;
	
	@PostMapping("/{group}")
	public Page<Article> getList(
			User user,
			Pageable page, Group group, String corpCode, String jobCode) {
		
		return service.getList(page, user, group, corpCode, jobCode);
	}

	@PutMapping("/{group}")
	public Article save(@PathVariable String group,
			@Valid @RequestBody Article article, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}

		article.setGroup(Article.Group.valueOf(group.toUpperCase()));
		addFileToArticle(article);

		article = service.save(article);

		clearFileAtSession();
		return article;
	}

	@PostMapping("/file")
	public List<ArticleFile> saveFile(List<ArticleFile> files) throws Throwable {
		for (ArticleFile part : files) {
			if (!part.getFile().isEmpty()) {
				String uid = Long.toString(UUID.randomUUID().getMostSignificantBits(), 16);
				String path = fileStorage + "/" + uid;
	
				FileCopyUtils.copy(part.getFile().getInputStream(),
						new FileOutputStream(path));
				
				part.setUuid(uid);
				part.setPath(path);
				part.setName(part.getFile().getOriginalFilename());
				part.setMime(part.getFile().getContentType());
				part.setLength(part.getFile().getSize());

				putFileAtSession(part);
			}
		}

		return files;
	}
	
	@DeleteMapping("/file")
	public ArticleFile deleteFile(ArticleFile file) {
		return service.deleteFile(file);
	}
	
	private void clearFileAtSession() {
		session.removeAttribute(AttachFile.class.getName());
	}
	private void addFileToArticle(Article article) {
		if (session.getAttribute(ArticleFile.class.getName()) != null) {
			@SuppressWarnings("unchecked")
			List<ArticleFile> files = (List<ArticleFile>)session.getAttribute(AttachFile.class.getName());
			article.setFiles(files);
		}
	}
	private void putFileAtSession(ArticleFile articleFile) {
		@SuppressWarnings("unchecked")
		List<ArticleFile> files = (List<ArticleFile>)session.getAttribute(AttachFile.class.getName());

		if (files == null) {
			files = new ArrayList<ArticleFile>(1);
			session.setAttribute(AttachFile.class.getName(), files);
		}

		files.add(articleFile);
	}
}
