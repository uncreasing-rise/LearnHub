package com.example.learnhub.Service;

import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.DTO.common.enums.ErrorMessage;
import com.example.learnhub.DTO.common.response.ApiResponse;
import com.example.learnhub.DTO.quiz.request.AnswerRequest;
import com.example.learnhub.DTO.quiz.request.SubmitAnswerRequest;
import com.example.learnhub.DTO.quiz.response.QuizAnswerResponse;
import com.example.learnhub.Entity.*;
import com.example.learnhub.Exceptions.BusinessException;
import com.example.learnhub.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceOfQuiz {
    private final QuizRepository quizRepository;
    private final ServiceOfQuestion serviceOfQuestion;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final AnswerAttemptRepository answerAttemptRepository;
    private final QuizAttempRepository quizAttempRepository;

//    @Autowired
//    public ServiceOfQuiz(QuizRepository quizRepository, ServiceOfQuestion serviceOfQuestion, SectionRepository sectionRepository, UserRepository userRepository) {
//        this.quizRepository = quizRepository;
//        this.serviceOfQuestion = serviceOfQuestion;
//        this.sectionRepository = sectionRepository;
//
//        this.userRepository = userRepository;
//    }

    @Transactional
    public List<Quiz> createQuizzes(Section section, List<QuizDTO> quizDTOs) {
        List<Quiz> quizzes = new ArrayList<>();

        for (QuizDTO quizDTO : quizDTOs) {
            Quiz quiz = convertToQuizEntity(quizDTO, section);
            quizzes.add(quiz);
        }

        return quizRepository.saveAll(quizzes);
    }

    private Quiz convertToQuizEntity(QuizDTO quizDTO, Section section) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getQuizTitle());
        quiz.setSection(section);

        List<QuestionDTO> questionDTOs = quizDTO.getQuestions();
        List<Question> questions = serviceOfQuestion.createQuestions(quiz, questionDTOs);
        quiz.setQuestions(questions);

        return quiz;
    }

    @Transactional
    public void updateQuiz(QuizDTO quizDTO) {
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(quizDTO.getQuizId());
            if (optionalQuiz.isPresent()) {
                Quiz existingQuiz = optionalQuiz.get();
                // Update quiz properties
                existingQuiz.setTitle(quizDTO.getQuizTitle());
                // Update other properties if needed
                // Save the updated quiz
                quizRepository.save(existingQuiz);
            } else {
                throw new IllegalArgumentException("Quiz not found for ID: " + quizDTO.getQuizId());
            }
        } catch (Exception e) {
            // Handle update failure
            e.printStackTrace();
        }
    }


    @Transactional
    public Quiz createQuizToSection(Integer sectionId, QuizDTO quizDTO) {
        try {
            Optional<Section> optionalSection = sectionRepository.findById(sectionId);
            if (optionalSection.isPresent()) {
                Section section = optionalSection.get();
                Quiz quiz = convertToQuizEntity(quizDTO, section);
                return quizRepository.save(quiz);
            } else {
                throw new IllegalArgumentException("Section not found for ID: " + sectionId);
            }
        } catch (Exception e) {
            // Handle creation failure
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public void deleteQuizFromSection(Integer sectionId, Integer quizId) {
        try {
            Optional<Section> optionalSection = sectionRepository.findById(sectionId);
            if (optionalSection.isPresent()) {
                Section section = optionalSection.get();
                Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
                if (optionalQuiz.isPresent()) {
                    Quiz quiz = optionalQuiz.get();
                    // Remove the quiz from the section
                    section.getQuizzes().remove(quiz);
                    sectionRepository.save(section);
                    // Delete the quiz
                    quizRepository.delete(quiz);
                } else {
                    throw new IllegalArgumentException("Quiz not found for ID: " + quizId);
                }
            } else {
                throw new IllegalArgumentException("Section not found for ID: " + sectionId);
            }
        } catch (Exception e) {
            // Handle deletion failure
            e.printStackTrace();
        }
    }


    // --------------------------------------------------------------------------
    public ApiResponse<QuizAnswerResponse> submitProcess(SubmitAnswerRequest request) {
        try {
            // find user request
            User user = userRepository.findByEmail(request.getPrincipal()).orElseThrow(
                    () -> new BusinessException(ErrorMessage.USER_NOT_FOUND)
            );
            // find quiz id;
            Quiz quiz = quizRepository.findById(request.getQuizId()).orElseThrow(
                    () -> new BusinessException(ErrorMessage.USER_QUIZ_NOT_FOUND)
            );
            // find question and answer
            List<AnswerAttempt> answerAttemptList = new ArrayList<>();
            double totalPoint = 0.0;
            double point = 0.0;
            for (Question question : quiz.getQuestions()) {
                totalPoint = totalPoint + question.getPoint().doubleValue();
            }
            for (AnswerRequest answerRequest : request.getAnswerRequests()) {
                Question question = serviceOfQuestion.getQuestionById(answerRequest.getQuestionId(),false);
                Answer answerCorrect = question.getAnswers().stream().filter(Answer::isCorrect).toList().stream().findFirst().orElse(null);
                if(Objects.isNull(answerCorrect)) {
                    log.error("answer correct not found, to check the list");
                    throw new BusinessException(ErrorMessage.USER_ANSWER_NOT_FOUND);
                }
                Answer answerSelection = answerRepository.findByIdAndQuestion(answerRequest.getAnswerId(), question);
                if (Objects.equals(answerCorrect.getId(), answerSelection.getId())) {
                    point = point + question.getPoint().doubleValue();
                }

                AnswerAttempt answerAttempt = new AnswerAttempt();
                answerAttempt.setQuestionId(question.getId());
                answerAttempt.setSelectedAnswerId(answerSelection.getId());
                answerAttempt.setCorrectAnswerId(answerCorrect.getId());


                answerAttempt = answerAttemptRepository.save(answerAttempt);
                answerAttemptList.add(answerAttempt);
            }

            QuizAttempt quizAttempt = new QuizAttempt();
            quizAttempt.setQuiz(quiz);
            quizAttempt.setUser(user);
            quizAttempt.setListAnswer(answerAttemptList);
            quizAttempt.setPoint(point);
            quizAttempt.setTotalPoint(totalPoint);
            quizAttempt.setStartTime(request.getStartTime());
            quizAttempt.setEndTime(request.getEndTime());
            quizAttempt = quizAttempRepository.save(quizAttempt);

            return new ApiResponse<QuizAnswerResponse>().ok(new QuizAnswerResponse(quizAttempt));

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorMessage.USER_SUBMIT_ANSWER_FAILED);
        }
    }


    public ApiResponse<List<QuizAnswerResponse>> getHistory(Principal principal, Integer id) {
        try {
            // find user request
            User user = userRepository.findByEmailAndDeleted(principal.getName(), false).stream().findFirst().orElseThrow(
                    () -> new BusinessException(ErrorMessage.USER_NOT_FOUND)
            );
            // find quiz id;
            Quiz quiz = quizRepository.findById(id).orElseThrow(
                    () -> new BusinessException(ErrorMessage.USER_QUIZ_NOT_FOUND)
            );

            List<QuizAttempt> quizAttemptList = quizAttempRepository.findByUserAndQuiz(user, quiz);

            return new ApiResponse<List<QuizAnswerResponse>>().ok(quizAttemptList.stream().map(QuizAnswerResponse::new).collect(Collectors.toList()));

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorMessage.USER_GET_FAIL);
        }
    }


}
