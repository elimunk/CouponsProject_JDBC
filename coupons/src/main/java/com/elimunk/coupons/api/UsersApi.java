package com.elimunk.coupons.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.elimunk.coupons.beans.PostLoginUserData;
import com.elimunk.coupons.beans.User;
import com.elimunk.coupons.beans.UserIdTypeAndToken;
import com.elimunk.coupons.exceptions.ApplicationException;
import com.elimunk.coupons.logic.UsersController;

@RestController
@RequestMapping("/users")
public class UsersApi {

	@Autowired
	private UsersController usersController;
	
	@PostMapping
	public void createUser(@RequestBody User user) throws ApplicationException {
		usersController.createUser(user);
	}
	
	@PutMapping()
	public void updateUser(@RequestBody User userToUpdate, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		usersController.updateUser(userToUpdate, userData );
	}
	
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable ("userId") long userId, HttpServletRequest request) throws ApplicationException {
		PostLoginUserData userData = (PostLoginUserData) request.getAttribute("userData");
		usersController.deleteUser(userId, userData);
	}
	
	@GetMapping("/{userId}")
	public User getUser(@PathVariable("userId") long userId) throws ApplicationException {
		return usersController.getUser(userId);
	}
	
	@GetMapping
	public List<User> getAllUsers() throws ApplicationException {
		return usersController.getAllUsers();
	}
	
	@GetMapping("/byCompany")
	public List<User> getCompanyUsers(@RequestParam("id") long companyId) throws ApplicationException {
		return usersController.getCompanyUsers(companyId);
	}
	
	@PostMapping("/login")
	public UserIdTypeAndToken login(@RequestBody User user) throws ApplicationException {
		return usersController.login(user.getUserName(), user.getPassword());
	}
	
}
