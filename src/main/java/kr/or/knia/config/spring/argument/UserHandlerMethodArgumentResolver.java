package kr.or.knia.config.spring.argument;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import kr.or.knia.cns.domain.User;

public class UserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	public boolean supportsParameter(MethodParameter parameter) {
		return User.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		User user = (User)webRequest.getAttribute(User.class.getName(), RequestAttributes.SCOPE_SESSION);

		// TODO remove this context
		/*if(user == null) {
			user = new User("Admin", "DOMINATOR");
		}*/

		return user;
	}

}
