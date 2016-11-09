package kr.or.knia.cns.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "HDR_DTL")
@SequenceGenerator(name = "HDR_DTL-sequence", sequenceName = "SQ_HDR_DTL_NO", allocationSize = 1)
@Data
public class HeaderDetail {

	@Id
	@GeneratedValue(generator = "HDR_DTL-sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "HDR_DTL_NO")
	private Integer no;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HDR_NO")
	@JsonBackReference
	private Header header;

	private String name;

	@Enumerated(EnumType.STRING)
	private Type type;

	private Integer offset;
	private Integer length;

	@Column(name = "ORD")
	private Integer order;

	public HeaderDetail() {}
	public HeaderDetail(Header header, String name) {
		this.header = header;
		this.name = name;
	}
	public HeaderDetail(Header header, String name, Type type) {
		this(header, name);
		this.type = type;
	}

	public static enum Type {
		NUMBER,
		STRING,
		NUMBER_STRING
	}

	@Override
	public String toString() {
		return "HeaderDetail [no=" + no + ", name=" + name + ", type=" + type + ", offset=" + offset + ", length="
				+ length + ", order=" + order + "]";
	}
}
