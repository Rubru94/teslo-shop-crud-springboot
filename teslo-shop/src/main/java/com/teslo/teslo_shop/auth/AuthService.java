package com.teslo.teslo_shop.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authManager, UserRepository userRepository,
            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Not found user with email: " + email));
    }

    public AuthenticatedUserDto registerUser(CreateUserDto createUserDto) {
        User user = this.createUser(createUserDto);
        this.userRepository.save(user);
        return new AuthenticatedUserDto(user, this.getJwtToken(user),
                this.jwtService.getExpirationTime());
    }

    public List<User> registerMultiple(List<CreateUserDto> users) {
        return this.userRepository
                .saveAll(users.stream().map(user -> this.createUser(user)).collect(Collectors.toList()));
    }

    public AuthenticatedUserDto login(LoginUserDto loginUserDto) {
        User user = this.findUserByEmail(loginUserDto.getEmail());
        if (!this.checkPassword(loginUserDto.getPassword(), user.getPassword()))
            throw new UnauthorizedException("Credentials are not valid (password)");
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        return new AuthenticatedUserDto(user, this.getJwtToken(user),
                this.jwtService.getExpirationTime());
    }

    /**
     * User token revalidation
     */
    public AuthenticatedUserDto checkAuthStatus(User user) {
        User userSaved = this.findUserByEmail(user.getEmail());
        return new AuthenticatedUserDto(userSaved, this.getJwtToken(userSaved), this.jwtService.getExpirationTime());
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User getJwtUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public void deleteAllUsers() {
        this.userRepository.deleteAllUsers();
    }

    private String getJwtToken(User user) {
        return this.jwtService.generateToken(user);
    }

    private User createUser(CreateUserDto createUserDto) {
        User user = new User(createUserDto);
        user.setPassword(this.encodePassword(createUserDto.getPassword()));
        return user;
    }

}
