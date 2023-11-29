package com.shujie;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shujie.User;
import com.shujie.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	public UserServiceImpl(UserRepository userRepositor) {
		this.userRepository = userRepository;
//		this.bCryptPasswordEncoder =  bCryptPasswordEncoder;
	}

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    
	@Override
	public Mono<User> saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Mono<User> updateUser(User user, Long userId) {
		return userRepository.findById(userId).flatMap(dbUser -> {
			dbUser.setFirstName(user.getFirstName());
			dbUser.setLastname(user.getLastname());
			dbUser.setEmail(user.getEmail());
			dbUser.setAbout(user.getAbout());
			dbUser.setRoles(user.getRoles());
			dbUser.setLanguages(user.getLanguages());
			dbUser.setSkills(user.getSkills());
			dbUser.setProject_experiences(user.getProject_experiences());
			dbUser.setAssignments(user.getAssignments());
			dbUser.setProfilePic(user.getProfilePic());
			return userRepository.save(dbUser);
		});
	}

	@Override
	public Mono<User> deleteUserById(Long userId) {
		return userRepository.findById(userId)
				.flatMap(existingUser -> userRepository.delete(existingUser).then(Mono.just(existingUser)));
	}

	@Override
	public Mono<User> getById(Long userId) {

		return userRepository.findById(userId);
	}

	@Override
	public Flux<User> fetchUserList() {
		return userRepository.findAll();
	}
	
    @Override
    public Mono<Object> signUp(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.error(new RuntimeException("User with this email already exists")))
                .switchIfEmpty(Mono.defer(() -> {
//                    user.setPassword(passwordEncoder().encode(user.getPassword()));
                    return userRepository.save(user);
                }));
    }

//    @Override
//    public Mono<String> signIn(String email, String password) {
////         Find the user by email
//        return userRepository.findByEmail(email)
//                .flatMap(user -> {
//                    // Check if the entered password matches the stored hashed password
//                    if (passwordEncoder.matches(password, user.getPassword())) {
//                        // Return a token or some indication of successful authentication
//                        // For simplicity, returning a success message in this example
//                        return Mono.just("Authentication successful");
//                    } else {
//                        // Passwords do not match
//                        return Mono.error(new RuntimeException("Invalid credentials"));
//                    }
//                })
//                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
//        return Mono.error(new RuntimeException("Invalid credentials"));
//    }
    
    public void updateResetPasswordToken(String token, String email) throws RuntimeException {
        Mono<User> user = userRepository.findByEmail(email);
        if (user != null) {
        	user.flatMap(dbUser -> {
    			dbUser.setResetPasswordToken(dbUser.getResetPasswordToken());
    			return userRepository.save(dbUser);
    		});
	
        } else {
            throw new RuntimeException("Could not find any customer with the email " + email);
        }
    }
     
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }


}
