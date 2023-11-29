package com.shujie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class IndexController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserServiceImpl userService;

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	// Save operation
    @PostMapping("/users")
    public Mono<User> saveUser(@RequestBody User user)
    {
        return userService.saveUser(user);
    }

    
    // Read all operation
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
    
 // Sign Up (Registration)
    @PostMapping("/signup")
    public Mono<ResponseEntity<Object>> signUp(@RequestBody User user) {
        return userService.signUp(user)
                .map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    // Sign In (Authentication)
    @PostMapping("/signin")
    public Mono<ResponseEntity<String>> signIn(@RequestBody User user) {
        // Assuming SignInRequest is a class that holds email/username and password
        return userService.signIn(user.getEmail(), user.getPassword())
                .map(token -> ResponseEntity.ok().body(token))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

}