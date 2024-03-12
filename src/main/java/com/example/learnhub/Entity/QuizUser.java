//package com.example.learnhub.Entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.Type;
//
//import java.util.Date;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "quiz_user")
//public class QuizUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "exam_id")
//    private Quiz quiz;
//
//    @JsonIgnore
//    @ManyToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @Column(name = "is_started")
//    private boolean isStarted;
//
//    @Column(name = "time_start")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date timeStart;
//
//    @Column(name = "time_finish")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date timeFinish;
//
//    @JsonIgnore
//    @Column(name = "answer_sheet", columnDefinition = "text")
//    private String answerSheet;
//
//    @Column(name = "is_finished")
//    private boolean isFinished;
//
//    @Column(name = "remaining_time")
//    private int remainingTime;
//
//    @Column(name = "total_point")
//    private Double totalPoint;
//}
