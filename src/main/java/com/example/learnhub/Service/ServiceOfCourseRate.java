package com.example.learnhub.Service;

import com.example.learnhub.DTO.RatingDTO;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.Rating;

import com.example.learnhub.Exceptions.CourseNotFoundException;
import com.example.learnhub.Exceptions.UserNotFoundException;
import com.example.learnhub.Repository.CourseRateRepository;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfCourseRate {

    private final CourseRateRepository courseRateRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ServiceOfCourseRate(CourseRateRepository courseRateRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRateRepository = courseRateRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public boolean rateCourse(int courseId, int userId, int ratingValue) throws UserNotFoundException {
        // Check if the user has already rated the course
        Optional<Rating> existingRating = courseRateRepository.getCourseRateByAccountIdAndCourseId(userId, courseId);
        if (existingRating.isPresent()) {
            // Update the existing rating
            Rating ratingToUpdate = existingRating.get();
            ratingToUpdate.setRatingValue(ratingValue);
            courseRateRepository.save(ratingToUpdate);
            return true;
        } else {
            // Add a new rating
            Rating newRating = new Rating();
            newRating.setRatingValue(ratingValue);

            // Set the Course entity based on courseId
            Optional<Course> courseOptional = Optional.ofNullable(courseRepository.findById(courseId));
            if (courseOptional.isPresent()) {
                newRating.setCourse(courseOptional.get());
            } else {
                // Handle the case when the course is not found
                throw new CourseNotFoundException(courseId);
            }

            // Set the User entity based on userId
            Optional<com.example.learnhub.Entity.User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                newRating.setUser(userOptional.get());
            } else {
                // Handle the case when the user is not found
                throw new UserNotFoundException(userId);
            }

            newRating.setRatingTime(new java.util.Date());
            courseRateRepository.save(newRating);
            return true;
        }
    }

    public RatingDTO getCourseRating(int courseId) {
        RatingDTO ratingDTO = new RatingDTO();
        List<Rating> ratings = courseRateRepository.showCourseRateByCourseId(courseId);

        // Calculate the average, count, etc. based on the ratings list (implement the logic accordingly)
        int ratingCount = ratings.size();

        ratingDTO.setRatingValue(ratingDTO.getRatingValue());
        ratingDTO.setRatingCount(ratingCount);
        return ratingDTO;
    }

    public boolean deleteRatingById(int ratingId) {
        // Check if the rating exists
        Optional<Rating> ratingToDelete = courseRateRepository.findById(ratingId);
        if (ratingToDelete.isPresent()) {
            // Delete the rating by ID
            courseRateRepository.deleteRatingById(ratingId);
            return true;
        } else {
            return false; // Rating not found
        }
    }
}
