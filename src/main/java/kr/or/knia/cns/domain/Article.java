package kr.or.knia.cns.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Entity
@Table(name = "BBS")
@SequenceGenerator(name = "BBS-sequence", sequenceName = "SQ_BBS_NO", allocationSize = 1)
@Data
public class Article {

	@Id
	@GeneratedValue(generator = "BBS-sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "BBS_NO")
	private Integer no;

	@Enumerated(EnumType.STRING)
	private Group group;

	@OneToOne
	@JoinColumn(name = "USR_NO")
	private User user;

	@ManyToOne
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "JOB", referencedColumnName = "CD")),
		@JoinColumnOrFormula(formula = @JoinFormula(value = "'JOBS'", referencedColumnName = "PCD"))
	})
	private Code job;

	@NotEmpty
	private String title;

	private String text;
	private int read;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable = false)
	private Date updated;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "BBS_NO")
	private List<ArticleFile> files;

	public Article() {}
	public Article(User user, String title) {
		this.user = user;
		this.title = title;
	}

	
	public static enum Group {
		NOTICE,
		DESIGN,
		TALKATIVE
	}
}
