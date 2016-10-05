package kr.or.knia.config.spring.formatter.bool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrueFormat {
	String value() default "Y|y|true|True|TRUE|T|on|ON|Yes|yes|YES";
}