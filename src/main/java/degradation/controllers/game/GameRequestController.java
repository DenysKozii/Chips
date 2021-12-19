//package degradation.controllers.game;
//
//import degradation.dto.GameRequestDto;
//import degradation.pagination.PageDto;
//import lombok.AllArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/gameRequest")
//public class GameRequestController {
//
//    private final GameRequestService gameRequestService;
//
//    @GetMapping("/list")
//    public PageDto<GameRequestDto> getGameRequestsByUser(@RequestParam(defaultValue = "0", required = false) int page,
//                                                         @RequestParam(defaultValue = "10", required = false) int pageSize) {
//        return gameRequestService.findAllByUsername(page, pageSize);
//    }
//
//    @PostMapping("/new/{gamePatternId}")
//    public GameRequestDto newGameWithFriend(@PathVariable Long gamePatternId,
//                                            @RequestParam String friendUsername) {
//        return gameRequestService.createGameRequest(friendUsername, gamePatternId);
//    }
//
//    @PostMapping("/accept/{gameRequestId}")
//    public boolean acceptGameRequest(@PathVariable Long gameRequestId) {
//        return gameRequestService.acceptGameRequest(gameRequestId);
//    }
//
//    @PostMapping("/reject/{gameRequestId}")
//    public boolean rejectGameRequest(@PathVariable Long gameRequestId) {
//        return gameRequestService.rejectGameRequest(gameRequestId);
//    }
//
//    @PostMapping("/start/{gameRequestId}")
//    public GameDto startGameByRequest(@PathVariable Long gameRequestId) {
//        return gameRequestService.startGameRequest(gameRequestId);
//    }
//
//
//
//}
