package com.teslo.teslo_shop.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teslo.teslo_shop.auth.dto.AuthenticatedUserDto;
import com.teslo.teslo_shop.auth.dto.CreateUserDto;
import com.teslo.teslo_shop.auth.dto.LoginUserDto;
import com.teslo.teslo_shop.auth.entities.User;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.core.error.exceptions.UnauthorizedException;

@Transactional
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Not found user with email: " + email));
    }

    public AuthenticatedUserDto registerUser(CreateUserDto createUserDto) {
        User user = new User(createUserDto);
        user.setPassword(this.encodePassword(createUserDto.getPassword()));
        this.userRepository.save(user);
        return new AuthenticatedUserDto(user);
    }

    public AuthenticatedUserDto login(LoginUserDto loginUserDto) {
        User user = this.findUserByEmail(loginUserDto.getEmail());
        if (!this.checkPassword(loginUserDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credentials are not valid (password)");
        }
        return new AuthenticatedUserDto(user);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
