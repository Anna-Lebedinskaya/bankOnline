package com.bankproject.service.userdetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
public class CustomUserDetails
        implements UserDetails {

    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String username;
    private final Integer id;
    private final Boolean enabled;
    private final Boolean accountNotExpired;
    private final Boolean accountNotLocked;
    private final Boolean credentialsNonExpired;

    public CustomUserDetails(final Integer id,
                             final String username,
                             final String password,
                             final Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNotExpired = true;
        this.accountNotLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Integer getUserId() {
        return id;
    }

//    @Override
//    public String toString() {
//        return "{\"user_id\":\"" + id + "\"," +
//                "\"username\":\"" + username + "\"," +
//                "\"user_role\":\"" + authorities + "\"," +
//                "\"password\":\"" + password + "\"}";    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(getFieldsToInclude());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();

    }

    private Object getFieldsToInclude() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("user_id", id);
        fields.put("username", username);
        fields.put("user_role", authorities);
        fields.put("password", password);
        return fields;
    }

}
