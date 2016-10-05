package kr.or.knia.reply.system.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.knia.reply.system.index.dao.IndexDao;

@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	private IndexDao dao;

	@Override
	public String getHelp(String id) {
		return dao.getHelp(id);
	}

	@Override
	public int saveHelp(String id, String content) {
		return dao.saveHelp(id, content);
	}
}
