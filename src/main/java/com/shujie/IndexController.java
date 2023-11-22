package com.shujie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shujie.User;
import com.shujie.UserRepository;
import com.shujie.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class IndexController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;


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
	
//    // Read operation
//    @GetMapping("/test")
//    public String test()
//    {
//        return "ytest";
//    }
//    
//    @PostMapping("/testpost")
//    public String testpost()
//    {
//    	logger.info("This is an test message.");
//        return "ytest";
//    }
//    
//	@GetMapping("/error")
//	@ResponseBody
//	public String error() {
//		logger.info("This is an error message.");
//		return "Error!";
//	}
//	
	// Save operation
    @PostMapping("/users")
    public Mono<User> saveUser(@RequestBody User user)
    {
    	logger.info("This is an signup message.");
        return userService.saveUser(user);
//    	return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
 
//    public String fallbackMethod(User user, RuntimeException runtimeException) {
//    	return "Oops!";
//    }
    
    // Read operation
    @GetMapping("/users")
    public Flux<User> fetchUserList()
    {
        return userService.fetchUserList();
    }
    
    //Get user by id
	@GetMapping("{id}")
	public Mono<ResponseEntity<User>> getById(@PathVariable("id") final Long id) {
		 Mono<User> user = userService.getById(id);
	        return user.map( u -> ResponseEntity.ok(u))
	                .defaultIfEmpty(ResponseEntity.notFound().build());
	}
 
    // Update operation
    @PutMapping("/users/{id}")
    public Mono<ResponseEntity<User>>
    updateUser(@RequestBody User user,
                     @PathVariable("id") Long userId)
    {
    	  return userService.updateUser(user, userId)
                  .map(updatedUser -> ResponseEntity.ok(updatedUser))
                  .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
 
    // Delete operation
    @DeleteMapping("/users/{id}")
 
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable("id")
                                       Long userId)
    {
    	 return userService.deleteUserById(userId)
                 .map( r -> ResponseEntity.ok().<Void>build())
                 .defaultIfEmpty(ResponseEntity.notFound().build());
     }

}