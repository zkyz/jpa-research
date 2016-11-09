package kr.or.knia.config.spring.formatter.text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TextFormat {
	String[] remove() default "";
	int[] substring() default {-1, -1};
	String[] replace() default "";
}
