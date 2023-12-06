package com.shujie.Service;

import java.util.List;

import Entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	Mono<User> saveUser(User user);
	
	Flux<User> fetchUserList();
	
	Mono<User> updateUser(User user, Long userId);
	
	Mono<User> deleteUserById(Long userId);

	Mono<User> getById(Long userId);
	
//	Mono<Object> signUp(User user);
	
	Mono<Object> registerUser(String username, String email, String password, String role);

    Mono<User> verifyEmail(String verificationToken);
	
//	Mono<String> signIn(String email, String password);

}
