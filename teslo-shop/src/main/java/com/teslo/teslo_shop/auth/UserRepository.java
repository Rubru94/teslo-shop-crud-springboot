package com.teslo.teslo_shop.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.teslo.teslo_shop.auth.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM User")
    void deleteAllUsers();
}