package devPilot.backend.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import devPilot.backend.Security.CurrentUser;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import devPilot.backend.dto.UserResponse;
import devPilot.backend.Security.AppUserPrincipal;
import devPilot.backend.entity.User;


@RestController
@RequestMapping("/api/auth") 
@RequiredArgsConstructor 
public class AuthController {
    private final CurrentUser currentUser;

    @GetMapping("/login-url")
    public Map<String, String> getLoginUrl() {
           
        return Map.of("url", "/oauth2/authorization/github");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        AppUserPrincipal principal = currentUser.require();
        User user = principal.getUser();
        return ResponseEntity.ok(
            new UserResponse(
            user.getId(),
            user.getGithubId(),
            user.getGithubUsername(),
            user.getDisplayName(),
            user.getAvatarUrl()
        ));
    }
}
