package ru.vitalykhan.voting.to;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserTo extends AbstractTo {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 5, max = 50)
    private String password;

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
