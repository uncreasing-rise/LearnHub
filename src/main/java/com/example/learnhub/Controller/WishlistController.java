package com.example.learnhub.Controller;

import com.example.learnhub.DTO.WishlistDTO;
import com.example.learnhub.Service.ServiceOfWishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/wishlist")
public class WishlistController {

    private final ServiceOfWishlist wishlistService;

    @Autowired
    public WishlistController(ServiceOfWishlist wishlistService) {
        this.wishlistService = wishlistService;
    }

    // Endpoint to add a course to the wishlist
    @PostMapping("/add")
    public ResponseEntity<WishlistDTO> addToWishlist(@RequestBody WishlistDTO wishlistDTO) {
        WishlistDTO addedWishlistItem = wishlistService.addToWishList(wishlistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedWishlistItem);
    }

    // Endpoint to delete a course from the wishlist by wishlistId
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> deleteWishlistItem(@PathVariable Integer wishlistId) {
        wishlistService.deleteWishlistItem(wishlistId);
        return ResponseEntity.noContent().build();
    }

    // Other endpoints for retrieving, updating, and deleting wishlist items
}
