package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CommentDTO;
import com.example.learnhub.DTO.CourseDTO;
import com.example.learnhub.DTO.ResponeCourseDTO;
import com.example.learnhub.DTO.SectionDTO;
import com.example.learnhub.DTO.common.enums.ErrorMessage;
import com.example.learnhub.DTO.common.response.ApiResponse;
import com.example.learnhub.DTO.user.response.CommonStatusResponse;
import com.example.learnhub.Entity.Comment;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.CourseRegister;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Exceptions.AppServiceExeption;
import com.example.learnhub.Exceptions.BusinessException;
import com.example.learnhub.Repository.CourseRegisterRepository;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.Service.ServiceOfComment;
import com.example.learnhub.Service.ServiceOfCourse;
import com.example.learnhub.Service.ServiceOfLearningDetail;
import com.example.learnhub.Service.ServiceOfSection;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin("*")
@MultipartConfig
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final ServiceOfComment serviceOfComment;

    private final ServiceOfSection serviceOfSection;
    private final ServiceOfCourse serviceOfCourse;
    private final CourseRepository courseRepository;
    private final ServiceOfLearningDetail serviceOfLearningDetail;
    private final CourseRegisterRepository courseRegisterRepository;
    private final UserRepository userRepository;


    // API để hiển thị thông tin về các phần và video của một khóa học
    @PostMapping(   "/showSectionAndVideo/{id}")
    public ResponseEntity<ResponeCourseDTO> showSectionAndVideo(@PathVariable int id) {
        try {
            ResponeCourseDTO courseDTO = serviceOfCourse.showSectionAndVideo(id);
            return new ResponseEntity<>(courseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // API để tìm kiếm các khóa học theo giá
    @GetMapping("/{price}")
    public List<CourseDTO> findAllCourseByPrice(@PathVariable double price) {
        List<Course> courses = courseRepository.findByPrice(price);
        return serviceOfCourse.fromCourseListToCourseDTOList(courses);
    }

    @GetMapping("/{courseId}/comments")
    public List<CommentDTO> getCommentsForCourse(@PathVariable Integer courseId) {
        // Call the service or repository to retrieve comments for the course with the specified ID
        List<Comment> comments = serviceOfComment.findByCourseID(courseId);
        return mapToDTO(comments);
    }

    private List<CommentDTO> mapToDTO(List<Comment> comments) {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO dto = new CommentDTO();
            dto.setUserName(comment.getUser().getFullName());
            dto.setCommentText(comment.getCommentText());
            dto.setUserImage(comment.getUser().getImage());
            dto.setCreatedDate(comment.getCreatedDate());
            // Map other properties as needed
            commentDTOs.add(dto);
        }
        return commentDTOs;
    }
    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponeCourseDTO createCourse(@RequestPart("courseDTO") @Valid CourseDTO courseDTO,
                                         @RequestPart("articleFiles") List<MultipartFile> articleFiles,
                                         @RequestPart("videoFiles") List<MultipartFile> videoFiles,
                                         @RequestPart("videoTrial") MultipartFile videoTrial,
                                         @RequestPart("ImageFile") MultipartFile imageCourse)
            throws AppServiceExeption, IOException {
        Course course = serviceOfCourse.createCourse(courseDTO, imageCourse, videoTrial);

        List<SectionDTO> sections = courseDTO.getSections();

        int articleIndex = 0;
        int videoIndex = 0;

        for (SectionDTO section : sections) {
            int articleFilesInSection = section.getArticles().size();
            int videoFilesInSection = section.getVideos().size();

            List<MultipartFile> sectionArticleFiles = articleFiles.subList(articleIndex, articleIndex + articleFilesInSection);
            List<MultipartFile> sectionVideoFiles = videoFiles.subList(videoIndex, videoIndex + videoFilesInSection);

            serviceOfSection.createSection(section, course, sectionArticleFiles, sectionVideoFiles);

            articleIndex += articleFilesInSection;
            videoIndex += videoFilesInSection;
        }

        serviceOfLearningDetail.createLearningDetail(courseDTO.getLearningDetail(), course);

        return serviceOfCourse.fromCourseToResponeCourseDTO(course);
    }



    // API để lấy tất cả các khóa học
    @GetMapping
    public ResponseEntity<List<ResponeCourseDTO>> getAllCourses() {
        List<Course> courses = serviceOfCourse.getAllCourses();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }
    @GetMapping("/courses")
    public List<Course> getCoursesByPriceRange(
            @RequestParam("minPrice") double minPrice,
            @RequestParam("maxPrice") double maxPrice) {

        return serviceOfCourse.getCoursesByPriceRange(minPrice, maxPrice);
    }

    // API để tìm kiếm các khóa học theo từ khóa
    @GetMapping("/search")
    public ResponseEntity<List<ResponeCourseDTO>> searchCoursesByKeyword(@RequestParam String keyword) {
        List<Course> courses = serviceOfCourse.findCoursesByKeyword(keyword);
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo danh mục
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByCategory(@PathVariable String category) {
        List<Course> courses = serviceOfCourse.getCoursesByCategory(category);
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo giá cao hơn
    @GetMapping("/price/higher")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByPriceHigher() {
        List<Course> courses = serviceOfCourse.getCoursesByPriceHigher();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo giá thấp hơn
    @GetMapping("/price/lower")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByPriceLower() {
        List<Course> courses = serviceOfCourse.getCoursesByPriceLower();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo ngày mới nhất
    @GetMapping("/date/new")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByDateNew() {
        List<Course> courses = serviceOfCourse.getCoursesByDateNew();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API để lấy danh sách các khóa học theo ngày cũ nhất
    @GetMapping("/date/old")
    public ResponseEntity<List<ResponeCourseDTO>> getCoursesByDateOld() {
        List<Course> courses = serviceOfCourse.getCoursesByDateOld();
        List<ResponeCourseDTO> courseDTOs = courses.stream()
                .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(courseDTOs);
    }

    // API to delete a course
    @DeleteMapping
    public ResponseEntity<Void> deleteCourse(@RequestParam("id") int id) {
        serviceOfCourse.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }



    //-------------------------------------
    @GetMapping("/studying")
    ResponseEntity<List<ResponeCourseDTO>> getCoursesByUser(Principal principal) {
        try {
            User user = userRepository.findByEmailAndDeleted(principal.getName(), false).stream().findFirst().orElse(null);
            if(Objects.isNull(user)){
                throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
            }
            List<CourseRegister> courseRegisterList = courseRegisterRepository.findByUser(user);
            List<Course> courses = courseRegisterList.stream().map(CourseRegister::getCourse).toList();
            List<ResponeCourseDTO> courseDTOs = courses.stream()
                    .map(serviceOfCourse::fromCourseToListResponeCourseDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(courseDTOs);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorMessage.USER_GET_FAIL);
        }
    }


    @GetMapping("/{courseID}/check")
    ResponseEntity<ApiResponse<CommonStatusResponse>> checkCourseStudying(Principal principal, @PathVariable("courseID") Integer courseId) {
        try {
            User user = userRepository.findByEmailAndDeleted(principal.getName(), false).stream().findFirst().orElse(null);
            if(Objects.isNull(user)){
                throw new BusinessException(ErrorMessage.USER_NOT_FOUND);
            }
            Course course = courseRepository.findById(courseId).orElse(null);

            if(Objects.isNull(course)){
                throw new BusinessException(ErrorMessage.COURSE_NOT_FOUND);
            }
            CourseRegister courseRegister = courseRegisterRepository.findByUserAndCourse(user, course).orElse(null);
            ApiResponse<CommonStatusResponse> response = new ApiResponse<CommonStatusResponse>().ok(new CommonStatusResponse(!Objects.isNull(courseRegister)));
            return new ResponseEntity<ApiResponse<CommonStatusResponse>>(response,HttpStatus.OK);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorMessage.USER_GET_FAIL);
        }
    }
}
