package com.example.demo.config;

import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.config.CustomUserDetails;
import com.example.demo.repository.CustomImplementation.Database;
import com.example.demo.repository.CustomImplementation.DatabaseResponse;
import com.example.demo.repository.CustomImplementation.QueryFormatter;
import com.example.demo.repository.CustomImplementation.QueryFormatters.MysqlQueryFormatter;

public class CustomUserDetailsService implements UserDetailsService {
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        DatabaseResponse response = lookupUser(username);

        if (response.getResultList() != null && !response.getResultList().isEmpty()) {
            Map<String, Object> entity = response.getResultList().getFirst();
            long id = ((Number)entity.get("id")).longValue();
            String email = (String) entity.get("email");
            String pass = (String) entity.get("password");

            return new CustomUserDetails(
                id,
                email,
                pass,
                true,
                true,
                true,
                true
            );
            
        } else {
            throw new UsernameNotFoundException("Could not find any users");
        }
    }

    private DatabaseResponse lookupUser(String email) {
        QueryFormatter queryFormatter = new MysqlQueryFormatter("Users", "email");
        Map<String, Object> properties = QueryFormatter.property("email", email);
        String sql = queryFormatter.findOne();
        return Database.executeQuery(sql, properties, null);
    }

    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }
}
