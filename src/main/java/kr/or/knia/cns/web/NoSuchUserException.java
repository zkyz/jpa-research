package kr.or.knia.cns.web;

@SuppressWarnings("serial")
public class NoSuchUserException extends InvalidDataException {
	public NoSuchUserException() {
		super("id", "login.deny.anonymous");
	}
}
