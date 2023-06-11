package com.fresco.wings.mcdiffystorebackend.repo;

import com.fresco.wings.mcdiffystorebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    Optional<List<Product>> findByProductNameContainingIgnoreCaseOrCategory_CategoryName(String productName, String categoryName);

    Optional<List<Product>> findAllBySeller_Username(String username);

    Boolean existsByProductIdAndSeller_UserId(Integer productId, Integer userId);
}
