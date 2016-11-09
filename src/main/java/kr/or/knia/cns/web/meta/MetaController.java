package kr.or.knia.cns.web.meta;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.knia.cns.domain.Form;
import kr.or.knia.cns.domain.Transaction;
import kr.or.knia.cns.repository.FormRepository;
import kr.or.knia.cns.repository.TransactionRepository;
import kr.or.knia.cns.web.InvalidDataException;

@RestController
@RequestMapping("/meta")
public class MetaController {

	@Autowired
	private TransactionRepository txRepo;
	
	@Autowired
	private FormRepository formRepo;

	@PutMapping("/transaction")
	public Transaction save(@Valid @RequestBody Transaction tx, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}

		return txRepo.save(tx);
	}

	@DeleteMapping("/transaction")
	public Transaction delete(@RequestBody Transaction tx) {
		txRepo.delete(tx);
		return tx;
	}

	@PutMapping("/form")
	public Form saveForm(@Valid @RequestBody Form form, BindingResult result) {
		if (result.hasErrors()) {
			throw new InvalidDataException(result);
		}

		return formRepo.save(form);
	}

	@DeleteMapping("/form")
	public Form deleteForm(@RequestBody Form form) {
		formRepo.delete(form);
		return form;
	}

}
