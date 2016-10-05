package kr.or.knia.system;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

@Controller
@RequestMapping("/system/logger")
public class LoggerController {

	@RequestMapping(params = "level")
	public void setLogLevel(HttpServletResponse response,
			@RequestParam String level) throws Exception {
	    Logger logger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	    logger.setLevel(Level.valueOf(level));

	    logger = (Logger)LoggerFactory.getLogger("kr.or.knia");
	    logger.setLevel(Level.valueOf(level));
	    
	    OutputStream os = response.getOutputStream();
	    os.write(new String("changed normaly to " + level).getBytes());
	    os.flush();
	    os.close();
	}
}
