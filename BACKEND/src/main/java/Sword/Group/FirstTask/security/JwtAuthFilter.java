//import Sword.Group.FirstTask.dao.UsersDAO;
//import Sword.Group.FirstTask.userDetails.UserInfoUserDetails;
//import Sword.Group.FirstTask.userDetails.UserInfoUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.aspectj.weaver.patterns.IToken;
//import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
////import org.springframework.core.Ordered;
//
//
//import java.io.IOException;
//
////@Order(Ordered.HIGHEST_PRECEDENCE)
//@Component
//public class JwtAuthFilter extends OncePerRequestFilter { //So we can apply the filter just once to every request
//	
////	private UsersDAO eDAO;
//
//    @Autowired
//    private JwtService jwtService;
//
////    @Autowired
////    private UserInfoUserDetailsService userDetailsService;
//
//    //This filter will be applied to every Http request that arrives
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String username = null;
//        if (authHeader != null && authHeader.startsWith("Bearer ")) { //If it's not a public API, and it actually does have a authorization header and a JWT token
//            token = authHeader.substring(7);
//            username = jwtService.extractUsername(token);
//        }
//        
//        // ASK MORE ABOUT THIS! How frequently is the user authenticated? Every request at the browser until u close the browser? or till the jwt expires or what?
//        
//        //HERE I USED JPA
//        //I only validate the jwt if the authentication is null, because if it's valid already then it's also authenticated.
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // no other user is currently authenticated or being authenticated
//           //it also checks if the user is already authenticated, so it doesn't waste time re-authenticating him/her
//        	UserDetails userDetails = UserDetailsService.loadUserByUsername(username);
////        	UserInfoUserDetails userDetails = (UserInfoUserDetails) userDetailsService.loadUserByUsername(username);
//            if (jwtService.validateToken(token, userDetails)) {
////            	userDetails.setAuthorities(eDAO.getUserRoles(username));
////                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
//
//                //To update the authentication at SecurityContextHolder
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//                // This marks the user as authenticated for the current request, allowing them to access protected resources.
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
package Sword.Group.FirstTask.security;


import Sword.Group.FirstTask.model.Role;
import Sword.Group.FirstTask.userDetails.UserInfoUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.core.Ordered;


import java.io.IOException;
import java.util.Collection;
import java.util.List;

//@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class JwtAuthFilter extends OncePerRequestFilter { //So we can apply the filter just once to every request

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserInfoUserDetailsService userDetailsService;

    //This filter will be applied to every Http request that arrives
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) { //If it's not a public API, and it actually does have a authorization header and a JWT token
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        
        // ASK MORE ABOUT THIS! How frequently is the user authenticated? Every request at the browser until u close the browser? or till the jwt expires or what?
        
        //HERE I USED JPA
        //I only validate the jwt if the authentication is null, because if it's valid already then it's also authenticated.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // no other user is currently authenticated or being authenticated
           //it also checks if the user is already authenticated, so it doesn't waste time re-authenticating him/her
        	UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        	userDetails.set userDetailsService.getUserRoles(username);
        	Collection<? extends GrantedAuthority> auth = userDetails.getAuthorities();
//        	System.out.println("ABOUT TO STUCCK");
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, auth);
//               System.out.println("IM STUCK HERE");
                //To update the authentication at SecurityContextHolder
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // This marks the user as authenticated for the current request, allowing them to access protected resources.
            }
        }
        filterChain.doFilter(request, response);
    }
}