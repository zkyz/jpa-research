package kr.or.knia.config.spring.argument;

//public class UserArgumentResolver implements HandlerMethodArgumentResolver {
//	public boolean supportsParameter(MethodParameter parameter) {
//		return User.class.isAssignableFrom(parameter.getParameterType());
//	}
//
//	@Override
//	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//
//		User user = (User)webRequest.getAttribute(User.SESS_NAME, RequestAttributes.SCOPE_SESSION);
//
//		// TODO remove this context
//		/*if(user == null) {
//			user = new User("Admin", "DOMINATOR");
//		}*/
//
//		return user;
//	}
//
//}
