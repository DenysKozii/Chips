package degradation.services.impl;

import degradation.dto.UserDto;
import degradation.dto.user.LoginRequest;
import degradation.dto.user.UserProfileDto;
import degradation.entity.Role;
import degradation.entity.User;
import degradation.exception.EntityNotFoundException;
import degradation.pagination.PageDto;
import degradation.pagination.PagesUtility;
import degradation.repositories.UserRepository;
import degradation.services.AuthorizationService;
import degradation.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String PASSWORD = "GDJ1231@#";

    @Override
    public boolean login(LoginRequest request) {
        String username = request.getUsername();
        Optional<User> userFromDb = userRepository.findByUsername(username);
        log.info(String.format("Added user with %s username", username));
        User user;
        if (userFromDb.isPresent()) {
            user = userFromDb.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new EntityNotFoundException("Invalid Credentials");
            }
        } else {
            user = new User();
            user.setRole(Role.USER);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(PASSWORD));
            userRepository.save(user);
        }
        authorizationService.authorizeUser(user);
        return true;
    }

    public boolean addUser(UserDto userDto) {
        Optional<User> userFromDb = userRepository.findByUsername(userDto.getUsername());
        if (userFromDb.isPresent()) {
            User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() ->
                    new UsernameNotFoundException("Invalid Credentials"));
            authorizationService.authorizeUser(user);
            return true;
        } else{
            User user = new User();
            user.setRole(Role.USER);
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(PASSWORD));
            userRepository.save(user);
            authorizationService.authorizeUser(user);
        }
        return true;
    }

    @Override
    public PageDto<String> getFriendsByUsername(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        Page<User> result = userRepository.findFriendsByUsername(username, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
    }

    private List<String> mapToDto(List<User> users) {
        return users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

}
