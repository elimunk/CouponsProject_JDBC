package com.elimunk.coupons.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elimunk.coupons.beans.User;
import com.elimunk.coupons.enums.ClientType;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.UsersController;

@RestController
@RequestMapping("/users")
public class UsersApi {

	@Autowired
	private UsersController usersController;
	
	@PostMapping
	public void createCoupon(@RequestBody User user) throws ApplicationException {
		usersController.createUser(user);
	}
	
	@PutMapping()
	public void updateCoupon(@RequestBody User userToUpdate) throws ApplicationException {
		usersController.updateUser(userToUpdate);
	}
	
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable ("userId") long userId) throws ApplicationException {
		usersController.deleteUser(userId);
	}
	
	@GetMapping("/{userId}")
	public User getUser(@PathVariable ("userId") long userId) throws ApplicationException {
		return usersController.getUser(userId);
	}
	
	@GetMapping("/all")
	public List<User> getAllUsers() throws ApplicationException {
		return usersController.getAllUsers();
	}
	
	@GetMapping("/login")
	public ClientType login(@RequestParam("userName") String userName, @RequestParam("password") String password) throws ApplicationException {
		return usersController.login(userName, password);
	}
	
	
	
}