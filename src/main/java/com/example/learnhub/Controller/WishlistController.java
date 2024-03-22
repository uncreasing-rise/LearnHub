package com.example.learnhub.Controller;

import com.example.learnhub.DTO.WishlistDTO;
import com.example.learnhub.Entity.Wishlist;
import com.example.learnhub.Service.ServiceOfWishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
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
    @GetMapping("/check/{courseId}/{userId}")
    public ResponseEntity<Boolean> checkIfInWishlist(@PathVariable Integer courseId, @PathVariable Integer userId) {
        boolean isInWishlist = wishlistService.checkIfInWishlist(courseId, userId);
        return ResponseEntity.ok(isInWishlist);
    }    // Endpoint to delete a course from the wishlist by wishlistId
    @DeleteMapping("/delete/{courseId}/{userId}")
    public ResponseEntity<Void> deleteWishlistItem(@PathVariable Integer courseId, @PathVariable Integer userId) {
        wishlistService.deleteWishlistItemByCourseIdAndUserId(courseId, userId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<WishlistDTO>> getAllWishlistItemsByUserId(@PathVariable Integer userId) {
        List<Wishlist> wishlistItems = wishlistService.getAllWishlistItemsByUserId(userId);
        List<WishlistDTO> wishlistDTOs = wishlistItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(wishlistDTOs);
    }

    private WishlistDTO convertToDTO(Wishlist wishlist) {
        WishlistDTO dto = new WishlistDTO();
        dto.setWishlistId(wishlist.getWishlistId());
        dto.setCourseId(wishlist.getCourse().getCourseId());
        dto.setCourseCategory(wishlist.getCourse().getCategory().getCategoryName());
        dto.setCourseTitle(wishlist.getCourse().getCourseTitle());
        dto.setCourseImage(wishlist.getCourse().getImageUrl());
        // Map other properties from Wishlist to WishlistDTO
        return dto;
    }
    // Other endpoints for retrieving, updating, and deleting wishlist items
}
