package kr.or.knia.cns.domain;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import kr.or.knia.config.jpa.converter.Boolean2YNConverter;
import lombok.Data;

@Entity
@Table(name = "FLOW")
@SequenceGenerator(name = "FLOW-sequence", sequenceName = "SQ_FLOW_NO", allocationSize = 1)
@Data
public class Flow {

	@Id
	@GeneratedValue(generator = "FLOW-sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "FLOW_NO")
	private Integer no;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "JOB", referencedColumnName = "CD")),
		@JoinColumnOrFormula(formula = @JoinFormula(value = "'JOBS'", referencedColumnName = "PCD"))
	})
	private Code job;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRX_NO")
	private Transaction transaction;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FORM_NO")
	private Form type;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "SND_CORP", referencedColumnName = "CD")),
		@JoinColumnOrFormula(formula = @JoinFormula(value = "'CORPS'", referencedColumnName = "PCD"))
	})
	private Code sendCorp;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "RCV_CORP", referencedColumnName = "CD")),
		@JoinColumnOrFormula(formula = @JoinFormula(value = "'CORPS'", referencedColumnName = "PCD"))
	})
	private Code receiveCorp;

	@Convert(converter = Boolean2YNConverter.class)
	private boolean repeat;

	@Convert(converter = Boolean2YNConverter.class)
	private boolean omit;

	public Flow() {}
	public Flow(Transaction transaction, Form type) {
		this.transaction = transaction;
		this.type = type;
	}
}
