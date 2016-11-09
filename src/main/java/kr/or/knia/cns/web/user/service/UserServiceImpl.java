package kr.or.knia.cns.web.user.service;

import java.util.Calendar;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.or.knia.cns.domain.User;
import kr.or.knia.cns.domain.User.UserStatus;
import kr.or.knia.cns.repository.CodeRepository;
import kr.or.knia.cns.repository.UserRepository;
import kr.or.knia.cns.web.InvalidDataException;
import kr.or.knia.cns.web.NoSuchUserException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private CodeRepository code;

	public Page<User> getUsers(Pageable page, String corpCode, String name, Set<String> job) {
		if (job != null && job.size() > 0) {
			return repo.findAll(corpCode, name, job, page);
		}
		else {
			return repo.findAll(corpCode, name, page);
		}
	}

	public User save(User user) {
		if(user.getNo() != null) {
			User saved = repo.findOne(user.getNo());
			user.dirtyCheck(saved);
		}

		return repo.save(user);
	}

	public User delete(Integer no) {
		User user = repo.getOne(no);
		repo.delete(user);

		return user;
	}

	@Override
	public User login(User user) {
		Integer accountExpire = Integer.parseInt(code.getValue("LOGIN_POLICY", "ACCOUNT_EXPIRE"));
		Integer pinExpire = Integer.parseInt(code.getValue("LOGIN_POLICY", "PIN_EXPIRE"));
		Integer pinMaxError = Integer.parseInt(code.getValue("LOGIN_POLICY", "PIN_MAX_ERROR"));
		Calendar now = Calendar.getInstance();

		User login = repo.findById(user.getId());

		if (login == null) {
			throw new NoSuchUserException();
		}
		else if (!login.getPin().equals(user.getPin())) {
			int errorCount = login.getPinErrorCount();

			if (errorCount < pinMaxError) {
				login.setPinErrorCount(++ errorCount);
				repo.save(login);
				throw new NoSuchUserException();
			}
			else {
				throw new InvalidDataException("login.deny.locked");				
			}
		}
		else if (login.getEnableSystem().getLevel() < user.getEnableSystem().getLevel()) {
			throw new InvalidDataException("enableSystem", "login.deny.enable-system");
		}
		else if (login.getStatus() != UserStatus.APPROVE) {
			throw new InvalidDataException("login.deny.not-approved");
		}
		else {
			Calendar someday = login.getAccessed();
			if (someday != null) { 
				someday.add(Calendar.DATE, accountExpire);
			
				if (now.after(someday)) {
					throw new InvalidDataException("login.deny.sleep");
				}
			}

			someday = login.getPinChanged();
			someday.add(Calendar.DATE, pinExpire);

			if (now.after(someday)) {
				throw new InvalidDataException("login.deny.fusty-pin");
			}
		}

		login.setAccessed(now);
		login.setPinErrorCount(0);

		return repo.save(login);
	}
}
