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
	    
	
//	Mono<SignUpDto> saveUser(SignUpDto signupDto);
//	
//	Mono<SignUpDto> getUserById(Long userId);
//	
//	Flux<SignUpDto> getAllUsers();
//	
//	Mono<SignUpDto> updateUser(SignUpDto signupDto, Long userId);
//	
//	Mono<Void> deleteUserById(Long userId);

	

}
