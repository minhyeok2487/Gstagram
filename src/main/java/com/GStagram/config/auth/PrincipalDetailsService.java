package com.GStagram.config.auth;


import com.GStagram.domain.user.User;
import com.GStagram.domain.user.UserRepository;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final HttpSession session;
	// 1. 패스워드는 알아서 체킹하니까 신경쓸 필요 없다.
	// 2. 리턴이 잘되면 자동으로 세션을 만든다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			return null;
		} else {
			session.setAttribute("userId", userEntity.getId());
			return new PrincipalDetails(userEntity);
		}
	}
}
