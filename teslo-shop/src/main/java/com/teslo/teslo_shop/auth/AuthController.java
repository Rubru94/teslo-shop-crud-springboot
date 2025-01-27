package com.teslo.teslo_shop.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.teslo.teslo_shop.auth.dto.AuthenticatedUserDto;
import com.teslo.teslo_shop.auth.dto.CreateUserDto;
import com.teslo.teslo_shop.auth.dto.LoginUserDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticatedUserDto registerUser(@RequestBody CreateUserDto entity) {
        return this.service.registerUser(entity);
    }

    @PostMapping("login")
    public AuthenticatedUserDto login(@RequestBody LoginUserDto entity) {
        return this.service.login(entity);
    }

    @GetMapping("check-status")
    public AuthenticatedUserDto checkAuthStatus() {
        return this.service.checkAuthStatus(this.service.getJwtUser());
    }

}
