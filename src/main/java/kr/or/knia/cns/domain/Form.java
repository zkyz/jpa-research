package kr.or.knia.cns.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "FORM")
@SequenceGenerator(name = "FORM-sequence", sequenceName = "SQ_FORM_NO", allocationSize = 1)
@Data
public class Form {

	@Id
	@GeneratedValue(generator = "FORM-sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "FORM_NO")
	private Integer no;

	@Column(name = "FORM_CD")
	@NotNull
	private String formCode;

	@Column(name = "WORK_CD")
	@NotNull
	private String workCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRX_NO")
	@JsonBackReference
	private Transaction transaction;

	@Column(name = "DSC")
	private String desc;

	@OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
	private List<Attribute> attributes;

	private Integer dataOffset;
	private Integer repeatLength;

	public Form() {}
	public Form(String typeCode, String workCode) {
		this.formCode = typeCode;
		this.workCode = workCode;
	}
	public Form(Transaction transaction, String typeCode, String workCode) {
		this(typeCode, workCode);
		this.transaction = transaction;
	}

	public void addAttribute(Attribute attr) {
		if (attributes == null) {
			attributes = new ArrayList<Attribute>(1);
		}

		attributes.add(attr);
	}
}
