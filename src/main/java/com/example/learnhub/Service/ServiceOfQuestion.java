package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Repository.QuestionRepository;
import com.example.learnhub.Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfQuestion {
    private final QuestionRepository questionRepository;
    private final ServiceOfAnswer serviceOfAnswer;
    private final QuizRepository quizRepository;

    @Autowired
    public ServiceOfQuestion(QuestionRepository questionRepository, ServiceOfAnswer serviceOfAnswer, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.serviceOfAnswer = serviceOfAnswer;
        this.quizRepository = quizRepository;
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

    @Transactional
    public Question updateQuestion(Quiz existingQuiz, List<QuestionDTO> questionDTOs) {
        Question existingQuestion = null; // Declare outside try block to access it in the catch block
        try {
            for (QuestionDTO questionDTO : questionDTOs) {
                Optional<Question> optionalQuestion = questionRepository.findById(questionDTO.getQuestionId());
                if (optionalQuestion.isPresent()) {
                    existingQuestion = optionalQuestion.get();
                    // Update question properties
                    existingQuestion.setText(questionDTO.getQuestionText());
                    existingQuestion.setPoint(questionDTO.getPoint());
                    existingQuestion.setQuiz(existingQuiz); // Ensure the question is associated with the correct quiz
                    // Update answers if necessary
                } else {
                    throw new IllegalArgumentException("Question not found for ID: " + questionDTO.getQuestionId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return existingQuestion;
    }

    @Transactional
    public Question createQuestionToQuiz(Integer quizId, QuestionDTO questionDTO) {
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
            if (optionalQuiz.isPresent()) {
                Quiz quiz = optionalQuiz.get();

                Question question = convertToQuestionEntity(questionDTO, quiz);

                // Save the question entity and return it
                return questionRepository.save(question);
            } else {
                throw new IllegalArgumentException("Quiz not found for ID: " + quizId);
            }
        } catch (Exception e) {
            // Handle creation failure
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean deleteQuestionFromQuiz(Integer quizId, Integer questionId) {
        try {
            // Kiểm tra xem quiz có tồn tại không
            if (quizRepository.existsById(quizId)) {
                // Kiểm tra xem question có tồn tại không
                if (questionRepository.existsById(questionId)) {
                    // Xóa câu hỏi khỏi bài kiểm tra
                    questionRepository.deleteById(questionId);
                    return true; // Trả về true nếu xóa thành công
                } else {
                    throw new IllegalArgumentException("Question not found for ID: " + questionId);
                }
            } else {
                throw new IllegalArgumentException("Quiz not found for ID: " + quizId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu xóa thất bại
        }
    }
}