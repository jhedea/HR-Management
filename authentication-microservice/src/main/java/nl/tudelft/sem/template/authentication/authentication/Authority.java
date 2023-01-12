package nl.tudelft.sem.template.authentication.authentication;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
    private final transient String authorityName;
    static final long serialVersionUID = 42L;

    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String getAuthority() {
        return authorityName;
    }
}
