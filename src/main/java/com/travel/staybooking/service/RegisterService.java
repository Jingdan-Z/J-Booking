package com.travel.staybooking.service;
import com.travel.staybooking.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.travel.staybooking.dao.AuthorityDao;
import com.travel.staybooking.dao.UserDao;
import com.travel.staybooking.entity.Authority;
import com.travel.staybooking.entity.User;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.nio.file.attribute.UserPrincipalNotFoundException;

@Service
public class RegisterService {
    private UserDao userRepository;
    private AuthorityDao authorityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserDao userRepository, AuthorityDao authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(User user, UserRole role) throws UserPrincipalNotFoundException {
        if (userRepository.existsById(user.getUsername())) {
            throw new UserPrincipalNotFoundException("Attention! This user already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true); //can use to identify that the user account is activated => can also use 2sv for activation
        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));
    }


}
