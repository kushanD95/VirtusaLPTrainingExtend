package lk.harvestsellingsystem.sellcloud.auth.authserver.service;

import lk.harvestsellingsystem.sellcloud.auth.authserver.model.AuthUserDetail;
import lk.harvestsellingsystem.sellcloud.auth.authserver.model.User;
import lk.harvestsellingsystem.sellcloud.auth.authserver.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userDetailRepository.findByUsername(username);

        optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username or Password wrong"));

        UserDetails userDetails = new AuthUserDetail(optionalUser.get());

        new AccountStatusUserDetailsChecker().check(userDetails);

        return userDetails;
    }

    public User save(User user) {
        return userDetailRepository.save(user);
    }
}
