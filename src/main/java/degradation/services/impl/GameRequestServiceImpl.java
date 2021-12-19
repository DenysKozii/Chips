//package degradation.services.impl;
//
//
//import com.company.archon.dto.GameDto;
//import com.company.archon.dto.GameRequestDto;
//import com.company.archon.entity.GameRequest;
//import com.company.archon.entity.User;
//import com.company.archon.enums.GameRequestStatus;
//import com.company.archon.exception.EntityNotFoundException;
//import com.company.archon.mapper.GameRequestMapper;
//import com.company.archon.pagination.PageDto;
//import com.company.archon.pagination.PagesUtility;
//import com.company.archon.repositories.GameRequestRepository;
//import com.company.archon.repositories.UserRepository;
//import com.company.archon.services.AuthorizationService;
//import com.company.archon.services.GameRequestService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//@AllArgsConstructor
//public class GameRequestServiceImpl implements GameRequestService {
//
//    private final GameRequestRepository gameRequestRepository;
//    private final UserRepository userRepository;
//    private final AuthorizationService authorizationService;
//
//    @Override
//    public GameRequestDto createGameRequest( String friendUsername, Long gamePatternId) {
//        String username = authorizationService.getProfileOfCurrent().getUsername();
//        GameRequest gameRequest = new GameRequest();
//        gameRequest.setInvitorUsername(username);
//        gameRequest.setAcceptorUsername(friendUsername);
//        gameRequest.setGamePatternId(gamePatternId);
//        gameRequest.setStatus(GameRequestStatus.WAITING);
//        gameRequestRepository.save(gameRequest);
//        return GameRequestMapper.INSTANCE.mapToDto(gameRequest);
//    }
//
//    @Override
//    public boolean acceptGameRequest(Long gameRequestId) {
//        GameRequest gameRequest = gameRequestRepository.findById(gameRequestId)
//                .orElseThrow(() -> new EntityNotFoundException("GameRequest with id " + gameRequestId + " not found"));
//        gameRequest.setStatus(GameRequestStatus.ACCEPTED);
//        gameRequestRepository.save(gameRequest);
//        return true;
//    }
//
//    @Override
//    public boolean rejectGameRequest(Long gameRequestId) {
//        GameRequest gameRequest = gameRequestRepository.findById(gameRequestId)
//                .orElseThrow(() -> new EntityNotFoundException("GameRequest with id " + gameRequestId + " not found"));
//        gameRequestRepository.delete(gameRequest);
//        return true;
//    }
//
//    @Override
//    public GameDto startGameRequest(Long gameRequestId) {
//        GameRequest gameRequest = gameRequestRepository.findById(gameRequestId)
//                .orElseThrow(() -> new EntityNotFoundException("GameRequest with id " + gameRequestId + " not found"));
//        User invitor = userRepository.findByUsername(gameRequest.getInvitorUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User with username " + gameRequest.getInvitorUsername() + " doesn't exists!"));
//        User acceptor = userRepository.findByUsername(gameRequest.getAcceptorUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User with username " + gameRequest.getAcceptorUsername() + " doesn't exists!"));
//
//
//        return null;
//    }
//
//    @Override
//    public PageDto<GameRequestDto> findAllByUsername(int page, int pageSize) {
//        String username = authorizationService.getProfileOfCurrent().getUsername();
//        Page<GameRequest> result = gameRequestRepository.findAllByAcceptorUsername(username, PagesUtility.createPageableUnsorted(page, pageSize));
//        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
//    }
//
//    private List<GameRequestDto> mapToDto(List<GameRequest> content) {
//        return content.stream()
//                .map(GameRequestMapper.INSTANCE::mapToDto)
//                .collect(Collectors.toList());
//    }
//}