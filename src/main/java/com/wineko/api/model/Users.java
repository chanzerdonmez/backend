package com.wineko.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "users")
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Integer id;

    @Column(name ="name")
    private String name;

    @Column(name ="first_name")
    private String firstName;

    @Column(name ="email")
    private String email;

    @Column(name ="password")
    private String password;

    @Column(name ="enabled")
    private boolean enabled;

//    @Column(name ="role")
//    private String role;

    @Enumerated(EnumType.STRING)
    @Column(name ="role")
    private Role role;

    // @Temporal(TemporalType.TIMESTAMP)
    // @Column(name ="date_creation", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Column(name ="date_creation")
    private Date dateCreation;

    @Column(name ="token")
    private String token;

//    @ManyToMany(fetch = FetchType.EAGER)
//    private List<Role> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.name()));
    }

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //    return null;
    // }

    @Override
    public String getUsername() {
        return this.email;
    }

//    @Override
//    public String getUsername() {
//        return null;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
