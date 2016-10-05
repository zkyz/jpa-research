package kr.or.knia.et.supervision.user;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import kr.or.knia.config.persist.converter.Boolean2YNConverter;

@Entity
@Table(name = "GRP")
public class Group {

	@Id
	@Column(name = "GRPCD")
	private String groupCode;

	private String name;
	
	@Column(name = "ORD")
	private int order;

	@Convert(converter = Boolean2YNConverter.class)
	private Boolean enable = true;
	
	public Group() {}
	public Group(String groupCode, String name) {
		this.groupCode = groupCode;
		this.name = name;
	}
	public Group(String groupCode, String name, int order) {
		this.groupCode = groupCode;
		this.name = name;
		this.order = order;
	}

	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
