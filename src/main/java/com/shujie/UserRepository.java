package com.shujie;

//import org.springframework.data.jpa.repository.JpaRepository;
//import com.shujie.User;
//
//public interface UserRepository extends JpaRepository<User, Integer> {
//User findByEmail(String email);
//}

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shujie.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
