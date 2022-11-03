package com.example.demo.config;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.repository.CustomImplementation.Database;
import com.example.demo.repository.CustomImplementation.DatabaseRequest;
import com.example.demo.repository.CustomImplementation.DatabaseResponse;
import com.example.demo.repository.CustomImplementation.QueryFormatter;
import com.example.demo.repository.CustomImplementation.QueryFormatters.MysqlQueryFormatter;

/*
 * The CustomUserDetailsService class defines 
 * the behavior of loading a user trying to login.
 */
public class CustomUserDetailsService implements UserDetailsService {

    private static final String LOOKUP_TABLE = "Users"; 
    private static final String LOOKUP_COLUMN = "email"; 
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        DatabaseResponse response = lookupUser(username);
        Map<String, Object> entity = response.getFirstResult();

        if (entity == null)
            throw new UsernameNotFoundException("Could not find any users");

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
    }

    private DatabaseResponse lookupUser(String columnValue) {
        QueryFormatter queryFormatter = new MysqlQueryFormatter(LOOKUP_TABLE, LOOKUP_COLUMN);
        DatabaseRequest request = new DatabaseRequest();
        request.setProperty(LOOKUP_COLUMN, columnValue);
        request.setSql(queryFormatter.findOne());
        return Database.executeQuery(request);
    }
}
