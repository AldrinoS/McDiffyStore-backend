package com.fresco.wings.mcdiffystorebackend.controller;

import com.fresco.wings.mcdiffystorebackend.exception.InvalidCredentialException;
import com.fresco.wings.mcdiffystorebackend.exception.ResultNotFoundException;
import com.fresco.wings.mcdiffystorebackend.model.Cart;
import com.fresco.wings.mcdiffystorebackend.model.CartProduct;
import com.fresco.wings.mcdiffystorebackend.model.Product;
import com.fresco.wings.mcdiffystorebackend.repo.CartProductRepo;
import com.fresco.wings.mcdiffystorebackend.repo.CartRepo;
import com.fresco.wings.mcdiffystorebackend.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.config.Elements.JWT;

@RestController
@RequestMapping("/api/auth/consumer")
public class ConsumerController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartProductRepo cartProductRepo;

    @GetMapping("/cart")
    public ResponseEntity<Cart> getCartDetails() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Cart> optionalCart = cartRepo.findByUser_Username(username);

        if(optionalCart.isPresent()) {
            return new ResponseEntity<>(optionalCart.get(), HttpStatus.OK);
        }

        throw new ResultNotFoundException("User does not have any cart assigned");
    }

    @PostMapping("/cart")
    public ResponseEntity<?> addProduct(@RequestBody Product request) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Cart> optionalCart = cartRepo.findByUser_Username(username);

        if(optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            if(cartProductRepo.existsCartProductByProduct_ProductName(request.getProductName())) {
                return new ResponseEntity<>("Item exist", HttpStatus.CONFLICT);
            }

            CartProduct cartProduct = new CartProduct(cart, request, 1);
            cart.getCartProducts().add(cartProduct);

            cartRepo.save(cart);

            return new ResponseEntity<>("Product added to cart", HttpStatus.OK);
        }

        throw new Exception("Item not added");
    }

    @PutMapping("/cart")
    public ResponseEntity<?> updateCartProduct(@RequestBody CartProduct request) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Cart> optionalCart = cartRepo.findByUser_Username(username);

        if(optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            if (cartProductRepo.existsCartProductByProduct_ProductName(request.getProduct().getProductName())) {
                List<CartProduct> result = cart.getCartProducts().stream().map(cartProduct -> {
                    if (cartProduct.getProduct().getProductName().equalsIgnoreCase(request.getProduct().getProductName())) {
                        cartProduct.setQuantity(request.getQuantity());
                    }
                    return cartProduct;
                }).filter(cartProduct -> cartProduct.getQuantity()>0)
                        .collect(Collectors.toList());
                cart.setCartProducts(result);

                cartRepo.save(cart);

                return new ResponseEntity<>("Updated Quantity", HttpStatus.OK);
            }

            CartProduct cartProduct = new CartProduct(cart, request.getProduct(), request.getQuantity());
            cart.getCartProducts().add(cartProduct);

            cartRepo.save(cart);

            return new ResponseEntity<>("Product added to cart", HttpStatus.OK);
        }

        throw new Exception("Item not added");
    }

    @DeleteMapping("/cart")
    public ResponseEntity<?> deleteCartProduct(@RequestBody Product request) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Cart> optionalCart = cartRepo.findByUser_Username(username);

        if(optionalCart.isPresent()) {
            Cart cart = optionalCart.get();

            if (cartProductRepo.existsCartProductByProduct_ProductName(request.getProductName())) {
                List<CartProduct> result = cart.getCartProducts().stream().filter(cartProduct -> !cartProduct.getProduct().getProductName().equalsIgnoreCase(request.getProductName()))
                        .collect(Collectors.toList());
                cart.setCartProducts(result);

                cartRepo.save(cart);

                return new ResponseEntity<>("Item Deleted", HttpStatus.OK);
            }

            return new ResponseEntity<>("Item not found", HttpStatus.OK);
        }

        throw new Exception("Action not possible");
    }


}
