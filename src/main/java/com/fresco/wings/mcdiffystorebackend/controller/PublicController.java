package com.fresco.wings.mcdiffystorebackend.controller;

import com.fresco.wings.mcdiffystorebackend.config.JwtUtils;
import com.fresco.wings.mcdiffystorebackend.exception.InvalidCredentialException;
import com.fresco.wings.mcdiffystorebackend.exception.ResultNotFoundException;
import com.fresco.wings.mcdiffystorebackend.model.JwtRequest;
import com.fresco.wings.mcdiffystorebackend.model.Product;
import com.fresco.wings.mcdiffystorebackend.model.User;
import com.fresco.wings.mcdiffystorebackend.repo.ProductRepo;
import com.fresco.wings.mcdiffystorebackend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.PrimitiveIterator;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> getMatchingProduct(@RequestParam String keyword) throws Exception {
        Optional<List<Product>> results = productRepo.findByProductNameContainingIgnoreCaseOrCategory_CategoryName(keyword, keyword);

        if(results.isPresent() && results.get().size()>0) {
            return new ResponseEntity<>(results.get(), HttpStatus.OK);
        }

        throw new ResultNotFoundException("No results for keyword "+ keyword+". Try another keyword");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody JwtRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            throw new InvalidCredentialException("Invalid Credentials!!", e);
        }

        Optional<User> optionalUser = userRepo.findByUsername(request.getUsername());

        if (optionalUser.isPresent()) {
            String token = jwtUtils.generateToken(optionalUser.get());
            return new ResponseEntity<>(token, HttpStatus.OK);
        }

        throw new ResultNotFoundException("User not found in DB");
    }

    @GetMapping("/product/all")
    public ResponseEntity<List<Product>> getAllProduct() {
        return new ResponseEntity<>(productRepo.findAll(), HttpStatus.OK);
    }
}
