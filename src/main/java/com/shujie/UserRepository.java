package com.shujie;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long>{
	Mono<User> findByEmail(String email);
//	Mono<User> findByUsername(String username);
}
