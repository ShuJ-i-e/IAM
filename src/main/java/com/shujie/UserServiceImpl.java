package com.shujie;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shujie.User;
import com.shujie.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
//	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
//		this.bCryptPasswordEncoder =  bCryptPasswordEncoder;
	}

//	@Override
//	public User saveUser(User user) {
////		user.setUserId(UUID.randomUUID().toString());
//		user.setPassword(user.getPassword());
//		return userRepository.save(user);
//	}
//
//	@Override
//	public List<User> fetchUserList() {
//		return (List<User>)userRepository.findAll();
//	}
//
//	@Override
//	public User updateUser(User user, Long userId) {
//		User depDB
//        = userRepository.findById(userId)
//              .get();
//
//    if (Objects.nonNull(user.getEmail())
//        && !"".equalsIgnoreCase(
//            user.getEmail())) {
//        depDB.setEmail(
//            user.getEmail());
//    }
//
//    return userRepository.save(depDB);
//	}
//
//	@Override
//	public void deleteUserById(Long userId) {
//		userRepository.deleteById(userId);
//	}
//
//	@Override
//	public User getById(Long userId) {
//
//		return userRepository.findById(userId).get();
//	}

	@Override
	public Mono<User> saveUser(User user) {
//		user.setId(UUID.randomUUID());
//		user.setPassword(user.getPassword());
//		ModelMapper modelmapper = new ModelMapper();
//		modelmapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//		User userSignUpModel = modelmapper.map(user, User.class);
//		SignUpDto dto = modelmapper.map(userRepository.save(userSignUpModel), SignUpDto.class);
		return userRepository.save(user);
//		return userRepository.save(user);
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

//		userRepository.deleteById(userId);
	}

	@Override
	public Mono<User> getById(Long userId) {

		return userRepository.findById(userId);
	}

	@Override
	public Flux<User> fetchUserList() {
		return userRepository.findAll();
	}
}
