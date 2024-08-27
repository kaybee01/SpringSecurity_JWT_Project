package org.scaler.securityjwt.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
public class User extends BaseModel  implements UserDetails {

    //private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    @ManyToMany
    private List<Role> roles;
    private Boolean isverified;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
