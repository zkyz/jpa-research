package kr.or.knia.config.spring.formatter.text;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.util.StringValueResolver;

public class TextFormatAnnotationFormatterFactory implements 
				AnnotationFormatterFactory<TextFormat>,
				EmbeddedValueResolverAware {

	public void setEmbeddedValueResolver(StringValueResolver resolver) {
	}

	public Set<Class<?>> getFieldTypes() {
		Set<Class<?>> types = new HashSet<Class<?>>();
		types.add(String.class);
		return types;
	}

	public Printer<?> getPrinter(TextFormat annotation, Class<?> fieldType) {
		return new Formatter<String>() {
			public String print(String object, Locale locale) {
				return object;
			}

			public String parse(String text, Locale locale) throws ParseException {
				return text;
			}
		};
	}

	public Parser<?> getParser(TextFormat annotation, Class<?> fieldType) {
		return getFormatter(annotation);
	}

	public TextFormatter getFormatter(TextFormat annotation) {
		TextFormatter formatter = new TextFormatter();
		formatter.setRemove(annotation.remove());
		formatter.setReplace(annotation.replace());
		formatter.setSubstring(annotation.substring());
		return formatter;
	}
}
