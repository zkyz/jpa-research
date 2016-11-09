package kr.or.knia.cns.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "BBS_FILE")
@SequenceGenerator(name = "BBS_FILE-sequence", sequenceName = "SQ_BBS_FILE_NO", allocationSize = 1)
@Data
public class ArticleFile {

	@Id
	@GeneratedValue(generator = "BBS_FILE-sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "BBS_FILE_NO")
	private Integer no;

	@Column(name = "BBS_NO")
	private Integer articleNo;

	@JsonIgnore
	private String path;

	private String uuid;
	private String name;
	private String mime;
	private Long length;

	@Transient
	private MultipartFile file;
}
