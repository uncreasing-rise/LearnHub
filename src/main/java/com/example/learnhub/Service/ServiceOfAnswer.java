package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.AnswerRepository;
import com.example.learnhub.Repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceOfAnswer {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public ServiceOfAnswer(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }
    @Transactional
    public AnswerDTO createAnswer(AnswerDTO answerDTO) {
        validateAnswerDTO(answerDTO);

        Optional<Question> optionalQuestion = questionRepository.findById(answerDTO.getQuestionId());
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            Answer answer = mapAnswerDTOToEntity(answerDTO, question);
            Answer savedAnswer = answerRepository.save(answer);
            return mapAnswerEntityToDTO(savedAnswer);
        } else {
            throw new RuntimeException("Question not found");
        }
    }

    public List<AnswerDTO> getAllAnswers() {
        List<Answer> answers = answerRepository.findAll();
        return answers.stream()
                .map(this::mapAnswerEntityToDTO)
                .collect(Collectors.toList());
    }

    public AnswerDTO getAnswerById(Integer id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found with id: " + id));
        return mapAnswerEntityToDTO(answer);
    }

    @Transactional
    public AnswerDTO updateAnswer(Integer id, AnswerDTO updatedAnswerDTO) {
        validateAnswerDTO(updatedAnswerDTO);
        Answer existingAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found with id: " + id));
        existingAnswer.setText(updatedAnswerDTO.getAnswerText());
        existingAnswer.setCorrect(updatedAnswerDTO.getIsCorrect());
        // Update other properties as needed...

        Answer savedAnswer = answerRepository.save(existingAnswer);
        return mapAnswerEntityToDTO(savedAnswer);
    }

    @Transactional
    public void deleteAnswer(Integer id) {
        Answer existingAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found with id: " + id));
        answerRepository.delete(existingAnswer);
    }

    private void validateAnswerDTO(AnswerDTO answerDTO) {
        if (answerDTO.getQuestionId() == null) {
            throw new IllegalArgumentException("Question ID cannot be null");
        }
        // Add more validation if needed
    }

    private Answer mapAnswerDTOToEntity(AnswerDTO answerDTO, Question question) {
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setText(answerDTO.getAnswerText());
        answer.setCorrect(answerDTO.getIsCorrect());
        return answer;
    }

    private AnswerDTO mapAnswerEntityToDTO(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setAnswerId(answer.getId());
        answerDTO.setQuestionId(answer.getQuestion().getId());
        answerDTO.setAnswerText(answer.getText());
        answerDTO.setIsCorrect(answer.isCorrect());
        return answerDTO;
    }
}