package com.shujie.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shujie.Repository.UserRepository;
import com.shujie.Service.UserServiceImpl;

import Entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class IndexController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	private JavaMailSender mailSender;

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

//	// Save operation
//	@PostMapping("/users")
//	public Mono<User> saveUser(@RequestBody User user) {
//		return userService.saveUser(user);
//	}
	
	//Sign Up / Create (C)
	@PostMapping("/signup")
	public Mono<Object> registerUser(@RequestBody Map<String, String> payload) {
		String username = payload.get("username");
		String email = payload.get("email");
		String password = payload.get("password");
		String role = payload.get("role");

		return userService.registerUser(username, email, password, role).defaultIfEmpty(ResponseEntity.badRequest().build());
	}
	
	//Sign Up Email Verification
	@GetMapping("/verify_email/{verificationToken}")
	public Mono<String> verifyEmail(@PathVariable String verificationToken) {
		return userService.verifyEmail(verificationToken).map(user -> "Email verified successfully.")
				.defaultIfEmpty("Invalid verification token.");
	}

	// Read All Users (R)
	@GetMapping("/users")
	public Flux<User> fetchUserList() {
		return userService.fetchUserList();
	}

	// Get User by ID (R)
	@GetMapping("{id}")
	public Mono<ResponseEntity<User>> getById(@PathVariable("id") final Long id) {
		Mono<User> user = userService.getById(id);
		return user.map(u -> ResponseEntity.ok(u)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	//Forgot Password by Email
	@PostMapping("/forgot_password")
	public Mono<User> forgotPassword(@RequestBody Map<String, String> payload) {
		String email = payload.get("email");
	    if (email == null) {
	        return Mono.error(new RuntimeException("Email is required in the request body"));
	    }
	    return userService.initiatePasswordReset(email);
	}
	
	//Update Password
	@PostMapping("/reset_password/{token}")
	public Mono<Void> resetPassword(@PathVariable String token, @RequestBody Map<String, String> requestBody) {
	    String newPassword = requestBody.get("newPassword");
	    return userService.resetPassword(token, newPassword);
	}

	// Delete operation (D)
	@DeleteMapping("/users/{id}")
	public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable("id") Long userId) {
		return userService.deleteUserById(userId).map(r -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}