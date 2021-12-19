//package degradation.services.impl;
//
//import com.company.archon.dto.*;
//import com.company.archon.entity.*;
//import com.company.archon.enums.GameStatus;
//import com.company.archon.exception.EntityNotFoundException;
//import com.company.archon.mapper.QuestionMapper;
//import com.company.archon.repositories.*;
//import com.company.archon.services.*;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//@Transactional
//@AllArgsConstructor
//public class GameServiceImpl implements GameService {
//
//    private final QuestionCounterRepository questionCounterRepository;
//    private final GameRepository gameRepository;
//    private final GamePatternRepository gamePatternRepository;
//    private final QuestionRepository questionRepository;
//    private final ParameterRepository parameterRepository;
//    private final GameParameterRepository gameParameterRepository;
//    private final GameParameterService gameParameterService;
//    private final QuestionParameterRepository questionParameterRepository;
//    private final UserRepository userRepository;
//    private final AnswerService answerService;
//    private final AnswerRepository answerRepository;
//    private final AnswerParameterRepository answerParameterRepository;
//    private final AnswerUserParameterRepository answerUserParameterRepository;
//    private final QuestionUserParameterRepository questionUserParameterRepository;
//    private final UserParameterRepository userParameterRepository;
//    private final AuthorizationService authorizationService;
//
//
//    @Override
//    public GameDto startNewGame(Long gamePatternId) {
//        String username = authorizationService.getProfileOfCurrent().getUsername();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
//
//        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
//                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
//
//        Game game = new Game();
//        game.setGameStatus(GameStatus.RUNNING);
//        game.setGamePattern(gamePattern);
//        createParameters(game, gamePattern);
//        game.setUser(user);
//        gameRepository.save(game);
//        createQuestionCounters(game, user);
//
//        return mapToDto(game, null);
//    }
//
//    private GameDto mapToDto(Game game, Answer answer) {
//        GameDto gameDto = new GameDto();
//        gameDto.setId(game.getId());
//        gameDto.setGameStatus(game.getGameStatus());
//        gameDto.setGamePatternId(game.getGamePattern().getId());
//        gameDto.setGameStatus(game.getGameStatus());
//
//        QuestionDto question = nextQuestion(game, answer);
//        gameDto.setQuestion(question);
//
//        if (question != null) {
//            List<AnswerDto> answers = answerService.getAnswersByQuestionId(question.getId());
//            gameDto.setAnswers(answers);
//
//            game.setGameStatus(question.getStatus());
//            gameRepository.save(game);
//        }
//
//        List<GameParameterDto> parameters = gameParameterService.getByGameId(game.getId());
//        gameDto.setParameters(parameters);
//
//        return gameDto;
//    }
//
//    private void createParameters(Game game, GamePattern gamePattern) {
//        List<Parameter> parameters = parameterRepository.findAllByGamePatternId(gamePattern.getId());
//        for (Parameter parameter : parameters) {
//            GameParameter gameParameter = new GameParameter();
//            gameParameter.setParameter(parameter);
//            gameParameter.setVisible(parameter.getVisible());
//            gameParameter.setTitle(parameter.getTitle());
//            gameParameter.setValue(parameter.getDefaultValue());
//            gameParameter.setGame(game);
//            gameParameterRepository.save(gameParameter);
//            game.getParameters().add(gameParameter);
//        }
//    }
//
//    private List<Question> changeQuestions(Game game) {
//        String username = authorizationService.getProfileOfCurrent().getUsername();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
//
//        List<Question> questions = questionRepository.findAllByGamePatternId(game.getGamePattern().getId());
//        return questions.stream()
//                .filter(question -> questionParameterRepository
//                        .findAllByQuestion(question).stream()
//                        .noneMatch(parameter ->
//                                parameter.getValueAppear() > gameParameterRepository
//                                        .findAllByTitleAndGame(parameter.getTitle(), game)
//                                        .orElseThrow(() -> new EntityNotFoundException("GameParameter with title " + parameter.getTitle() + " not found")).getValue()
//                                        || parameter.getValueDisappear() < gameParameterRepository
//                                        .findAllByTitleAndGame(parameter.getTitle(), game)
//                                        .orElseThrow(() -> new EntityNotFoundException("GameParameter with title " + parameter.getTitle() + " not found")).getValue()))
//                .filter(question -> questionUserParameterRepository
//                        .findAllByQuestion(question).stream()
//                        .noneMatch(parameter ->
//                                parameter.getValueAppear() > userParameterRepository
//                                        .findByTitleAndUser(parameter.getTitle(), user)
//                                        .orElseThrow(() -> new EntityNotFoundException("UserParameter with title " + parameter.getTitle() + " not found"))
//                                        .getValue()))
//                .collect(Collectors.toList());
//    }
//
//    private void createQuestionCounters(Game game, User user) {
//        List<Question> questions = questionRepository.findAllByGamePatternId(game.getGamePattern().getId());
//        for (Question question:questions) {
//            Optional<QuestionCounter> questionCounterOptional = questionCounterRepository.findByQuestionAndUserId(question, user.getId());
//            if (!questionCounterOptional.isPresent()) {
//                QuestionCounter questionCounter = new QuestionCounter();
//                questionCounter.setQuestion(question);
//                questionCounter.setUser(user);
//                questionCounter.setTime(0);
//                questionCounterRepository.save(questionCounter);
//            }
//        }
//    }
//
//    private QuestionDto nextQuestion(Game game, Answer answer) {
//        List<Question> questions = changeQuestions(game);
//        game.setQuestionsPull(questions);
//        gameRepository.save(game);
//        Question question = randomQuestion(questions, answer);
//        if (questions == null)
//            return null;
//        return QuestionMapper.INSTANCE.mapToDto(question);
//    }
//
//    private Question randomQuestion(List<Question> questions, Answer answer) {
//        String username = authorizationService.getProfileOfCurrent().getUsername();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
//
//        if (answer != null && answer.getQuestion().getRelativeQuestion() != null)
//            return answer.getQuestion().getRelativeQuestion();
//
//        List<Question> questionPull = new ArrayList<>();
//        for (Question question : questions) {
//            if (GameStatus.GAME_OVER.equals(question.getStatus())
//                    || GameStatus.COMPLETED.equals(question.getStatus())) {
//                return question;
//            }
//            QuestionCounter questionCounter = questionCounterRepository.findByQuestionAndUserId(question, user.getId())
//                    .orElseThrow(() -> new UsernameNotFoundException("QuestionCounter with user " + user.getUsername() + " doesn't exists!"));
//            if (questionCounter.getTime() > Integer.min(5, questions.size())) {
//                questionCounter.setTime(0);
//                questionCounterRepository.save(questionCounter);
//            }
//            if (questionCounter.getTime() == 0) {
//                questionPull.add(question);
//            }
//        }
//        long summary = questions.stream()
//                .map(Question::getWeight)
//                .reduce(0, Integer::sum);
//        double random = Math.random() * summary;
//        long counter = 0;
//        for (Question question : questions) {
//            counter += question.getWeight();
//            if (counter >= random) {
//                QuestionCounter questionCounter = questionCounterRepository.findByQuestionAndUserId(question, user.getId())
//                        .orElseThrow(() -> new UsernameNotFoundException("QuestionCounter with user " + user.getUsername() + " doesn't exists!"));
//                questionCounter.setTime(questionCounter.getTime() + 1);
//                questionCounterRepository.save(questionCounter);
//                return question;
//            }
//        }
////        Question question = randomQuestion(questions, answer);
////        if (question == null)
////            return null;
//        return questions.get(0);
//    }
//
////    private List<Question> fillQuestionsPull(List<Question> questions, User u){
////        for (Question question : questions) {
////            if (GameStatus.GAME_OVER.equals(question.getStatus())
////                    || GameStatus.COMPLETED.equals(question.getStatus())) {
////                return question;
////            }
////            QuestionCounter questionCounter = questionCounterRepository.findByQuestionAndUserId(question, user)
////                    .orElseThrow(() -> new UsernameNotFoundException("QuestionCounter with user " + user.getUsername() + " doesn't exists!"));
////            if (questionCounter.getTime() > Integer.min(5, questions.size())) {
////                questionCounter.setTime(0);
////                questionCounterRepository.save(questionCounter);
////            }
////            if (questionCounter.getTime() == 0) {
////                questionPull.add(question);
////            }
////        }
////    }
//
//    @Override
//    public GameDto answerInfluence(Long answerId, Long gameId) {
//        Game game = gameRepository.findById(gameId)
//                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " not found"));
//        Answer answer = answerRepository.findById(answerId)
//                .orElseThrow(() -> new EntityNotFoundException("Answer with id " + answerId + " not found"));
//
//        answer.setCounter(answer.getCounter()+1);
//        answerRepository.save(answer);
//
//        gameParameterRepository.findAllByGame(game)
//                .forEach(o ->
//                {
//                    o.setValue(Integer.min(o.getParameter().getHighestValue(),
//                            o.getValue() + answerParameterRepository
//                                    .findByTitleAndAnswer(o.getParameter().getTitle(), answer)
//                                    .orElseThrow(() -> new EntityNotFoundException("AnswerParameter with title " + o.getParameter().getTitle() + " not found"))
//                                    .getValue()));
//                    o.setValue(Integer.max(o.getParameter().getLowestValue(),
//                            o.getValue()));
//                });
//
//        String username = authorizationService.getProfileOfCurrent().getUsername();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
//        user.getUserParameters()
//                .forEach(o -> o.setValue(Integer.max(0,
//                        o.getValue() + answerUserParameterRepository
//                                .findByTitleAndAnswer(o.getTitle(), answer)
//                                .orElseThrow(() -> new EntityNotFoundException("AnswerUserParameter with title " + o.getTitle() + " not found"))
//                                .getValue())));
//
//        gameRepository.save(game);
//        return mapToDto(game, answer);
//    }
//
//    @Override
//    public boolean deleteById(Long gameId) {
//        String username = authorizationService.getProfileOfCurrent().getUsername();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
//
//        Game game = gameRepository.findById(gameId)
//                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " not found"));
//        List<Question> questions = questionRepository.findAllByGamePatternId(game.getGamePattern().getId());
//        for (Question question: questions) {
//            QuestionCounter questionCounter = questionCounterRepository.findByQuestionAndUserId(question, user.getId())
//                    .orElseThrow(() -> new EntityNotFoundException("QuestionCounter with userId " + user.getId() + " not found"));
//            questionCounter.setUser(null);
//            questionCounter.setQuestion(null);
//            questionCounterRepository.save(questionCounter);
//            questionCounterRepository.delete(questionCounter);
//        }
//        game.getParameters().stream()
//                .map(GameParameter::getId)
//                .forEach(gameParameterService::deleteById);
//
//        game.setUser(null);
//        game.setGamePattern(null);
//        game.setGameRequest(null);
//        game.setParameters(null);
//        game.setQuestionsPull(new ArrayList<>());
//        gameRepository.save(game);
//        gameRepository.delete(game);
//        return true;
//    }
//
//    @Override
//    public void freeData() {
//        List<Game> games = gameRepository.findAll();
//        games.forEach(gameRepository::delete);
//    }
//
//    @Override
//    public void freeDataByGamePattern(GamePattern gamePattern) {
//        List<Game> games = gameRepository.findAllByGamePattern(gamePattern);
//        games.forEach(gameRepository::delete);
//    }
//
//}
