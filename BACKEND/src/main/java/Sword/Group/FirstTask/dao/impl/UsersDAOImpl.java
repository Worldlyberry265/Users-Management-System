package Sword.Group.FirstTask.dao.impl;

import java.util.List;

import java.time.ZonedDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Repository;

import org.springframework.web.bind.annotation.RequestBody;

import Sword.Group.FirstTask.dao.UsersDAO;
import Sword.Group.FirstTask.exceptions.CustomException;
import Sword.Group.FirstTask.model.MiniUser;
import Sword.Group.FirstTask.model.Role;
import Sword.Group.FirstTask.model.Users;
import Sword.Group.FirstTask.security.JwtService;
import Sword.Group.FirstTask.userDetails.UserInfoUserDetailsService;

@Repository
public class UsersDAOImpl implements UsersDAO {

	@Autowired
	JdbcTemplate Jtemplate;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	UserInfoUserDetailsService UserService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public ResponseEntity<Object> update(MiniUser user) {
		System.out.println("UserPassword: " + user.getPassword());
		int id = user.getId();
		String Pass = user.getPassword();
		try {
			// Validate input for special characters
			if (!isUsernametValid(user.getUsername())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Username, Only Letters and spaces are allowed.", "/UpdateUser", ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			// Validate input for special characters
			if (!isEmailValid(user.getEmail())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Email, Only letters, numbers, and .-_ are allowed", "/UpdateUser",
						ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			if (user.getPassword() != null) {

				// Validate input for special characters
				if (!isPassValid(user.getPassword())) {
					CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
							"Invalid Password, Only letters, numbers, and !@#$%^&* are allowed", "/UpdateUser",
							ZonedDateTime.now());
					return exception.toResponseEntity();
//					throw new IllegalArgumentException(
//							"INVALID Password! ONLY LETTERS, NUMBERS, AND !@#$%^&* ARE ALLOWED.");
				}

				String sql = "SELECT Password FROM users where ID = ? LIMIT 1";
				try {
					String password = Jtemplate.queryForObject(sql, new Object[] { id }, String.class);
					if (user.getPassword() != password) {
						Pass = passwordEncoder.encode(user.getPassword());
					}
				} catch (DataAccessException e) {
					// Exception occurred while accessing the data
					CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/UpdateUser",
							ZonedDateTime.now());
					return exception.toResponseEntity();

				}
			}
			// Check if the email is unique
//			System.out.println("TRIGGERED1");
			String Emailsql = "SELECT Email FROM users where ID = ? LIMIT 1";
			String Email = Jtemplate.queryForObject(Emailsql, new Object[] { id }, String.class);
//			System.out.println("Email1: " + user.getEmail());
//			System.out.println("Email2: " + Email);
			if (!user.getEmail().matches(Email)) {
				boolean isEmailUnique = isEmailUnique(user.getEmail());
				if (!isEmailUnique) {
					System.out.println("TRIGGERED2");
					CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
							"Email already exists, You should use a different email.", "/UpdateUser",
							ZonedDateTime.now());
					return exception.toResponseEntity();
				}
			}

			if (!isAddressValid(user.getAddress())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Address, Only letters, numbers, spaces, and comas are allowed", "/UpdateUser",
						ZonedDateTime.now());
				return exception.toResponseEntity();
			}
			if (user.getRoles() != null) {
				if (!isRolesValid(user.getRoles())) {
					CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
							"Invalid Roles, Only letters, spaces, and comas are allowed", "/UpdateUser",
							ZonedDateTime.now());
					return exception.toResponseEntity();
				}
			}
			int result = 0;
			if (user.getPassword() != null) {
				String updateSql = "UPDATE users SET Username = ?, Password = ?, Email = ?, Address = ? WHERE ID = ?";
				result = Jtemplate.update(updateSql, user.getUsername(), Pass, user.getEmail(), user.getAddress(), id);
			} else {
				String updateSql = "UPDATE users SET Username = ?, Email = ?, Address = ? WHERE ID = ?";
				result = Jtemplate.update(updateSql, user.getUsername(), user.getEmail(), user.getAddress(), id);
			}

			int result1 = 0; // boolean1 = false;
			// int result2 = 0;
			// int result3 = 0;
			System.out.println(
					user.getEmail() + user.getUsername() + user.getPassword() + user.getAddress() + user.getRoles());
			System.out.println("ROLES ARE " + user.getRoles());
			String[] rolesArray = user.getRoles().split(",");

			// Unnecessary try?
			String deleteRolesSql = "DELETE FROM user_roles WHERE user_id = ?";
			Jtemplate.update(deleteRolesSql, id);

			for (String role : rolesArray) {

				if (role.equalsIgnoreCase("viewer")) {

					String insertRoleSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
					result1 = Jtemplate.update(insertRoleSql, id, 3);
					System.out.println("Performing action for viewer role");
				} else if (role.equalsIgnoreCase("administrator")) {
					String insertRoleSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
					result1 = Jtemplate.update(insertRoleSql, id, 2);
					System.out.println("Performing action for admin role");
				} else if (role.equalsIgnoreCase("user")) {
					String insertRoleSql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
					result1 = Jtemplate.update(insertRoleSql, id, 1);
					System.out.println("Performing action for user role");
				} else {
					CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
							"No Such Role Available", "/UpdateUser", ZonedDateTime.now());
					return exception.toResponseEntity();
				}
			}
			System.out.println("i AM TRIGGERED");

			if (result > 0 && result1 > 0) {
				return ResponseEntity.ok("User Updated");
			}
		} catch (DataAccessException e) {
			// Exception occurred while accessing the data
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/UpdateUser",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		CustomException exception = new CustomException(HttpStatus.SERVICE_UNAVAILABLE.value(), // Another other value?
				"SERVICE_UNAVAILABLE", "An error occurred while accessing the data", "/UpdateUser",
				ZonedDateTime.now());
		return exception.toResponseEntity();

	}

	@Override
	public ResponseEntity<Object> save(Users user) {
		try {
			// Validate input for special characters
			if (!isUsernametValid(user.getUsername())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Username, Only Letters and spaces are allowed.", "/SaveUser", ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			// Validate input for special characters
			if (!isPassValid(user.getPassword())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Password, Only letters, numbers, and !@#$%^&* are allowed", "/SaveUser",
						ZonedDateTime.now());
				return exception.toResponseEntity();
//				throw new IllegalArgumentException(
//						"INVALID Password! ONLY LETTERS, NUMBERS, AND !@#$%^&* ARE ALLOWED.");
			}

			// Validate input for special characters
			if (!isEmailValid(user.getEmail())) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Invalid Email, Only letters, numbers, and .-_ are allowed", "/SaveUser", ZonedDateTime.now());
				return exception.toResponseEntity();
			}

			String hashedPassword = passwordEncoder.encode(user.getPassword());

			// Check if the email is unique
			boolean isEmailUnique = isEmailUnique(user.getEmail());
			if (!isEmailUnique) {
				CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
						"Email already exists, You should use a different email.", "/SaveUser", ZonedDateTime.now());
//				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception);
				return exception.toResponseEntity();
			}

			int result = Jtemplate.update("INSERT INTO users (Username, Password, Email) VALUES (?, ?, ?)",
					user.getUsername(), hashedPassword, user.getEmail());

			if (result > 0) {
				return ResponseEntity.ok("User Saved");
			}
		} catch (DataAccessException e) {
			// Exception occurred while accessing the data
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/SaveUser",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		CustomException exception = new CustomException(HttpStatus.SERVICE_UNAVAILABLE.value(), // Another other value?
				"SERVICE_UNAVAILABLE", "An error occurred while accessing the data", "/SaveUser", ZonedDateTime.now());
		return exception.toResponseEntity();

	}

	// All the regexes below reduce significantly the percentage of successful sql
	// injections by not allowing semicolons and quotes

	private boolean isUsernametValid(String input) {
		// Perform input validation to disallow special characters
		String regex = "^[A-Za-z ]+$"; // Regular expression that only allows alphabets and spaces
		return input.matches(regex);
	}

	private boolean isAddressValid(String input) {
		// Perform input validation to disallow special characters
		String regex = "^[A-Za-z0-9 ,]+$"; // letters, numbers, spaces, and comas
		return input.matches(regex);
	}

	private boolean isRolesValid(String input) {
		// Perform input validation to disallow special characters
		String regex = "^[A-Za-z ,]+$"; // letters, spaces, and comas
		return input.matches(regex);
	}

	// 6 chars min, small and capital and include number atleast 1, and 1 special
	// min
	private boolean isPassValid(String input) {
		// Perform input validation to disallow special characters
		String regex = "^[A-Za-z0-9!@#$%^&*]+$"; // Regular expression that only allows alphabets, numbers, and special
													// // characters
		return input.matches(regex);
	}

	private boolean isEmailValid(String input) {
		// Perform input validation to disallow special characters
		// new regex ^[a-zA-Z][a-zA-Z0-9._-]*@[a-zA-Z0-9-]+\.[a-zA-Z]{2,4}$
		String regex = "^[A-Za-z0-9._-]+@[A-Za-z.]+\\.[A-Za-z]{2,}$"; // Regular expression for an email
		return input.matches(regex);
	}

	private boolean isEmailUnique(String email) { // To search if the email is already used
		String sql = "SELECT COUNT(*) FROM users WHERE Email = ? LIMIT 1";
		int count = Jtemplate.queryForObject(sql, Integer.class, email);
		return count == 0; // if not found return true, else if count = 1 return false
	}

	@Override
	public List<MiniUser> getAll(String field, int Index, int pageSize) {
//CANT YET SORT BY ROLES 
//		System.out.println("FIELD:            " + field);
//		if(field == null) {
//			field = "s";
//		}
		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "//homepage/getUsers/", ZonedDateTime.now());

//	    String sql = "SELECT u.Username, u.Email, u.Address, r.Name AS role_name "
//	    		+ 	 "FROM users u"
//	    		+ 	 "INNER JOIN user_roles ur ON u.ID = ur.User_ID"
//	    		+ 	 "INNER JOIN roles r ON ur.Role_ID = r.ID";

//
		// Option 1
		int pageIndex = Index;
		int PageSize = pageSize;
//		String sql = "SELECT Username, Email, Address From users"; // Option 2
		int offset = pageIndex * PageSize;

		switch (field.toLowerCase()) {
		case "usernameasc":
			field = "ORDER BY Username ASC";
			break;
		case "usernamedesc":
			field = "ORDER BY Username DESC";
			break;
		case "emailasc":
			field = "ORDER BY Email ASC";
			break;
		case "emaildesc":
			field = "ORDER BY Email DESC";
			break;
		case "addressasc":
			field = "ORDER BY Address ASC";
			break;
		case "addressdesc":
			field = "ORDER BY Address DESC";
			break;
		default:
			field = "";
		}
		String sql = "SELECT ID, Username, Email, Address FROM users " + field + " LIMIT " + PageSize + " OFFSET "
				+ offset;

		try {
			List<MiniUser> users = Jtemplate.query(sql, (resultSet, rowNum) -> {
				int id = resultSet.getInt("ID");
				String username = resultSet.getString("Username");
				String email = resultSet.getString("Email");
				String address = resultSet.getString("Address");
				List<Role> roles = UserService.getUserRoles(username);
				String roleNames = "";
				for (int i = 0; i < roles.size(); i++) {
					roleNames += roles.get(i).getName();
					if (i + 1 != roles.size()) {
						roleNames += ",";
					}
				}
				MiniUser user = new MiniUser(id, username, email, address, roleNames);
				return user;
			});
			return users;

		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "Empty Database",
					"//homepage/getUsers/", ZonedDateTime.now());
			throw exception;
		} catch (DataAccessException e) {
			exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
					"An error occurred while accessing the data", "//homepage/getUsers/", ZonedDateTime.now());
			throw exception;
		}

	}

	@Override
	public List<MiniUser> FilterBy(String filterValue) {

		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "/dashboard/filterBy", ZonedDateTime.now());

//		String sql = "SELECT Username, Email, Address "
//			+ "FROM users "
//			+ "WHERE Username LIKE '%baraa training%'"
//			+ "OR Email LIKE '%baraa training%'"
//			+ "OR Address LIKE '%baraa training%';";
		String sql = "SELECT u.ID ,u.Username, u.Email, u.Address, r.Name AS role_name " + "FROM users u "
				+ "INNER JOIN user_roles ur ON u.ID = ur.User_ID " + "INNER JOIN roles r ON ur.Role_ID = r.ID "
				+ "WHERE u.Username LIKE '%" + filterValue + "%' " + "   OR u.Email LIKE '%" + filterValue + "%' "
				+ "   OR u.Address LIKE '%" + filterValue + "%' " + "   OR r.Name LIKE '%" + filterValue + "%';";

		try {
			List<MiniUser> users = Jtemplate.query(sql, (resultSet, rowNum) -> {
				int id = resultSet.getInt("ID");
				String username = resultSet.getString("Username");
				String email = resultSet.getString("Email");
				String address = resultSet.getString("Address");
				String RoleName = resultSet.getString("role_name");
				MiniUser user = new MiniUser(id, username, email, address, RoleName);
				return user;
			});
			return users;

		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND",
					"No Users were found with this filter", "dashboard/filterBy", ZonedDateTime.now());
			throw exception;
		} catch (DataAccessException e) {
			exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
					"An error occurred while accessing the data", "/dashboard/filterBy", ZonedDateTime.now());
			throw exception;
		}

	}

	@Override
	public MiniUser getUserById(int ID) {
		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "/dashboard/usersList/edit/:id", ZonedDateTime.now());

		String sql = "SELECT Username, Email, Password, Address FROM users WHERE ID=? ";

//		MiniUser foundUser = new MiniUser();
//		 MiniUser returnedUser = new MiniUser();

		try {
//			MiniUser foundUser = Jtemplate.queryForObject(sql, MiniUser.class, ID);

			@SuppressWarnings("deprecation")
			MiniUser user = Jtemplate.queryForObject(sql, new Object[] { ID }, (resultSet, rowNum) -> {
				MiniUser foundUser = new MiniUser();
//			return Jtemplate.queryForObject(sql, (resultSet, rowNum) -> {
//			    int id = resultSet.getInt("ID");
				foundUser.setId(ID);
				foundUser.setUsername(resultSet.getString("Username"));
				foundUser.setEmail(resultSet.getString("Email"));
				foundUser.setPassword(resultSet.getString("Password"));
				foundUser.setAddress(resultSet.getString("Address"));
				List<Role> roles = UserService.getUserRoles(foundUser.getUsername());
				String roleNames = "";
				for (int i = 0; i < roles.size(); i++) {
					roleNames += roles.get(i).getName();
					if (i + 1 != roles.size()) {
						roleNames += ",";
					}
				}
				foundUser.setRoles(roleNames);
				return foundUser;
//			    returnedUser.setRoles(roleNames);
//			   returnedUser = (id, username, email, address, roleNames);
//			    return  returnedUser;
			});
//			return Jtemplate.queryForObject(sql, new BeanPropertyRowMapper<MiniUser>(MiniUser.class), ID);
			return user;
		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND",
					"No Users were found with this filter", "homepage/filterBy", ZonedDateTime.now());
			throw exception;
		} catch (DataAccessException e) {
			System.out.println("Error occurred during data access: " + e.getMessage()); // Add this line
			e.printStackTrace();
//			System.out.println("foundUser: " + );
			exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
					"An error occurred while accessing the data", "/homepage/filterBy", ZonedDateTime.now());
			throw exception;
		}

//		return returnedUser;
	}

	@Override
	public int getUserCount() {

		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "//homepage/getUserCount/", ZonedDateTime.now());

		String sql = "SELECT COUNT(*) FROM users";
		int UsersCount = 0;
		try {

			UsersCount = Jtemplate.queryForObject(sql, Integer.class);

		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "Empty Database",
					"//homepage/getUserCount/", ZonedDateTime.now());
			throw exception;
		} catch (DataAccessException e) {
			exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
					"An error occurred while accessing the data", "//homepage/getUserCount/", ZonedDateTime.now());
			throw exception;
		}
		return UsersCount;
	}

	@Override
	public ResponseEntity<Object> AuthenticateUser(Users user) {
//	    String sql = "SELECT COUNT(*) FROM users WHERE Email = ? LIMIT 1";
		String sql2 = "SELECT Password FROM users WHERE Email = ? LIMIT 1";

		// Validate input for special characters
		if (!isEmailValid(user.getEmail())) {
			CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
					"Invalid Email, Only letters, numbers, and .-_ are allowed", "/AuthenticateUser",
					ZonedDateTime.now());
			return exception.toResponseEntity();
		}

		// Validate input for special characters
		if (!isPassValid(user.getPassword())) {
			CustomException exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
					"Invalid Paasword, Only letters, numbers, and !@#$%^&* are allowed", "/AuthenticateUser",
					ZonedDateTime.now());
			return exception.toResponseEntity();
		}

		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/AuthenticateUser",
				ZonedDateTime.now());
		try {

			String retrievedPassword = Jtemplate.queryForObject(sql2, String.class, user.getEmail());
			if (retrievedPassword != null && passwordEncoder.matches(user.getPassword(), retrievedPassword)) {
				return ResponseEntity.ok("You may sign in");
			} else if (retrievedPassword != null) {
				exception = new CustomException(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED",
						"You have entered a wrong password", "/AuthenticateUser", ZonedDateTime.now());
				return exception.toResponseEntity();
			}
		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND",
					"Please check your email and try again", "/AuthenticateUser", ZonedDateTime.now());
			return exception.toResponseEntity();
		} catch (DataAccessException e) {

			return exception.toResponseEntity();
		}
		return exception.toResponseEntity();
	}

	@Override
	public ResponseEntity<Object> authANDGetToken(@RequestBody Users user) {

		String foundUser = null;

		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "/authenticate", ZonedDateTime.now());

		String sql2 = "SELECT Username FROM users WHERE Email = ? LIMIT 1"; // just anything

		// Validate input for special characters
		if (!isEmailValid(user.getEmail())) {
			exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
					"Invalid Email, Only letters, numbers, and .-_ are allowed", "/authenticate", ZonedDateTime.now());
			return exception.toResponseEntity();
		}
		// Validate input for special characters
		if (!isPassValid(user.getPassword())) {
			exception = new CustomException(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST",
					"Invalid Paasword, Only letters, numbers, and !@#$%^&* are allowed", "/authenticate",
					ZonedDateTime.now());
			return exception.toResponseEntity();

		}
		try {
			foundUser = Jtemplate.queryForObject(sql2, String.class, user.getEmail());

			if (foundUser != null) {

				try {
					// The authenticate method will fetch the userDetails from the Db and compare it
					// with the authRequest
//		System.out.println("User: " + user.getEmail());
//		System.out.println("foundUser: " + foundUser);
					Authentication authentication = authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(foundUser, user.getPassword()));
//			System.out.println("IM INNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
//		System.out.println("Pass: " + authentication.getCredentials());
//		System.out.println("Pass: " + user.getPassword());

					if (authentication.isAuthenticated()) {
//			return ResponseEntity.ok(jwtService.generateToken(user.getUsername(),user.getRoles()));
						return ResponseEntity.ok(jwtService.generateToken(foundUser));
					}

				} catch (org.springframework.security.core.AuthenticationException ex) {
					exception = new CustomException(HttpStatus.FORBIDDEN.value(), "FORBIDDEN", "Wrong Password",
							"/authenticate", ZonedDateTime.now());
					return exception.toResponseEntity();
				} catch (DataAccessException e) {
					exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
							"An error occurred while accessing the data", "/authenticate", ZonedDateTime.now());
					return exception.toResponseEntity();
				}
			}

		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND",
					"Please check your email and try again", "/authenticate", ZonedDateTime.now());
			return exception.toResponseEntity();
		} catch (DataAccessException e) {
			exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR",
					"An error occurred while accessing the data", "/authenticate", ZonedDateTime.now());
			return exception.toResponseEntity();
		}
		return exception.toResponseEntity();

	}
}
