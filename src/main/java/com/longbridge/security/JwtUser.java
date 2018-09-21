package com.longbridge.security;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.longbridge.models.User;
import javax.annotation.Generated;
import java.util.Collections;

/**
 * Created by stephan on 20.03.16.
 */
public class JwtUser implements UserDetails {
    private User user;

    private String password;

    private Collection<SimpleGrantedAuthority> authorities;

    private boolean enabled;

    private Date lastPasswordResetDate;



    private JwtUser(Builder builder) {
        this.user = builder.user;
        this.password = builder.password;
        this.authorities = builder.authorities;
        this.enabled = builder.enabled;
        this.lastPasswordResetDate = builder.lastPasswordResetDate;
    }

    @Generated("SparkTools")
    public JwtUser() {
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    /**
     * Creates builder to build {@link JwtUser}.
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }


    /**
     * Builder to build {@link JwtUser}.
     */
    public static final class Builder {
        private User user;
        private String password;
        private Collection<SimpleGrantedAuthority> authorities = Collections.emptyList();
        private boolean enabled;
        private Date lastPasswordResetDate;

        private Builder() {
        }

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withAuthorities(Collection<SimpleGrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withLastPasswordResetDate(Date lastPasswordResetDate) {
            this.lastPasswordResetDate = lastPasswordResetDate;
            return this;
        }

        public JwtUser build() {
            return new JwtUser(this);
        }
    }




}
