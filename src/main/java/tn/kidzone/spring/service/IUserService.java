package tn.kidzone.spring.service;

import java.util.List;

import tn.kidzone.spring.entity.User;
import tn.kidzone.spring.entity.User.Role;

public interface IUserService {
	public List<User> getAllUsers();
	public User addUser(User u);
	public void deleteUser(User u);
	public User updateUser(User u);
	public User getUser(String id);
	public User getUserByEmail(String email);
	public User authenticate(String login, String password) ;
	public int NumberOfUsers();
	public int NumberOfVisitors();
	public int NumberOfRecentUsers();
    public int NumberOfRecentVisitors();
	public int Numberof(Role role);
}
