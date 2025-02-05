package com.teslo.teslo_shop.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

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
import com.teslo.teslo_shop.auth.AuthService;
import com.teslo.teslo_shop.auth.JwtService;
import com.teslo.teslo_shop.auth.enums.ValidRoles;
import com.teslo.teslo_shop.config.SecurityConfiguration;
import com.teslo.teslo_shop.core.dto.PaginationDto;
import com.teslo.teslo_shop.product.dto.PlainProductDto;

@WebMvcTest(ProductController.class)
@Import(SecurityConfiguration.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

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
    public void testFindAll() throws Exception {
        when(productService.findAll(any(PaginationDto.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // also .is(200) is valid
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService, times(1)).findAll(any(PaginationDto.class));
    }

    @Test
    public void testFindOne() throws Exception {
        String term = "product-slug";
        PlainProductDto product = new PlainProductDto();
        when(productService.findOne(term)).thenReturn(product);

        mockMvc.perform(get("/api/products/{term}", term)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.title").value(product.getTitle()));

        verify(productService, times(1)).findOne(term);
    }

    @Test
    public void testCreate() throws Exception {
        PlainProductDto product = new PlainProductDto();
        when(productService.save(any(PlainProductDto.class), any())).thenReturn(product);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.title").value(product.getTitle()));

        verify(authService, times(1)).verifyRoles(ValidRoles.ADMIN, ValidRoles.SUPER_USER);
        verify(productService, times(1)).save(any(PlainProductDto.class), any());
    }

    @Test
    public void testUpdate() throws Exception {
        String productId = "1";
        PlainProductDto product = new PlainProductDto();
        when(productService.update(eq(productId), any(PlainProductDto.class))).thenReturn(product);

        mockMvc.perform(patch("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.title").value(product.getTitle()));

        verify(authService, times(1)).verifyRoles(ValidRoles.ADMIN, ValidRoles.SUPER_USER);
        verify(productService, times(1)).update(eq(productId), any(PlainProductDto.class));
    }

    @Test
    public void testDelete() throws Exception {
        String productId = "1";
        PlainProductDto product = new PlainProductDto();
        when(productService.delete(productId)).thenReturn(product);

        mockMvc.perform(delete("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.title").value(product.getTitle()));

        verify(authService, times(1)).verifyRoles(ValidRoles.ADMIN, ValidRoles.SUPER_USER);
        verify(productService, times(1)).delete(productId);
    }
}
