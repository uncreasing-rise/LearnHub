package com.example.learnhub.Service;

import com.example.learnhub.DTO.WishlistDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Entity.Wishlist;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.Repository.WishlistRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
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
        Wishlist wishlist = new Wishlist();
        User user = userRepository.findById(wishlistDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(wishlistDTO.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));

        wishlist.setUser(user);
        wishlist.setCourse(course);

        Wishlist savedWishlistItem = wishlistRepository.save(wishlist);

        return mapWishlistEntityToDTO(savedWishlistItem);    }

    @Override
    public WishlistDTO deleteWishlistItem(Integer wishlistId) throws ChangeSetPersister.NotFoundException {
        Wishlist wishlistItemToDelete = wishlistRepository.findById(wishlistId).orElse(null);
        if (wishlistItemToDelete == null) {
            throw new ChangeSetPersister.NotFoundException();
        }
        wishlistRepository.delete(wishlistItemToDelete);
        return mapWishlistEntityToDTO(wishlistItemToDelete);
    }


    private WishlistDTO mapWishlistEntityToDTO(Wishlist wishlist) {
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setWishlistId(wishlist.getWishlistId());
        wishlistDTO.setUserId(wishlist.getUser().getUserId());
        wishlistDTO.setCourseId(wishlist.getCourse().getCourseId());
        return wishlistDTO;
    }


}
