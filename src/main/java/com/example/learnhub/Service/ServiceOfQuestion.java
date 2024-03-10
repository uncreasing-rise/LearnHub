package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOfQuestion {
    private final QuestionRepository questionRepository;
    private final ServiceOfAnswer serviceOfAnswer;

    @Autowired
    public ServiceOfQuestion(QuestionRepository questionRepository, ServiceOfAnswer serviceOfAnswer) {
        this.questionRepository = questionRepository;
        this.serviceOfAnswer = serviceOfAnswer;
    }

    @Transactional
    public List<Question> createQuestions(Quiz quiz, List<QuestionDTO> questionDTOS) {
        List<Question> questions = new ArrayList<>();

        for (QuestionDTO questionDTO : questionDTOS) {
            Question question = convertToQuestionEntity(questionDTO, quiz);
            questions.add(question);
        }

        return questionRepository.saveAll(questions);
    }

    private Question convertToQuestionEntity(QuestionDTO questionDTO, Quiz quiz) {
        Question question = new Question();
        question.setText(questionDTO.getQuestionText());
        question.setPoint(questionDTO.getPoint());

        // Kiểm tra nếu quiz hiện tại của câu hỏi khác với quiz mới
        if (question.getQuiz() != quiz) {
            question.setQuiz(quiz);
        }

        List<AnswerDTO> answerDTOS = questionDTO.getAnswerDTOs();
        if (answerDTOS != null && !answerDTOS.isEmpty()) { // Kiểm tra nếu danh sách không rỗng
            List<Answer> answers = serviceOfAnswer.convertToAnswerEntities(answerDTOS, question);
            question.setAnswers(answers);
        }
        return question;
    }


}