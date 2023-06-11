package com.fresco.wings.mcdiffystorebackend.repo;

import com.fresco.wings.mcdiffystorebackend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUser_Username(String username);

}
