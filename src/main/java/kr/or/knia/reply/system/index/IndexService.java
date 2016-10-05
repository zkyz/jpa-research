package kr.or.knia.reply.system.index;

public interface IndexService {
	String getHelp(String id);
	int saveHelp(String id, String content);
}
