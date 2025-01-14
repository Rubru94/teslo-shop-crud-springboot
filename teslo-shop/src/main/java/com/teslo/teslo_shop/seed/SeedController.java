package com.teslo.teslo_shop.seed;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/api/seed")
public class SeedController {

    private final SeedService service;

    public SeedController(SeedService service) {
        this.service = service;
    }

    @PostMapping()
    public String populateDb() {
        return this.service.populateDb();
    }

}
