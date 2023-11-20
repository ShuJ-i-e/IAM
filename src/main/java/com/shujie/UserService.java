package com.shujie;

import java.util.List;

import com.shujie.User;

public interface UserService {
	
	User saveUser(User user);
	
	List<User> fetchUserList();
	
	User updateUser(User user, Long userId);
	
	void deleteUserById(Long userId);

	User getById(Long userId);

}
