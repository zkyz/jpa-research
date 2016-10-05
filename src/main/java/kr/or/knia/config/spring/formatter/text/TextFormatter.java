package kr.or.knia.config.spring.formatter.text;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class TextFormatter implements Formatter<String>{
	private String[] remove;
	private int[] substring;
	private String[] replace;

	public String[] getRemove() {
		return remove;
	}
	public void setRemove(String[] remove) {
		this.remove = remove;
	}
	public int[] getSubstring() {
		return substring;
	}
	public void setSubstring(int[] substring) {
		this.substring = substring;
	}
	public String print(String object, Locale locale) {
		return object;
	}
	public String[] getReplace() {
		return replace;
	}
	public void setReplace(String[] replace) {
		this.replace = replace;
	}

	public String parse(String text, Locale locale) throws ParseException {
		
		if(remove != null && !"".equals(remove[0])) {
			for(String removeString : remove) {
				text = text.replaceAll(removeString, "");
			}
			
			return text;
		}
		if(substring[0] > -1) {
			int start = substring[0];
			int end = substring[1];
			
			if(text.length() > start && end == -1) {
				return text.substring(start);
			}
			else if(text.length() > end && start > -1) {
				return text.substring(start, end);
			}
		}
		if(replace != null && replace.length > 1) {
			return text.replaceAll(replace[0], replace[1]);
		}
		
		return text;
	}
}
