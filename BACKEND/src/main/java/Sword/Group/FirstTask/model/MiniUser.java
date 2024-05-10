package Sword.Group.FirstTask.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import Sword.Group.FirstTask.dao.UsersDAO;
import Sword.Group.FirstTask.userDetails.UserInfoUserDetails;

public class MiniUser {

	private int Id;
	private String Username;
	private String Email;
	private String Password;
	private String Address; //TAKE CARE HEREE
	private String Roles;

//	public List<Role> getRoles(UserInfoUserDetails UserDetail) {
//		return (List<Role>) UserDetail.getAuthorities();
//	}



	public MiniUser() {

	}

//	public MiniUser(String username, String email, String address,  String roles) {
//		super();
//		Username = username;;
//		Email = email;
//		Address = address;
//		Roles = roles;
//	}
	public MiniUser(int id, String username, String email, String address,  String roles) {
		super();
		Id = id;
		Username = username;;
		Email = email;
		Address = address;
		Roles = roles;
	}

	public String getRoles() {
		return Roles;
	}
	public void setRoles(String roles) {
		this.Roles = roles;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
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
	public void setId(int id) {
		this.Id = id;
	}

	public int getId() {
		return Id;
	}
}
