package com.shujie;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends the user to the views.
 *
 * @author Rob Winch
 * @since 5.0
 */
@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	@GetMapping("/")
	@ResponseBody
	public String index() {
		logger.info("This is an informational message.");
		return "index!";
		
	}

	@GetMapping("/login")
	@ResponseBody
	public String login() {
		logger.info("This is an login message.");
		return "Hello!";
	}
	
	@GetMapping("/error")
	@ResponseBody
	public String error() {
		logger.info("This is an error message.");
		return "Error!";
	}
	
	@GetMapping("/test")
	@ResponseBody
	public String test() {
		logger.info("This is an test message.");
		return "Test!";
	}

}
