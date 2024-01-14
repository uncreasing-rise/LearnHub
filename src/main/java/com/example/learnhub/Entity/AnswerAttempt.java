package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "AnswerAttempt")
public class AnswerAttempt {
    @Id
    @Column(name = "AttemptID")
    private Integer attemptId;

    @Id
    @Column(name = "QuestionID")
    private Integer questionId;

    @Column(name = "SelectedAnswerID")
    private Integer selectedAnswerId;

    public Integer getAttemptId() {
        return this.attemptId;
    }

    public void setAttemptId(Integer attemptId) {
        this.attemptId = attemptId;
    }

    public Integer getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getSelectedAnswerId() {
        return this.selectedAnswerId;
    }

    public void setSelectedAnswerId(Integer selectedAnswerId) {
        this.selectedAnswerId = selectedAnswerId;
    }
}
