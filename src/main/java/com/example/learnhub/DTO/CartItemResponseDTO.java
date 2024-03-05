package com.example.learnhub.DTO;

import com.example.learnhub.Entity.CartItem;
import com.example.learnhub.Entity.Image;
import com.example.learnhub.Entity.Rating;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartItemResponseDTO {
    private Integer cartItemId;
    private Integer courseId;
    private String courseTitle;
    private String courseDes;
    private Double coursePrice;
    private String categoryName;
    private Boolean isPassed;
    private Date courseDate;
    private String level;
    private String tag;
    private Integer userId;
    private List<Image> images;
    private Integer status = 0;

    public CartItemResponseDTO(CartItem i) {
        this.cartItemId = i.getCartItemId();
        this.courseId = i.getCourse().getCourseId();
        this.courseTitle = i.getCourse().getCourseTitle();
        this.courseDes = i.getCourse().getCourseDes();
        this.coursePrice = i.getCourse().getCoursePrice();
        this.categoryName = i.getCourse().getCategory().getCategoryName();
        this.isPassed = i.getCourse().getPassed();
        this.courseDate = i.getCourse().getCourseDate();
        this.level = i.getCourse().getLevel();
        this.tag = i.getCourse().getTag();
        this.userId = i.getCourse().getUserId();
        this.images = i.getCourse().getImages();
        this.status = i.getCourse().getStatus();
    }

    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDes() {
        return courseDes;
    }

    public void setCourseDes(String courseDes) {
        this.courseDes = courseDes;
    }

    public Double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(Double coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getPassed() {
        return isPassed;
    }

    public void setPassed(Boolean passed) {
        isPassed = passed;
    }

    public Date getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(Date courseDate) {
        this.courseDate = courseDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
