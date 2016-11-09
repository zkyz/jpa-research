package kr.or.knia.cns.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Entity
@Table(name = "HDR")
@SequenceGenerator(name = "HDR-sequence", sequenceName = "SQ_HDR_NO", allocationSize = 1)
@Data()
public class Header {

	@Id
	@GeneratedValue(generator = "HDR-sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "HDR_NO")
	private Integer no;

	private String name;
	
	private Type type;

	@OneToMany(mappedBy = "header", cascade = CascadeType.ALL)
	@OrderBy("no")
	@JsonManagedReference
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<HeaderDetail> details;

	public Header() {}
	public Header(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Header [name=" + name + ", type=" + type + "]";
	}

	public void addDetail(HeaderDetail detail) {
		if (details == null) {
			details = new ArrayList<HeaderDetail>(1);
		}

		details.add(detail);
	}
	
	public static enum Type {
		PUBLIC,
		STANDARD
	}
}
