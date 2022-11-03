package com.example.demo.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/*
 * The CustomUserDetails class defines 
 * the authorized user entity.
 */
public class CustomUserDetails implements UserDetails {

    /*
     * A prefix that should be inserted before a role
     * to ensure  methods such as hasRole(role) 
     * works in the front end.
     */
    private static final String PREFIX_F = "ROLE_%s"; 

    /*
     * Id
     */
    protected long id;

    /*
     * Credentials
     */
    private String email;
    private String password;

    /*
     * User states
     */
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    /*
     * User constructor
     */
    public CustomUserDetails(
        long id, 
        String email, 
        String password, 
        Boolean enabled, 
        Boolean accountNonExpired, 
        Boolean accountNonLocked,
        Boolean credentialsNonExpired) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /*
     * Returns the user's id.
     */
    public long getId() {
        return id;
    }

    /*
     * Returns the user's email.
     */
    public String getEmail() {
        return email;
    }

    /*
     * Returns the user's roles/authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        String role = String.format(PREFIX_F, SecurityConfiguration.AUTHORIZED_ROLE);
        list.add(new SimpleGrantedAuthority(role));
        return list;
    }

    /*
     * Returns the password used to authenticated
     * the user.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /*
     * Returns the name used to authenticated
     * the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /*
     * Is the user's account not expired?
     * 
     * true = not expired
     * false = expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /*
     * Is the user's account not locked?
     * 
     * true = unlocked
     * false = locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /*
     * Is the user's credentials not expired?
     * 
     * true = not expired
     * false = expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /*
     * Is the user's credentials not expired?
     * 
     * true = enabled
     * false = disabled
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /*
     * Returns the current authenticated user.
     */
    public static CustomUserDetails AuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }

        return null;
    }
}
