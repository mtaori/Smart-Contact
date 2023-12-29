package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.entity.User;
import com.smart.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching user form database
		
		
				User user =  userRepository.getUserByUsername(username);
				
				if(user == null)
				{
					throw new UsernameNotFoundException("User not found");
				}
				/*
				 * CustomUser customUser = new CustomUser(user); return customUser;
				 */
				else {
					return new CustomUser(user);
				}
			
	
	}

}
