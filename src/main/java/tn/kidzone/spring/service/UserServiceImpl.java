package tn.kidzone.spring.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.kidzone.spring.entity.User;
import tn.kidzone.spring.entity.User.Role;
import tn.kidzone.spring.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	UserRepository userRepository;
	private static final Logger l = LogManager.getLogger(IUserService.class);

	@Override
	public List<User> getAllUsers() {
		List<User> users = (List<User>) userRepository.findAll();
		for (User user : users) {
			l.info("user list : " + user);
		}
		return users;
	}

	@Override
	public User getUser(String id) {
		return userRepository.findById(Long.parseLong(id)).get();
	}

	@Override
	public User addUser(User u) {
		return userRepository.save(u);
	}

	@Override
	public void deleteUser(User u) {
		userRepository.delete(u);

	}

	@Override
	public User updateUser(User u) {
		return userRepository.save(u);
	}
	
	@Override
	public User getUserByEmail(String email) {
		return userRepository.retrieveUsersByEmail(email);
	}

	@Override
	public User authenticate(String email, String password) {
		return userRepository.getUserByEmailAndPassword(email, password);
	}

	@Override
	public int NumberOfUsers() {
		return userRepository.NumberOfUsers();
	}

	@Override
	public int NumberOfVisitors() {
		return userRepository.NumberOfVisitors();
	}

	@Override
	public int NumberOfRecentUsers() {
		return userRepository.NumberOfRecentUsers();
	}

	@Override
	public int NumberOfRecentVisitors() {
		return userRepository.NumberOfRecentVisitors();
	}

	@Override
	public int Numberof(Role role) {
		return userRepository.NumberOf(role);
	}

}
