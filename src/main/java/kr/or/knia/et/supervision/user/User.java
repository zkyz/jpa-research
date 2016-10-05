package kr.or.knia.et.supervision.user;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "USR")
@SequenceGenerator(name="User-sequence", sequenceName = "SQ_USR_UID", allocationSize = 1)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User-sequence")
	private Long no;
	
	@Column(unique = true)
	private String id;

	private String pin;
	private String name;
	
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@ManyToOne
	@JoinColumn(name = "GRPCD")
	private Group group;
	
	@Enumerated(EnumType.STRING)
	private UserType type;

	@Column(name = "ACCESS_ERROR_COUNT")
	private byte accessErrorCount;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date accessed;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date requested;

	@Temporal(TemporalType.TIMESTAMP)
	private Date approved;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

	public User() {}
	public User(String id, String pin, String name) {
		this.id = id;
		this.pin = pin;
		this.name = name;
	}
	public User(String id, String pin, String name, Group group) {
		this.id = id;
		this.pin = pin;
		this.name = name;
		this.group = group;
	}

	@Override
	public String toString() {
		return "User [no=" + no + ", id=" + id + ", name=" + name + ", status=" + status
				+ "]";
	}

	public static enum UserType {
		SYSOP,
		OPERATOR,
		USER
	}
	
	public static enum UserStatus {
		APPROVED,
		REQUESTED,
		SLEEP,
		DENY
	}
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public byte getAccessErrorCount() {
		return accessErrorCount;
	}

	public void setAccessErrorCount(byte accessErrorCount) {
		this.accessErrorCount = accessErrorCount;
	}

	public Date getAccessed() {
		return accessed;
	}

	public void setAccessed(Date accessed) {
		this.accessed = accessed;
	}

	public Date getRequested() {
		return requested;
	}

	public void setRequested(Date requested) {
		this.requested = requested;
	}

	public Date getApproved() {
		return approved;
	}

	public void setApproved(Date approved) {
		this.approved = approved;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
