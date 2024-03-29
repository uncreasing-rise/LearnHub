package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.AnswerRepository;
import com.example.learnhub.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfAnswer implements IServiceOfAnswer {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public ServiceOfAnswer(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Answer getAnswerById(int id, Question question) {
        return answerRepository.findByIdAndQuestion(id, question);
    }

    @Transactional
    public List<Answer> convertToAnswerEntities(List<AnswerDTO> answerDTOS, Question question) {
        List<Answer> answers = new ArrayList<>();

        for (AnswerDTO answerDTO : answerDTOS) {
            Answer answer = new Answer();
            answer.setText(answerDTO.getAnswerText());
            answer.setCorrect(answerDTO.getIsCorrect());
            answer.setQuestion(question);
            answers.add(answer);
        }
        return answerRepository.saveAll(answers);
    }

    @Transactional
    public Answer createAnswerToQuestion(Integer questionId, AnswerDTO answerDTO) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found for ID: " + questionId));

        Answer answer = new Answer();
        answer.setText(answerDTO.getAnswerText());
        answer.setCorrect(answerDTO.getIsCorrect());
        answer.setQuestion(question);
        return answerRepository.save(answer);
    }

    @Transactional
    public boolean deleteAnswerFromQuestion(Integer questionId, Integer answerId) {
        try {
            // Check if the question exists
            Optional<Question> optionalQuestion = questionRepository.findById(questionId);
            if (optionalQuestion.isPresent()) {
                Question question = optionalQuestion.get();

                // Retrieve the answer to delete
                Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
                if (optionalAnswer.isPresent()) {
                    Answer answer = optionalAnswer.get();

                    // Check if the answer belongs to the specified question
                    if (answer.getQuestion().equals(question)) {
                        // Get the number of answers associated with the question
                        int answerCount = question.getAnswers().size();

                        // Check if there are only two answers associated with the question
                        if (answerCount <= 2) {
                            throw new IllegalStateException("Cannot delete answer. There must be at least two answers associated with the question.");
                        }

                        // Delete the answer
                        answerRepository.delete(answer);
                        return true;
                    } else {
                        throw new IllegalArgumentException("The provided answer does not belong to the specified question.");
                    }
                } else {
                    throw new IllegalArgumentException("Answer not found for ID: " + answerId);
                }
            } else {
                throw new IllegalArgumentException("Question not found for ID: " + questionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete answer from question: " + e.getMessage());
        }
    }


}