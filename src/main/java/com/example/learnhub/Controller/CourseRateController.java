package com.example.learnhub.Controller;

import com.example.learnhub.DTO.RatingDTO;
import com.example.learnhub.Exceptions.UserNotFoundException;
import com.example.learnhub.Service.ServiceOfCourseRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course-ratings")
public class CourseRateController {

    private final ServiceOfCourseRate courseRateService;

    @Autowired
    public CourseRateController(ServiceOfCourseRate courseRateService) {
        this.courseRateService = courseRateService;
    }

    @PostMapping("/{courseId}/{userId}/{ratingValue}")
    public ResponseEntity<String> rateCourse(@PathVariable int courseId, @PathVariable int userId, @PathVariable int ratingValue) throws UserNotFoundException {
        boolean success = courseRateService.rateCourse(courseId, userId, ratingValue);

        if (success) {
            return new ResponseEntity<>("Rating added successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to add rating.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<RatingDTO> getCourseRating(@PathVariable int courseId) {
        RatingDTO ratingDTO = courseRateService.getCourseRating(courseId);

        if (ratingDTO != null) {
            return new ResponseEntity<>(ratingDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable int ratingId) {
        boolean success = courseRateService.deleteRatingById(ratingId);

        if (success) {
            return new ResponseEntity<>("Rating deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete rating.", HttpStatus.BAD_REQUEST);
        }
    }
}
