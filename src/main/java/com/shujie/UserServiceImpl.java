package com.shujie;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shujie.User;
import com.shujie.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
//		this.bCryptPasswordEncoder =  bCryptPasswordEncoder;
	}

	@Override
	public User saveUser(User user) {
//		user.setUserId(UUID.randomUUID().toString());
		user.setPassword(user.getPassword());
		return userRepository.save(user);
	}

	@Override
	public List<User> fetchUserList() {
		return (List<User>)userRepository.findAll();
	}

	@Override
	public User updateUser(User user, Long userId) {
		User depDB
        = userRepository.findById(userId)
              .get();

    if (Objects.nonNull(user.getEmail())
        && !"".equalsIgnoreCase(
            user.getEmail())) {
        depDB.setEmail(
            user.getEmail());
    }

    return userRepository.save(depDB);
	}

	@Override
	public void deleteUserById(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public User getById(Long userId) {

		return userRepository.findById(userId).get();
	}

}
