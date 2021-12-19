package degradation.controllers;

import degradation.dto.UserDto;
import degradation.dto.user.LoginRequest;
import degradation.dto.user.UserProfileDto;
import degradation.jwt.JwtProvider;
import degradation.services.AuthorizationService;
import degradation.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final AuthorizationService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        userService.login(loginRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtProvider.generateToken(loginRequest.getUsername()));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public UserProfileDto profile() {
        return authService.getProfileOfCurrent();
    }

}
