package kr.or.knia.cns.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.knia.cns.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	Transaction findByCode(String code);
	Transaction findByTestCode(String testCode);
}
