package degradation.services;

import degradation.dto.UserDto;
import degradation.dto.user.LoginRequest;
import degradation.dto.user.UserProfileDto;
import degradation.entity.User;
import degradation.pagination.PageDto;

public interface UserService {

    boolean login(LoginRequest request);

    boolean addUser(UserDto user);

    PageDto<String> getFriendsByUsername(int page, int pageSize);

    User loadUserByUsername(String login);
}
