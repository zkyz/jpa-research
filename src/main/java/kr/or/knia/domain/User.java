package kr.or.knia.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("User")
public class User implements Serializable {
	private static final long serialVersionUID = 113206181704049774L;
	
	public static final String SESS_NAME = User.class.getCanonicalName();
	
	public User() {}
	public User(String id) {
		this.id = id;
	}
	public User(String id, String name) {
		this(id);
		this.name = name;
	}
	
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", dept=" + dept + "]";
	}

	private String id;
	private String pin;
	private String name;
	private String email;
	private String mobile;
	private short age;
	private String group;
	private String groupName;
	private Dept dept;
	private Team team;
	private String grade;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public short getAge() {
		return age;
	}
	public void setAge(short age) {
		this.age = age;
	}
	public Dept getDept() {
		return dept;
	}
	public void setDept(Dept dept) {
		this.dept = dept;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
}
