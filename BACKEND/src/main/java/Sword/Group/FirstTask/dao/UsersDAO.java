package Sword.Group.FirstTask.dao;

import java.util.List;

import org.springframework.http.ResponseEntity;

import Sword.Group.FirstTask.model.MiniUser;
import Sword.Group.FirstTask.model.Users;

public interface UsersDAO { // Data Access Object

	ResponseEntity<Object> save(Users user);

	List<MiniUser> getAll(String field, int Index, int PageSize);

	ResponseEntity<Object> authANDGetToken(Users user);

	ResponseEntity<Object> AuthenticateUser(Users user);

	int getUserCount();

	List<MiniUser> FilterBy(String filterValue);

	MiniUser getUserById(int id);

	ResponseEntity<Object> update(MiniUser user);

}
