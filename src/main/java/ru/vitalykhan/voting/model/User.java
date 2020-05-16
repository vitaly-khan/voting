package ru.vitalykhan.voting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.Instant;

@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class User extends AbstractNamedEntity {

    private static final int MAX_EMAIL_LENGTH = 100;
    private static final int MIN_PASSWORD_LENGTH = 5;
    private static final int MAX_PASSWORD_LENGTH = 68; //Max number must cover 68 symbols of bcrypt encoding

    @Email
    @Size(max = MAX_EMAIL_LENGTH)
    private String email;

    @NotBlank
    @Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @NotNull
    @PastOrPresent
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant registered = Instant.now();

    private boolean enabled = true;

    public User() {
    }

    public User(Integer id, String name, String email, String password, Role role, Instant registered, boolean enabled) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.role = role;
        this.registered = registered;
        this.enabled = enabled;
    }

    public User(User u) {
        this(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.getRole(), u.getRegistered(), u.isEnabled());
    }

    //For UserTestData class
    public User(Integer id, String name, String email, String password, Role role) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.role = role;
        this.registered = Instant.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getRegistered() {
        return registered;
    }

    public void setRegistered(Instant registered) {
        this.registered = registered;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", registered=" + registered +
                ", enabled=" + enabled +
                '}';
    }
}