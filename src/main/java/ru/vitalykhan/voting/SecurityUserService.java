package ru.vitalykhan.voting;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.vitalykhan.voting.model.User;
import ru.vitalykhan.voting.repository.UserRepository;

@Service("securityUserService")
public class SecurityUserService implements UserDetailsService {

    private UserRepository userRepository;

    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthenticatedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthenticatedUser(user);
    }
}
