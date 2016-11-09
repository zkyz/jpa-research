package kr.or.knia.cns.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import kr.or.knia.config.jpa.converter.Boolean2YNConverter;
import lombok.Data;

@Entity
@Table(name = "COD")
@Data
public class Code {

	@EmbeddedId
	@Valid
	private Key key;

	@Column(name = "VAL")
	private String value;
	
	@Column(name = "DSC")
	private String desc;

	@Column(name = "ORD")
	private Integer order;

	@Convert(converter = Boolean2YNConverter.class)
	private boolean use = true;

	public Code() {}
	public Code(String parent, String code) {
		this.key = new Key(parent, code);
	}
	public Code(String parent, String code, boolean use) {
		this(parent, code);
		this.use = use;
	}
	public Code(String parent, String code, Integer order) {
		this(parent, code);
		this.order = order;
	}
	public Code(String parent, String code, boolean use, String value) {
		this(parent, code, use);
		this.value = value;
	}
	public Code(String parent, String code, Integer order, String value) {
		this(parent, code, order);
		this.value = value;
	}
	public Code(String parent, String code, boolean use, Integer order) {
		this(parent, code, use);
		this.order = order;
	}

	@Embeddable
	@SuppressWarnings("serial")
	@Data
	public static class Key implements Serializable {

		@Column(name = "PCD")
		@NotNull
		private String parent;

		@Column(name = "CD")
		@NotNull
		private String code;

		public Key() {}
		public Key(String parent, String code) {
			this.parent = parent;
			this.code = code;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public boolean equals(Object key) {
			if (key != null && key instanceof Key) {
				Key target = (Key)key;
				
				if(target.getParent() != null
						&& target.getCode() != null
						&& target.getParent().equals(this.parent)
						&& target.getCode().equals(this.code)) {
					return true;
				}
			}

			return false;
		}
	}
}
