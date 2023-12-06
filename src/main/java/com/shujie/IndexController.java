package com.shujie;

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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class IndexController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	private JavaMailSender mailSender;

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	// Save operation
	@PostMapping("/users")
	public Mono<User> saveUser(@RequestBody User user) {
		return userService.saveUser(user);
	}

	// Read all operation
	@GetMapping("/users")
	public Flux<User> fetchUserList() {
		return userService.fetchUserList();
	}

	// Get user by id
	@GetMapping("{id}")
	public Mono<ResponseEntity<User>> getById(@PathVariable("id") final Long id) {
		Mono<User> user = userService.getById(id);
		return user.map(u -> ResponseEntity.ok(u)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// Update operation
	@PutMapping("/users/{id}")
	public Mono<ResponseEntity<User>> updateUser(@RequestBody User user, @PathVariable("id") Long userId) {
		return userService.updateUser(user, userId).map(updatedUser -> ResponseEntity.ok(updatedUser))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	// Delete operation
	@DeleteMapping("/users/{id}")

	public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable("id") Long userId) {
		return userService.deleteUserById(userId).map(r -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// Sign Up (Registration)
	@PostMapping("/signup")
	public Mono<ResponseEntity<Object>> signUp(@RequestBody User user) {
		return userService.signUp(user).map(savedUser -> ResponseEntity.status(HttpStatus.CREATED).body(savedUser))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	@PostMapping("/forgot_password")
	public Mono<User> forgotPassword(@RequestBody Map<String, String> payload) {
		String email = payload.get("email");
	    if (email == null) {
	        return Mono.error(new RuntimeException("Email is required in the request body"));
	    }
	    return userService.initiatePasswordReset(email);
	}
	
	@PostMapping("/reset_password/{token}")
	public Mono<Void> resetPassword(@PathVariable String token, @RequestBody Map<String, String> requestBody) {
	    String newPassword = requestBody.get("newPassword");
	    return userService.resetPassword(token, newPassword);
	}

	@PostMapping("/register")
	public Mono<User> registerUser(@RequestBody Map<String, String> payload) {
		String username = payload.get("username");
		String email = payload.get("email");
		String password = payload.get("password");
		String role = payload.get("role");

		return userService.registerUser(username, email, password, role);
	}

	@GetMapping("/verify-email/{verificationToken}")
	public Mono<String> verifyEmail(@PathVariable String verificationToken) {
		return userService.verifyEmail(verificationToken).map(user -> "Email verified successfully.")
				.defaultIfEmpty("Invalid verification token.");
	}

}