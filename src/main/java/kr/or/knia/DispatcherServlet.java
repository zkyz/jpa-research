package kr.or.knia;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet {
	private static final long serialVersionUID = -6898130205984609923L;

	public DispatcherServlet() {
		super();
		this.setContextClass(AnnotationConfigWebApplicationContext.class);
	}

	public String getContextConfigLocation() {
		String config = "";
		String contextConfigLocation = super.getContextConfigLocation();

		if(contextConfigLocation != null && !"".equals(contextConfigLocation)) {
			config += ", " + contextConfigLocation;
		}

		return config;
	}
}
