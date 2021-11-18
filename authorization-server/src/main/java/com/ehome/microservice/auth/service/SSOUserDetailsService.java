package com.ehome.microservice.auth.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("ssoUserDetailsService")
public class SSOUserDetailsService implements UserDetailsService {
	@Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String user="user";
        if( !user.equals(s) ) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new User( s, passwordEncoder.encode("123456"), 
              AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}