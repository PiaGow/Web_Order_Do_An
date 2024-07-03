package JAVAC5.com.WebOrderDoAn.Services;

import JAVAC5.com.WebOrderDoAn.Entities.User;
import JAVAC5.com.WebOrderDoAn.Repositories.IRoleRepository;
import JAVAC5.com.WebOrderDoAn.Repositories.IUserRepository;
import JAVAC5.com.WebOrderDoAn.constants.Provider;
import JAVAC5.com.WebOrderDoAn.constants.Role;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder()
                .encode(user.getPassword()));
        userRepository.save(user);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE,
            rollbackFor = {Exception.class, Throwable.class})
    public void setDefaultRole(String username){
        userRepository.findByUsername(username).getRoles()
                .add(roleRepository.findRoleById(Role.USER.value));
    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
    public User findByUsername(String username) throws
            UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
    @Transactional
    public void saveOauthUser(String email, @NotNull String username) {
        if(userRepository.findByUsername(username) != null)
            return;
        if(userRepository.findByEmail(email) != null)
            return;
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        user.setProvider(Provider.GOOGLE.value);
        user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

}