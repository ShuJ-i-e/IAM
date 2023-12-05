package com.shujie.Repository;

import reactor.core.publisher.Flux;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.shujie.Roles;

@Repository
public interface RoleRepository extends R2dbcRepository<Roles, Long> {
	 Flux<Roles> findByUserId(Long userId);
}
