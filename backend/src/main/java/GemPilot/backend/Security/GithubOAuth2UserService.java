package GemPilot.backend.Security;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import GemPilot.backend.entity.User;
import GemPilot.backend.services.UserServices;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GithubOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // Implementation of the service that handles GitHub OAuth2 user information
    private final UserServices userServices;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    
     @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User githubUser = delegate.loadUser(userRequest);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        String scopes = userRequest.getAccessToken().getScopes() != null
                ? String.join(",", userRequest.getAccessToken().getScopes())
                : "read:user,repo";
    
         User user = userServices.upsertFromGitHub(githubUser.getAttributes() , accessToken , scopes);
         return new AppUserPrincipal(user , githubUser.getAttributes());
    }
    


    
}