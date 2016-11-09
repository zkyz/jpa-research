package kr.or.knia.cns.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Table(name = "TRX")
@Data
public class Transaction {
	@Id
	@Column(name = "TRX_NO")
	@NotNull
	private Integer no;

	@Column(name = "TRX_CD")
	@NotNull
	private String code;

	@Column(name = "TEST_NO")
	@NotNull
	private Integer testNo;
	
	@Column(name = "TEST_CD")
	@NotNull
	private String testCode;

	@ManyToOne
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "JOB", referencedColumnName = "CD")),
		@JoinColumnOrFormula(formula = @JoinFormula(value = "'JOBS'", referencedColumnName = "PCD"))
	})
	@NotNull
	private Code job;

	@Enumerated(EnumType.STRING)
	@NotNull
	private Type type;

	@OneToOne
	@JoinColumn(name = "HDR_NO")
	@NotNull
	private Header header;

	@Column(name = "DSC")
	private String desc;

	@OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
	@OrderBy("formCode DESC")
	@JsonManagedReference
	private List<Form> forms;

	public Transaction() {}
	public Transaction(Integer no, String code) {
		this.no = no;
		this.code = code;
	}

	public static enum Type {
		ONLINE,
		FILE
	}

	public void addForm(Form form) {
		if (forms == null) {
			forms = new ArrayList<Form>(1);
		}
		
		forms.add(form);
	}
}
