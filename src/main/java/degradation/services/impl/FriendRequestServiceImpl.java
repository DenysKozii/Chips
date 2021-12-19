package degradation.services.impl;


import degradation.entity.FriendRequest;
import degradation.entity.User;
import degradation.enums.RequestStatus;
import degradation.exception.EntityNotFoundException;
import degradation.pagination.PageDto;
import degradation.pagination.PagesUtility;
import degradation.repositories.FriendRequestRepository;
import degradation.repositories.UserRepository;
import degradation.services.AuthorizationService;
import degradation.services.FriendRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Override
    public boolean inviteByUsername(String friendUsername) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findByInvitorUsernameAndAcceptorUsername(username, friendUsername);
        if (friendRequestOptional.isEmpty()) {
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setInvitorUsername(username);
            friendRequest.setAcceptorUsername(friendUsername);
            friendRequest.setStatus(RequestStatus.WAITING);
            friendRequestRepository.save(friendRequest);
            return true;
        }
        return false;
    }

    @Override
    public boolean acceptByUsername(String friendUsername) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        FriendRequest friendRequest = friendRequestRepository.findByInvitorUsernameAndAcceptorUsername(username, friendUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Friend request by user username %s and friend username %s doesn't exists!", username, friendUsername)));
        if (!RequestStatus.WAITING.equals(friendRequest.getStatus()))
            return false;
        friendRequest.setStatus(RequestStatus.ACCEPTED);
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username %s not found!", username)));
        User friend = userRepository.findByUsername(friendUsername).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with username %s not found!", friendUsername)));
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        userRepository.save(user);
        userRepository.save(friend);
        friendRequestRepository.save(friendRequest);
        return true;
    }

    @Override
    public boolean deleteByUsername(String friendUsername) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        FriendRequest friendRequest = friendRequestRepository.findByInvitorUsernameAndAcceptorUsername(username, friendUsername)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Friend request by user username %s and friend username %s doesn't exists!", username, friendUsername)));
        friendRequestRepository.delete(friendRequest);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + friendUsername + " doesn't exists!"));

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        userRepository.save(user);
        userRepository.save(friend);
        return true;
    }

    @Override
    public PageDto<String> acceptList(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        Page<FriendRequest> result = friendRequestRepository.findAllByAcceptorUsername(username, PagesUtility.createPageableUnsorted(page, pageSize));
        List<String> content = result.getContent().stream()
                .filter(o->RequestStatus.WAITING.equals(o.getStatus()))
                .map(FriendRequest::getInvitorUsername)
                .collect(Collectors.toList());
        return PageDto.of(result.getTotalElements(), page, content);

    }

    @Override
    public PageDto<String> inviteList(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        Page<FriendRequest> result = friendRequestRepository.findAllByInvitorUsername(username, PagesUtility.createPageableUnsorted(page, pageSize));
        List<String> content = result.getContent().stream()
                .filter(o->RequestStatus.WAITING.equals(o.getStatus()))
                .map(FriendRequest::getAcceptorUsername)
                .collect(Collectors.toList());
        return PageDto.of(result.getTotalElements(), page, content);
    }

}