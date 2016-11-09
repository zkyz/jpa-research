package kr.or.knia.cns.web.code;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.knia.cns.domain.Code;
import kr.or.knia.cns.repository.CodeRepository;
import kr.or.knia.cns.web.InvalidDataException;

@RestController
@RequestMapping("/code")
public class CodeController {

	@Autowired
	private CodeRepository repo;

	@GetMapping
	public Code read(Code code) {
		return code;
	}
	@GetMapping("/{parent}")
	public List<Code> read(@PathVariable String parent) {
		return repo.findAllByKeyParent(parent);
	}
	@GetMapping("/{parent}/{code}")
	public Code read(@PathVariable String parent,
			@PathVariable String code) {
		return repo.getCode(parent, code);
	}

	@PutMapping
	public Code save(@Valid @RequestBody Code code, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}

		return repo.save(code);
	}
	
	@DeleteMapping
	public Code delete(@Valid @RequestBody Code.Key key) {
		Code code = repo.findOne(key);
		
		if (code == null) {
			repo.delete(code);
		}

		return code;
	}
}
