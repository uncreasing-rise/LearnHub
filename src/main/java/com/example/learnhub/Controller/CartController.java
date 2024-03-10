package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CartItemResponseDTO;
import com.example.learnhub.Entity.Cart;
import com.example.learnhub.Entity.CartItem;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Repository.CartItemRepository;
import com.example.learnhub.Repository.CartRepository;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return ResponseEntity.ok(carts);
    }
    @GetMapping("getCart/{userId}")
    public ResponseEntity<Cart> getCartsByUserId(@PathVariable Integer userId) {
        List<Cart> userCarts = cartRepository.findByUserUserId(userId);

        return ResponseEntity.ok(userCarts.get(0));
    }

    @GetMapping("getCartItems/{userId}")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItemsByUserId(@PathVariable Integer userId) {
        List<Cart> userCarts = cartRepository.findByUserUserId(userId);
        Cart curCart = userCarts.get(0);
        List<CartItem> lc =cartItemRepository.findByCartCartId(curCart.getCartId());
        List<CartItemResponseDTO> responseDTOList = new ArrayList<>();
        for(CartItem c : lc){
            responseDTOList.add(new CartItemResponseDTO());
        }
        return ResponseEntity.ok(responseDTOList);
    }
    @GetMapping("/add")// /add?userId=abc&
    public ResponseEntity<String> addToCart(@RequestParam(name = "userId")  Integer userId, @RequestParam(name = "courseId") int courseID) {
        CartItem cartItem = new CartItem();
        Cart c;
        Course course = courseRepository.findById(courseID);
        Optional<User> user = userRepository.findByUserId(userId);
        if (user == null){
            return ResponseEntity.ok("This userId is not exist!");
        }
        if (course == null){
            return ResponseEntity.ok("This courseId is not exist!");
        }
        List<Cart> userCarts = cartRepository.findByUserUserId(userId);
        if (userCarts.size() ==0){
            c = new Cart();
            c.setTotal(course.getCoursePrice());
            c.setCartDate(new Date());
            c.setUser(user.get());
            c = cartRepository.save(c);
            cartItem.setCart(c);
            cartItem.setCourse(course);
            CartItem addedCartItem = cartItemRepository.save(cartItem);
        }else {
            c = userCarts.get(0);
            List<CartItem> checkExist = cartItemRepository.findByCartCartIdAndCourseCourseId(c.getCartId(),courseID);

            if (checkExist.size()==0){

               cartItem.setCart(c);
               cartItem.setCourse(course);
               CartItem addedCartItem = cartItemRepository.save(cartItem);
               c.setTotal(c.getTotal() + course.getCoursePrice());
               c = cartRepository.save(c);
           }else {
                return ResponseEntity.ok("This course already exist in cart!");

            }
        }
        return ResponseEntity.ok("Add cart successfully");
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Integer cartItemId) {
        List<CartItem> check = cartItemRepository.findByCartItemId(Long.valueOf(cartItemId));
        if (check.size()==0){
            return ResponseEntity.ok("This id not exist try again!");
        }
        cartItemRepository.deleteById(Long.valueOf(cartItemId));
        return ResponseEntity.ok("Delete Successfully");

    }
}
