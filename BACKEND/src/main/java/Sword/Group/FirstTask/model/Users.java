package Sword.Group.FirstTask.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import Sword.Group.FirstTask.dao.UsersDAO;
import Sword.Group.FirstTask.userDetails.UserInfoUserDetails;

public class Users {

	private int ID;
	private String Username;
	private String Password;
	private String Email;
	private String Address; //TAKE CARE HERE
	private List<Role> roles;

//	public List<Role> getRoles(UserInfoUserDetails UserDetail) {
//		return (List<Role>) UserDetail.getAuthorities();
//	}

	public int getID() {
		return ID;
	}

	public Users() {

	}

	public Users(String username, String password, String email, String address, List<Role>Roles) {
		super();
		Username = username;
		Password = password;
		Email = email;
		Address = address;
		roles = Roles;
	}
	public Users(String username, String email, String address, List<Role> Roles) {
		super();
		Username = username;;
		Email = email;
		Address = address;
		roles = Roles;
	}

	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}
}
