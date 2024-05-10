package Sword.Group.FirstTask.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import Sword.Group.FirstTask.dao.UsersDAO;
import Sword.Group.FirstTask.model.MiniUser;
import Sword.Group.FirstTask.model.Users;

@RestController
public class UsersController {

	@Autowired // Instead ProductService service
	private UsersDAO eDAO; // Instead ProductService service

	@GetMapping("/dashboard/getUsers/{field}/{Index}/{PageSize}")
	public List<MiniUser> getUsers(@PathVariable String field, @PathVariable int Index, @PathVariable int PageSize) {
		return eDAO.getAll(field, Index, PageSize);
	}

	@GetMapping("/dashboard/getUserCount")
	public int getUserCount() {
		return eDAO.getUserCount();
	}

	@GetMapping("/dashboard/filterBy/{filterValue}")
	public List<MiniUser> FilterBy(@PathVariable String filterValue) {
		return eDAO.FilterBy(filterValue);
	}

	@GetMapping("/dashboard/usersList/getUser/{id}") // TAKE OUT OTHER PARAMETERS
	public MiniUser getUserById(@PathVariable int id) {
		return eDAO.getUserById(id);
	}

	@PostMapping("/SaveUser")
	public ResponseEntity<Object> saveUser(@RequestBody Users user) {
		ResponseEntity<Object> result = eDAO.save(user);
		return result;
	}
	@PostMapping("/dashboard/usersList/editUser")
	public ResponseEntity<Object> UpdateUser(@RequestBody MiniUser user) {
		ResponseEntity<Object> result = eDAO.update(user);
		return result;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<Object> authenticateAndGetToken(@RequestBody Users user) {
		ResponseEntity<Object> result = eDAO.authANDGetToken(user);
		return result;

	}

	@GetMapping("/AuthenticateUser")
	public ResponseEntity<Object> authenticateUser(@RequestBody Users user) {
		ResponseEntity<Object> result = eDAO.AuthenticateUser(user);
		return result;
	}

}
