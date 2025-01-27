package com.teslo.teslo_shop.seed;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.Profile;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teslo.teslo_shop.auth.AuthService;
import com.teslo.teslo_shop.auth.dto.CreateUserDto;
import com.teslo.teslo_shop.auth.entities.User;
import com.teslo.teslo_shop.core.error.exceptions.BadRequestException;
import com.teslo.teslo_shop.product.Product;
import com.teslo.teslo_shop.product.ProductService;

@Profile("dev")
@Service
public class SeedService {

    private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
    private final AuthService authService;
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public SeedService(AuthService authService, ProductService productService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    public String populateDb() {

        String msg = "Database populated";

        this.cleanTables();

        List<Product> products = this.getJsonDataMap(new TypeReference<Map<String, List<Product>>>() {
        }).get("products");
        List<CreateUserDto> users = this.getJsonDataMap(new TypeReference<Map<String, List<CreateUserDto>>>() {
        }).get("users");
        List<User> savedUsers = this.authService.registerMultiple(users);
        this.productService.saveMultiple(products, savedUsers);

        LOGGER.log(Level.INFO, msg);
        return msg;
    }

    private void cleanTables() {
        this.authService.deleteAllUsers();
        this.productService.deleteAll();
    }

    private <T> Map<String, List<T>> getJsonDataMap(TypeReference<Map<String, List<T>>> typeReference) {

        final String file = "seed/seed.data.json";

        // Load file from /resources dir
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file);

        try {
            return this.objectMapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
