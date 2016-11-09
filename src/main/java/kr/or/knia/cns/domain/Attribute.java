package kr.or.knia.cns.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ATTR")
@SequenceGenerator(name = "ATTR-sequence", sequenceName = "SQ_ATTR_NO", allocationSize = 1)
@Data
public class Attribute {

	@Id
	@GeneratedValue(generator = "ATTR-sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "ATTR_NO")
	private Integer no;

	private String name;

	@Enumerated(EnumType.STRING)
	private DataType dataType;
	
	private Integer offset;
	private Integer length;

	@ManyToOne
	@JoinColumn(name = "FORM_NO")
	private Form form;

	public Attribute() {}
	public Attribute(String name, Form form) {
		this.name = name;
		this.form = form;
	}

	@Override
	public String toString() {
		return "Attribute [no=" + no + ", name=" + name + ", dataType=" + dataType + ", offset=" + offset + ", length="
				+ length + "]";
	}
	
	public static enum DataType {
		MASKABLE,
		FINDABLE
	}
}
