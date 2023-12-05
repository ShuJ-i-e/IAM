package com.shujie.Repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.shujie.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long>{
	Mono<User> findByEmail(String email);
//	Mono<User> findByUsername(String username);
	
	public Mono<User> findByResetPasswordToken(String token);
	
	public Mono<User> findByVerificationToken(String verificationToken);
}
