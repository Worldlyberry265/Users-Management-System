package Sword.Group.FirstTask.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import Sword.Group.FirstTask.model.Role;
import Sword.Group.FirstTask.userDetails.UserInfoUserDetailsService;

@Component
public class JwtService {

//	public String SecureKeyGenerator() {
//
//		// Define the desired key length (in bytes)
//		int keyLength = 256;
//
//		// Generate a secure random byte array
//		BytesKeyGenerator keyGenerator = KeyGenerators.secureRandom(keyLength);
//		byte[] keyBytes = keyGenerator.generateKey();
//
//		// Convert the byte array to a hexadecimal string
//		 String secretKey;
//		 
//		 try {
//	            secretKey = new String(Hex.encode(keyBytes));
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            // Handle the exception here, such as logging or throwing a custom exception
//	            return null;
//	        }
//	        return secretKey;
//	    }
//	
	@Autowired
	UserInfoUserDetailsService service;

	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // To extract any desired claim
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		// parsing involves breaking down the token into its three parts: header,
		// payload (claims), and signature.
		// It gets the key used to generate the jwt
		// It then apply the cryptographic algorithm with the SignKey and check if it
		// matches the signature
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName, service.getUserRoles(userName));
	}

	private String createToken(Map<String, Object> claims, String userName, List<Role> authorities) {
		return Jwts.builder().setClaims(claims).setSubject(userName).claim("authorities: ", authorities)
				.setIssuedAt(new Date()) // System.currentTimeMillis()
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // for 60 mins only																
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
