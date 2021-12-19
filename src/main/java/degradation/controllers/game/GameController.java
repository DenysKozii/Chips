//package degradation.controllers.game;
//
//import com.fate.dto.GameDto;
//import com.fate.pagination.PageDto;
//import com.fate.services.GameService;
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/game")
//public class GameController {
//
//    private final GameService gameService;
//
//    @PostMapping("/new/{gamePatternId}")
//    public GameDto newGame(@PathVariable Long gamePatternId) {
//        return gameService.startNewGame(gamePatternId);
//    }
//
//    @PostMapping("/answer/{gameId}/{answerId}")
//    public GameDto checkAnswer(@PathVariable Long gameId,
//                               @PathVariable Long answerId) {
//        return gameService.answerInfluence(answerId, gameId);
//    }
//
//    @PostMapping("/save/{gameId}")
//    public GameDto saveGame(@PathVariable Long gameId) {
//        return gameService.saveGame(gameId);
//    }
//
//    @GetMapping("/saved")
//    public PageDto<GameDto> savedGames(@RequestParam(defaultValue = "0", required = false) int page,
//                                       @RequestParam(defaultValue = "10", required = false) int pageSize) {
//        return gameService.savedGames(page, pageSize);
//    }
//
//    @GetMapping("/load/{gameId}")
//    public GameDto loadGame(@PathVariable Long gameId) {
//        return gameService.loadGame(gameId);
//    }
//
//}
