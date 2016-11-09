package kr.or.knia.config.spring.formatter.bool;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.util.StringValueResolver;

public class TrueFormatAnnotationFormatterFactory implements 
				AnnotationFormatterFactory<TrueFormat>,
				EmbeddedValueResolverAware {

	public void setEmbeddedValueResolver(StringValueResolver resolver) {
	}

	public Set<Class<?>> getFieldTypes() {
		Set<Class<?>> types = new HashSet<Class<?>>();
		types.add(Boolean.class);
		return types;
	}

	public Printer<?> getPrinter(TrueFormat annotation, Class<?> fieldType) {
		return getFormatter(annotation);
	}

	public Parser<?> getParser(TrueFormat annotation, Class<?> fieldType) {
		return getFormatter(annotation);
	}

	public Formatter<Boolean> getFormatter(TrueFormat annotation) {
		TrueFormatter formatter = new TrueFormatter();
		formatter.setValue(annotation.value());
		return formatter;
	}

}
