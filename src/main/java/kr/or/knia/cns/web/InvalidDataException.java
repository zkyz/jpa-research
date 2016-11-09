package kr.or.knia.cns.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@SuppressWarnings("serial")
public class InvalidDataException extends ValidationException {
	private List<FieldError> errors = new ArrayList<FieldError>(1);

	public InvalidDataException() {}
	public InvalidDataException(String code) {
		this("", code);
	}
	public InvalidDataException(BindingResult err) {
		errors.addAll(err.getFieldErrors());

		for (ObjectError objectErr : err.getGlobalErrors()) {
			FieldError fieldErr = new FieldError(
					objectErr.getObjectName(),
					"", 
					objectErr.getDefaultMessage(),
					false,
					objectErr.getCodes(),
					objectErr.getArguments(),
					objectErr.getDefaultMessage());
			
			errors.add(fieldErr);
		}
	}
	public InvalidDataException(FieldError err) {
		errors.add(err);
	}
	public InvalidDataException(String dataField, String code) {
		this(new FieldError("", dataField, null, false, new String[]{code}, null, "{UNKNOWN}"));
	}
	public InvalidDataException(String dataField, String code, String message) {
		this(new FieldError("", dataField, null, false, new String[]{code}, null, message));
	}

	public List<FieldError> getErrors() {
		return errors;
	}
}
