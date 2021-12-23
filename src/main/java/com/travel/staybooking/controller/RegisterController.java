package com.travel.staybooking.controller;

import com.travel.staybooking.entity.User;
import com.travel.staybooking.entity.UserRole;
import com.travel.staybooking.service.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@RestController
public class RegisterController {
    private RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }
    @PostMapping("/register/guest")
    public void addGuest(@RequestBody User user) throws UserPrincipalNotFoundException {
        registerService.add(user, UserRole.ROLE_GUEST);
    }

    @PostMapping("/register/host")
    public void addHost(@RequestBody User user) throws UserPrincipalNotFoundException {
        registerService.add(user, UserRole.ROLE_HOST);
    }


}
