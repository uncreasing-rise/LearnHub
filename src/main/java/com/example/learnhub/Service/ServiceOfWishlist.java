package com.example.learnhub.Service;

import com.example.learnhub.DTO.WishlistDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Entity.Wishlist;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.Repository.WishlistRepository;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfWishlist implements IServiceOfWishlist {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public ServiceOfWishlist(WishlistRepository wishlistRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public WishlistDTO addToWishList(WishlistDTO wishlistDTO) {
        int rating = wishlistDTO.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        User user = userRepository.findById(wishlistDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(wishlistDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setCourse(course);
        wishlist.setRating(rating);

        Wishlist savedWishlistItem = wishlistRepository.save(wishlist);

        return mapWishlistEntityToDTO(savedWishlistItem);
    }

    @Override
    public WishlistDTO deleteWishlistItem(Integer wishlistId) {
        Wishlist wishlistItemToDelete = wishlistRepository.findById(wishlistId).orElse(null);
        if (wishlistItemToDelete == null) {
            throw new RuntimeException("Wishlist item not found");
        }
        wishlistRepository.delete(wishlistItemToDelete);
        return mapWishlistEntityToDTO(wishlistItemToDelete);
    }

    private WishlistDTO mapWishlistEntityToDTO(Wishlist wishlist) {
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setWishlistId(wishlist.getWishlistId());
        wishlistDTO.setUserId(wishlist.getUser().getUserId());
        wishlistDTO.setCourseId(wishlist.getCourse().getCourseId());
        wishlistDTO.setRating(wishlist.getRating());
        return wishlistDTO;
    }
}
