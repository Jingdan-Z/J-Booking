package com.travel.staybooking.service;
import com.travel.staybooking.dao.AuthorityDao;
import com.travel.staybooking.entity.Authority;
import com.travel.staybooking.entity.Token;
import com.travel.staybooking.entity.User;
import com.travel.staybooking.entity.UserRole;
import com.travel.staybooking.exception.UserNotExistException;
import com.travel.staybooking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

@Service
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private AuthorityDao authorityRepository;
    private JwtUtil jwtutil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, AuthorityDao authorityRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authorityRepository = authorityRepository;
        this.jwtutil = jwtUtil;
    }

    //authentication manager
    public Token authenticate(User user, UserRole role) throws UserNotExistException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword()));

        } catch (AuthenticationException exception) {
            throw new UserNotExistException("Error! User Doesn't Exist!!");
        }
        Authority authority = authorityRepository.findById(user.getUsername()).orElse(null);
        if (!authority.getAuthority().equals((role.name()))) {
            throw new UserNotExistException("Error! User Doesn't Exist!!");
        }
        return new Token(jwtutil.generateToken(user.getUsername()));
        }
    }
//success!! check user -> generate token -> return token
//fail!! check user -> return user not exists