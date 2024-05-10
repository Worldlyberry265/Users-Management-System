//BUSINESS LOGIC, FOR VALIDATION PURPOSES
package Sword.Group.FirstTask.userDetails;

import Sword.Group.FirstTask.exceptions.CustomException;
import Sword.Group.FirstTask.model.Role;
import Sword.Group.FirstTask.model.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserInfoUserDetailsService implements UserDetailsService {

	@Autowired
	JdbcTemplate Jtemplate;

	// ASK IF I SHOULD CHANGE IT TO DAOIMPLEMENTATION

	// For JWT Filter
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {
			String sql = "SELECT Username, Password FROM users WHERE Username= ? LIMIT 1";
			Optional<Users> user = Jtemplate.query(sql, new BeanPropertyRowMapper<>(Users.class), username).stream()
					.findFirst();
//		return user.map(UserInfoUserDetails::new).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND.value(),
//				"NOT_FOUND", "Username Not Found", "/authenticate", ZonedDateTime.now()));
			return user.map(u -> new UserInfoUserDetails(u, this))
					.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND",
							"Username Not Found", "/authenticate", ZonedDateTime.now()));

		} catch (DataAccessException e) {
			CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"INTERNAL_SERVER_ERROR", "An error occurred while accessing the data", "/authenticate",
					ZonedDateTime.now());
			throw exception;
		}

	}
//	public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
//		String sql = "SELECT Username FROM users WHERE Email= ? LIMIT 1";
//
//		Optional<Users> user = Jtemplate.query(sql, new BeanPropertyRowMapper<>(Users.class), email).stream()
//				.findFirst();
//		return user.map(UserInfoUserDetails::new)
//				.orElseThrow(() -> new UsernameNotFoundException("user not found " + email));
//
//	}

	@SuppressWarnings("deprecation")
	public List<Role> getUserRoles(String username) {
		int id = -1;
		List<Role> roles = new ArrayList<>();

		CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", "Server Out Of Service", "/getUserRoles", ZonedDateTime.now());
		try {
			String sql = "SELECT ID FROM users WHERE Username = ? LIMIT 1";
			id = Jtemplate.queryForObject(sql, Integer.class, username);
		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND",
					"Please check your email and try again", "/getUserRoles", ZonedDateTime.now());
			throw exception;
		} catch (DataAccessException e) {
			throw exception;
		}

		try {
			String sql2 = "SELECT r.ID, r.Name FROM roles r INNER JOIN user_roles ur ON r.ID = ur.Role_ID WHERE ur.User_ID = ?";
			Jtemplate.query(sql2, new Object[] { id }, (resultSet) -> {
				int roleId = resultSet.getInt("ID");
				String roleName = resultSet.getString("Name");
				Role role = new Role(roleId, roleName);
				roles.add(role);
			});
		} catch (EmptyResultDataAccessException e) {
			exception = new CustomException(HttpStatus.NOT_FOUND.value(), "NOT_FOUND",
					"Please check your email and try again", "/getUserRoles", ZonedDateTime.now());
			throw exception;
		} catch (DataAccessException e) {
			throw exception;
		}

		return roles;
	}

}
