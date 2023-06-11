package com.fresco.wings.mcdiffystorebackend.controller;

import com.fresco.wings.mcdiffystorebackend.exception.ResultNotFoundException;
import com.fresco.wings.mcdiffystorebackend.model.Cart;
import com.fresco.wings.mcdiffystorebackend.model.Product;
import com.fresco.wings.mcdiffystorebackend.model.User;
import com.fresco.wings.mcdiffystorebackend.repo.ProductRepo;
import com.fresco.wings.mcdiffystorebackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/seller")
public class SellerController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/product")
    public ResponseEntity<?> getSellerProducts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<List<Product>> optionalCart = productRepo.findAllBySeller_Username(username);

        if(optionalCart.isPresent()) {
            return new ResponseEntity<>(optionalCart.get(), HttpStatus.OK);
        }

        throw new ResultNotFoundException("User does not have any cart assigned");
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Integer id) {
        Optional<Product> optionalCart = productRepo.findById(id);

        if(optionalCart.isPresent()) {
            return new ResponseEntity<>(optionalCart.get(), HttpStatus.OK);
        }

        throw new ResultNotFoundException("User does not have any cart assigned");
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestBody Product request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepo.findByUsername(username);

        optionalUser.ifPresent(request::setSeller);
        productRepo.save(request);

        return new ResponseEntity<>("http://localhost:8000/api/auth/seller/product/"+request.getProductId(), HttpStatus.CREATED);

//        throw new ResultNotFoundException("User does not have any cart assigned");
    }

    @PutMapping("/product")
    public ResponseEntity<?> updateProduct(@RequestBody Product request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepo.findByUsername(username);
        Optional<Product> product = productRepo.findById(request.getProductId());

        if(product.isPresent()) {
            optionalUser.ifPresent(request::setSeller);
            Product current = product.get();
            current.setProductName(request.getProductName());
            current.setCategory(request.getCategory());
            current.setPrice(request.getPrice());
            productRepo.save(current);
        } else {
            productRepo.save(request);
        }


        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") Integer id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if(productRepo.existsByProductIdAndSeller_UserId(id, optionalUser.get().getUserId())) {
            productRepo.deleteById(id);
            return new ResponseEntity<>("", HttpStatus.OK);
        }

        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);

    }

}
