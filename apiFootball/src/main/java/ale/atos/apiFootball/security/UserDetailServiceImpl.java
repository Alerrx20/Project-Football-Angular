package ale.atos.apiFootball.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ale.atos.apiFootball.entity.User;
import ale.atos.apiFootball.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user =  userRepository
					.findOneByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + email + " no existe."));
		
		return new UserDetailsImpl(user);
	}
	
}
