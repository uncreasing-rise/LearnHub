package com.example.learnhub.Service;

import com.example.learnhub.DTO.WishlistDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Entity.Wishlist;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.Repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // Kiểm tra xem khoá học đã tồn tại trong wishlist chưa
        Wishlist existingWishlistItem = wishlistRepository.findByCourse_CourseId(wishlistDTO.getCourseId());
        if (existingWishlistItem != null) {
            throw new RuntimeException("Course already exists in the wishlist");
        }

        User user = userRepository.findById(wishlistDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(wishlistDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setCourse(course);

        Wishlist savedWishlistItem = wishlistRepository.save(wishlist);

        return mapWishlistEntityToDTO(savedWishlistItem);
    }


    @Override
    public void deleteWishlistItem(Integer wishlistId) {
        Wishlist wishlistItemToDelete = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found with ID: " + wishlistId));
        wishlistRepository.delete(wishlistItemToDelete);
        mapWishlistEntityToDTO(wishlistItemToDelete);
    }

    private WishlistDTO mapWishlistEntityToDTO(Wishlist wishlist) {
        WishlistDTO wishlistDTO = new WishlistDTO();
        wishlistDTO.setWishlistId(wishlist.getWishlistId());
        wishlistDTO.setUserId(wishlist.getUser().getUserId());
        wishlistDTO.setCourseId(wishlist.getCourse().getCourseId());
        return wishlistDTO;
    }

    public void deleteWishlistItemByCourseIdAndUserId(Integer courseId, Integer userId) {
        wishlistRepository.deleteWishlistItemByCourseIdAndUserId(courseId, userId);
    }

    public List<Wishlist> getAllWishlistItemsByUserId(Integer userId) {
        return wishlistRepository.getAllWishlistItemsByUserId(userId);
    }

    public boolean checkIfInWishlist(Integer courseId, Integer userId) {
        Wishlist wishlistItem = wishlistRepository.findByCourse_CourseIdAndUser_UserId(courseId, userId);
        return wishlistItem != null;

    }
}
