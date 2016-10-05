package kr.or.knia.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssDefenseFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest)request) {
			@Override
			public String getParameter(String name) {
				return defenseXss(super.getParameter(name));
			}
			
			@Override
			public String[] getParameterValues(String name) {
				String[] values = super.getParameterValues(name);
				if(values != null) {
					for(String value : values) {
						value = defenseXss(value);
					}
				}

				return values;
			}
		}, 
		response);
	}

	@Override
	public void destroy() {}
	
	public static final String defenseXss(String param) {
		return param == null ? null : param.replaceAll("(?i)<(\\/?script|meta|iframe|body|html)", "〈$1");
	}

}
