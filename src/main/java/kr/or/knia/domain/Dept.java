package kr.or.knia.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Dept implements Serializable {

	private String cd;
	private String name;
	
	@Override
	public String toString() {
		return "Dept [cd=" + cd + ", name=" + name + "]";
	}

	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
