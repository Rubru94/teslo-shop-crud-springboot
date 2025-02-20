package com.teslo.teslo_shop.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.auth.dto.AuthenticatedUserDto;
import com.teslo.teslo_shop.auth.dto.CreateUserDto;
import com.teslo.teslo_shop.auth.dto.LoginUserDto;
import com.teslo.teslo_shop.auth.entities.User;
import com.teslo.teslo_shop.config.SecurityConfiguration;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationProvider authenticationProvider;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setEmail("example@mail.com");
        CreateUserDto createUserDto = new CreateUserDto(user);
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(user);
        when(authService.registerUser(any(CreateUserDto.class))).thenReturn(authenticatedUserDto);

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto.getUser()))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(authenticatedUserDto.getId()))
                .andExpect(jsonPath("$.email").value(authenticatedUserDto.getEmail()));

        verify(authService, times(1)).registerUser(any(CreateUserDto.class));
    }

    @Test
    public void testLogin() throws Exception {
        User user = new User();
        user.setEmail("example@mail.com");
        user.setPassword("password");
        LoginUserDto loginUserDto = new LoginUserDto(user);
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(user);
        when(authService.login(any(LoginUserDto.class))).thenReturn(authenticatedUserDto);

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDto.getUser()))).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authenticatedUserDto.getId()))
                .andExpect(jsonPath("$.email").value(authenticatedUserDto.getEmail()));

        verify(authService, times(1)).login(any(LoginUserDto.class));
    }

    @Test
    public void testCheckAuthStatus() throws Exception {
        User user = new User();
        user.setEmail("example@mail.com");
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(user);
        when(authService.getJwtUser()).thenReturn(user);
        when(authService.checkAuthStatus(any(User.class))).thenReturn(authenticatedUserDto);

        mockMvc.perform(get("/api/auth/check-status").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(authenticatedUserDto.getId()))
                .andExpect(jsonPath("$.email").value(authenticatedUserDto.getEmail()));

        verify(authService, times(1)).getJwtUser();
        verify(authService, times(1)).checkAuthStatus(any(User.class));
    }

}
