package kr.or.knia.reply.system.index.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface IndexDao {
	String getHelp(String id);
	int saveHelp(String id, String content);
}
