package com.teslo.teslo_shop.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.teslo.teslo_shop.auth.dto.AuthenticatedUserDto;
import com.teslo.teslo_shop.auth.dto.CreateUserDto;
import com.teslo.teslo_shop.auth.dto.LoginUserDto;
import com.teslo.teslo_shop.auth.entities.User;
import com.teslo.teslo_shop.auth.enums.ValidRoles;
import com.teslo.teslo_shop.core.error.exceptions.AuthorizationDeniedException;
import com.teslo.teslo_shop.core.error.exceptions.NotFoundException;
import com.teslo.teslo_shop.core.error.exceptions.UnauthorizedException;
import com.teslo.teslo_shop.core.utils.SecurityUtil;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testFindUserByEmail_ShouldReturnUser() {
        String email = "example@mail.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = authService.findUserByEmail(email);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindUserByEmail_ShouldThrowNotFoundExceptionWhenUserNotFound() {
        String email = "example@mail.com";
        String expectedMessage = "Not found user with email: " + email;

        Exception exception = assertThrows(NotFoundException.class, () -> {
            authService.findUserByEmail(email);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRegisterUser_ShouldReturnAuthenticatedUserDto() {
        String encodedPassword = "$2a$10$4yDFOLw";
        String token = "eyJhbGciOi...";
        Long expirationTime = 5000L;
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("example@mail.com");
        createUserDto.setPassword("password");
        createUserDto.setFullName("John Doe");
        User user = new User(createUserDto);
        user.setPassword(encodedPassword);
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(user, token, expirationTime);

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        // ensure that save will return same user that was passed as argument.
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(expirationTime);

        AuthenticatedUserDto result = authService.registerUser(createUserDto);

        assertNotNull(result);
        assertEquals(authenticatedUserDto.getEmail(), result.getEmail());
        verify(passwordEncoder, times(1)).encode(createUserDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(jwtService, times(1)).getExpirationTime();
    }

    @Test
    public void testRegisterMultiple_ShouldReturnUsers() {
        String encodedPassword = "$2a$10$4yDFOLw";
        CreateUserDto createUserDtoOne = new CreateUserDto();
        createUserDtoOne.setEmail("one@mail.com");
        createUserDtoOne.setPassword("password");
        createUserDtoOne.setFullName("John Doe");
        CreateUserDto createUserDtoTwo = new CreateUserDto();
        createUserDtoTwo.setEmail("two@mail.com");
        createUserDtoTwo.setPassword("password");
        createUserDtoTwo.setFullName("Elena Rodriguez");
        User userOne = new User(createUserDtoOne);
        userOne.setPassword(encodedPassword);
        User userTwo = new User(createUserDtoTwo);
        userTwo.setPassword(encodedPassword);
        List<User> users = List.of(userOne, userTwo);
        List<CreateUserDto> createUserDtos = List.of(createUserDtoOne, createUserDtoTwo);

        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> encodedPassword);
        when(userRepository.saveAll(anyList())).thenReturn(users);

        List<User> result = this.authService.registerMultiple(createUserDtos);

        assertNotNull(result);
        assertEquals(users.size(), result.size());
        assertEquals(encodedPassword, result.get(0).getPassword());
        assertEquals(encodedPassword, result.get(1).getPassword());
        verify(passwordEncoder, times(users.size())).encode(anyString());
        verify(userRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testLogin_ShouldReturnAuthenticatedUserDto() {
        String token = "eyJhbGciOi...";
        Long expirationTime = 5000L;
        User user = new User();
        user.setEmail("example@mail.com");
        user.setPassword("$2a$10$4yDFOLw");
        LoginUserDto loginUserDto = new LoginUserDto(user);
        loginUserDto.setPassword("password");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(),
                loginUserDto.getPassword());
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(user, token, expirationTime);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.TRUE);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authToken);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(expirationTime);

        AuthenticatedUserDto result = authService.login(loginUserDto);

        assertNotNull(result);
        assertEquals(authenticatedUserDto.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(jwtService, times(1)).getExpirationTime();
    }

    @Test
    public void testLogin_ShouldThrowUnauthorizedExceptionWhenPasswordIsInvalid() {
        String expectedMessage = "Credentials are not valid (password)";
        User user = new User();
        user.setEmail("example@mail.com");
        user.setPassword("$2a$10$4yDFOLw");
        LoginUserDto loginUserDto = new LoginUserDto(user);
        loginUserDto.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(Boolean.FALSE);

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            authService.login(loginUserDto);
        });

        String actualMessage = exception.getMessage();
        verify(userRepository, times(1)).findByEmail(anyString());
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCheckAuthStatus_ShouldReturnAuthenticatedUserDto() {
        String token = "eyJhbGciOi...";
        Long expirationTime = 5000L;
        User user = new User();
        user.setEmail("example@mail.com");
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(user, token, expirationTime);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(expirationTime);

        AuthenticatedUserDto result = authService.checkAuthStatus(user);

        assertNotNull(result);
        assertEquals(authenticatedUserDto.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(jwtService, times(1)).getExpirationTime();
    }

    @Test
    public void testGetJwtUser_ShouldReturnUser() {
        User user = new User();
        user.setEmail("example@mail.com");
        user.setPassword("password");

        try (MockedStatic<SecurityUtil> util = mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getPrincipal).thenReturn(user);

            User result = authService.getJwtUser();

            assertEquals(user, result);
        }
    }

    @Test
    public void testGetJwtUser_ShouldThrowUnauthorizedExceptionWhenJwtTokenIsInvalid() {
        String expectedMessage = "It has not been possible to obtain the user from jwt token";

        try (MockedStatic<SecurityUtil> util = mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getPrincipal).thenThrow(new UnauthorizedException("UnauthorizedException"));

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                authService.getJwtUser();
            });

            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testVerifyRoles_ShouldNotReturnIfUserHasValidRole() {
        List<String> roles = Arrays.asList(ValidRoles.USER.getAuthority());

        try (MockedStatic<SecurityUtil> utilities = mockStatic(SecurityUtil.class)) {
            utilities.when(() -> SecurityUtil.hasAnyRole(roles)).thenReturn(Boolean.TRUE);

            authService.verifyRoles(ValidRoles.USER);
        }
    }

    @Test
    public void testVerifyRoles_ShouldThrowAuthorizationDeniedExceptionWhenUserNotHasValidRole() {
        List<String> roles = Arrays.asList(ValidRoles.ADMIN.getAuthority());

        try (MockedStatic<SecurityUtil> utilities = mockStatic(SecurityUtil.class)) {
            utilities.when(() -> SecurityUtil.hasAnyRole(roles)).thenReturn(Boolean.FALSE);

            assertThrows(AuthorizationDeniedException.class, () -> {
                authService.verifyRoles(ValidRoles.ADMIN);
            });
        }
    }

    @Test
    public void testDeleteAllUsers_ShouldDeleteAllUsers() {
        doNothing().when(userRepository).deleteAllUsers();

        authService.deleteAllUsers();

        verify(userRepository, times(1)).deleteAllUsers();
    }
}
