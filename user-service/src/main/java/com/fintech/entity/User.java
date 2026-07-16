package com.fintech.entity;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @Field(targetType = FieldType.STRING, name = "firstName")
    private String firstName;

    @Field(targetType = FieldType.STRING, name = "lastName")
    private String lastName;

    @Field(targetType = FieldType.STRING, name = "email")
    private String email;

    @Field(targetType = FieldType.STRING, name = "password")
    private String password;

    @Field(targetType = FieldType.INT64, name = "accountNumber")
    private Long accountNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }


}
