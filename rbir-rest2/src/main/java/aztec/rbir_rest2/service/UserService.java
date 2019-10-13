package aztec.rbir_rest2.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import aztec.rbir_database.Entities.User;
import aztec.rbir_database.Entities.UserRole;
import aztec.rbir_database.service.UserDataService;

@Component
public class UserService implements UserDetailsService {

	
    @Autowired
    private UserDataService userDataService;
	
	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		//userData =  new UserDataService();
		User user = userDataService.retrieveFromUserName(arg0);
		
		List<GrantedAuthority> authorities = new ArrayList<>();

		//long userId = user.getUserId();
		
		List<UserRole> userRoles = UserDataService.getAllUserRoles(user);
		
		for(UserRole userRole:userRoles){
			authorities.add(new SimpleGrantedAuthority(userRole.getRole().getRoleName()));
		}

		UserDetails userDetails1 = new org.springframework.security.core.userdetails.
                User(user.getUsername(),user.getPassword(), authorities);
		

		
		return userDetails1;
	}

}
