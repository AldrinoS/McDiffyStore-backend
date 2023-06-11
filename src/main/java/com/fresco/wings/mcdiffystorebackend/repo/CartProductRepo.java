package com.fresco.wings.mcdiffystorebackend.repo;

import com.fresco.wings.mcdiffystorebackend.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepo extends JpaRepository<CartProduct, Integer> {

    Boolean existsCartProductByProduct_ProductName(String productName);
}
