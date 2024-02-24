package com.example.learnhub.Controller;

import com.example.learnhub.DTO.WishlistDTO;
import com.example.learnhub.Service.ServiceOfWishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final ServiceOfWishlist wishlistService;

    @Autowired
    public WishlistController(ServiceOfWishlist wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/")
    public ResponseEntity<WishlistDTO> addToWishlist(@RequestBody WishlistDTO wishlistDTO) {
        WishlistDTO addedWishlistItem = wishlistService.addToWishList(wishlistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedWishlistItem);
    }
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> deleteWishlistItem(@PathVariable Integer wishlistId) throws ChangeSetPersister.NotFoundException {
        wishlistService.deleteWishlistItem(wishlistId);
        return ResponseEntity.noContent().build();
    }
        // Other endpoints for retrieving, updating, and deleting wishlist items

}
