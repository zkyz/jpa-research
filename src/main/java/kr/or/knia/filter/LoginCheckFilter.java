package kr.or.knia.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.knia.domain.User;

public class LoginCheckFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		
		if(req.getSession().getAttribute(User.SESS_NAME) == null) {
			boolean bypass = false;
			String uri = req.getRequestURI();
			
			for(String prefix : new String[]{
				"/login",
				"/css",
				"/img",
				"/js"
			}) {
				if(uri.startsWith(prefix)) {
					bypass = true;
					break;
				}
			}
			
			if(!bypass) { 
				((HttpServletResponse) response).sendRedirect("/login");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
