package degradation.services;


import degradation.pagination.PageDto;

public interface FriendRequestService {
    boolean inviteByUsername(String friendUsername);

    boolean acceptByUsername(String friendUsername);

    boolean deleteByUsername(String friendUsername);

    PageDto<String> acceptList(int page, int pageSize);

    PageDto<String> inviteList(int page, int pageSize);

}