package ru.vitalykhan.voting.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.time.Instant;

//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@NamedQueries({
//        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id"),
//        @NamedQuery(name = User.BY_EMAIL, query = "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=?1"),
//        @NamedQuery(name = User.ALL_SORTED, query = "SELECT u FROM User u ORDER BY u.name, u.email"),
//})
@Entity
public class User extends AbstractNamedEntity {

//    public static final String DELETE = "User.delete";
//    public static final String BY_EMAIL = "User.getByEmail";
//    public static final String ALL_SORTED = "User.getAllSorted";


    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 5, max = 68)  //Max number must cover 68 symbols of bcrypt encoding
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @NotNull
    @PastOrPresent
    private Instant registered = Instant.now();

    private boolean enabled = true;

//    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    @Enumerated(EnumType.STRING)
//    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
//    @Column(name = "role")
//    @ElementCollection(fetch = FetchType.EAGER)
//    // @Fetch(FetchMode.SUBSELECT)
//    @BatchSize(size = 200)
//    private Set<Role> roles;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
//    @OrderBy("dateTime DESC")
//    protected List<Meal> meals;

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