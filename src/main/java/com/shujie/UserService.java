package com.shujie;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.shujie.User;

public interface UserService {
	
	Mono<User> saveUser(User user);
	
	Flux<User> fetchUserList();
	
	Mono<User> updateUser(User user, Long userId);
	
	Mono<User> deleteUserById(Long userId);

	Mono<User> getById(Long userId);
	
	Mono<Object> signUp(User user);
	
//	Mono<String> signIn(String email, String password);

}
