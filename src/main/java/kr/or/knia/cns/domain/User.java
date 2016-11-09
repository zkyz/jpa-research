package kr.or.knia.cns.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Entity
@Table(name = "USR")
@SequenceGenerator(name="User-sequence", sequenceName = "SQ_USR_NO", allocationSize = 1)
@Data
public class User {
	public static final String[] DENIED_PIN_WORDS = new String[] { "asd", "qwe", "zxc", "love" };
	public static final Pattern ALPHABETS = Pattern.compile("[a-zA-Z]+");
	public static final Pattern NUMBERS = Pattern.compile("[0-9]+");
	public static final Pattern SPECIALS = Pattern.compile("[~!@#$%^&*()_+`\\-=\\[\\]\\|{}\\\\;':\",\\.\\/<>?]+");
	public static final Pattern SAME_CHAR = Pattern.compile("([\\w|~!@#$%^&*()_+`\\-=\\[\\]\\|{}\\\\;':\",\\.\\/<>?])\\1\\1\\1");

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User-sequence")
	@Column(name = "USR_NO")
	private Integer no;

	@Column(unique = true)
	@NotNull
	private String id;

	@Size(min = 8)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String pin;

	@NotNull
	private String name;

	@OneToOne
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name = "COP_CD", referencedColumnName = "CD")),
		@JoinColumnOrFormula(formula = @JoinFormula(value = "'CORPS'", referencedColumnName = "PCD"))
	})
	@NotNull
	private Code corporation;

	@Column(insertable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private EnableSystem enableSystem;

	@Column(insertable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@Column(insertable = false)
	@Enumerated(EnumType.STRING)
	private UserType type;

	private int pinErrorCount;

	@Column(insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar pinChanged;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar accessed;

	@Column(insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar requested;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar approved;

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "USR_JOB",
			joinColumns = @JoinColumn(name = "USR_NO"),
			inverseJoinColumns = {
				@JoinColumn(name = "JOB_CD", referencedColumnName = "CD"),
				@JoinColumn(name = "JOB_PCD", referencedColumnName = "PCD")
			})
	@NotNull
	private List<Code> jobs;

	public User() {}
	public User(String id, String pin) {
		this.id = id;
		this.pin = pin;
	}
	public User(String id, String pin, String name) {
		this(id, pin);
		this.name = name;
	}
	public User(String id, String pin, String name, Code corporation) {
		this(id, pin, name);
		this.corporation = corporation;
	}

	@Override
	public String toString() {
		return "User [no=" + no + ", id=" + id + ", name=" + name + ", status=" + status
				+ "]";
	}

	public static enum UserType {
		SYSOP,
		MANAGER,
		USER,
		GUEST
	}
	
	public static enum UserStatus {
		APPROVE,
		REQUEST,
		SLEEP,
		DENY
	}
	
	public static enum EnableSystem {
		ALL(9),
		OPERATION(2),
		TEST(1),
		NONE(0);
		
		int level;

		EnableSystem(int level) {
			this.level = level;
		}
		
		public final int getLevel() {
			return level;
		}
		
		public static final EnableSystem valueOf(int level) {
			switch(level) {
			case 9: return ALL;
			case 2: return OPERATION;
			case 1: return TEST;
			default: return NONE;
			}
		}
	}
	
	@AssertFalse @JsonIgnore
	public boolean isPinHasNotAlphabet() {
		return pin != null && !ALPHABETS.matcher(pin).find();
	}
	@AssertFalse @JsonIgnore
	public boolean isPinHasNotNumber() {
		return pin != null && !NUMBERS.matcher(pin).find();
	}
	@AssertFalse @JsonIgnore
	public boolean isPinHasNotSpecialCharacter() {
		return pin != null && !SPECIALS.matcher(pin).find();
	}
	@AssertFalse @JsonIgnore
	public boolean isPinRepeatSameCharacters() {
		return pin != null && SAME_CHAR.matcher(pin).find();
	}
	@AssertFalse @JsonIgnore
	public boolean isPinContainsId() {
		return pin != null && pin.contains(id);
	}
	@AssertFalse @JsonIgnore
	public boolean isPinSequencialCharacters() {
		if (pin == null) return false;
		
		int len = pin.length();
		for (int i = 0; i <= len - 4; i++) {
			char c = pin.charAt(i);
			String src  = new String(new char[] { (char)(c + 1), (char)(c + 2), (char)(c + 3) });
			String dest = new String(new char[] { pin.charAt(i + 1), pin.charAt(i + 2), pin.charAt(i + 3) });

			if(src.equals(dest)) {
				return true;
			}
		}

		return false;
	}
	@AssertFalse @JsonIgnore
	public boolean isPinContainsWeakWords() {
		if (pin == null) return false;

		for (String word : DENIED_PIN_WORDS) {
			if (pin.contains(word)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addJob(Code job) {
		if (jobs == null) {
			jobs = new ArrayList<Code>(1);
		}
		
		jobs.add(job);
	}

	public void dirtyCheck(User target) {
		if (getNo() != null) {
			
			if (getEnableSystem() != target.getEnableSystem()) {
				setStatus(UserStatus.REQUEST);
			}
			else if (getJobs().size() != target.getJobs().size()) {
				setStatus(UserStatus.REQUEST);
			}
			else {
				for (Code job : getJobs()) {
					if (!target.getJobs().contains(job)) {
						setStatus(UserStatus.REQUEST);
						setAccessed(null);
						return;
					}
				}	
			}
		}
	}
}
