package io.pp.arcade.v1.domain.guest;

import io.pp.arcade.v1.domain.rank.service.RankService;
import io.pp.arcade.v1.domain.security.jwt.Token;
import io.pp.arcade.v1.domain.security.jwt.TokenRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.type.RacketType;
import io.pp.arcade.v1.global.type.RoleType;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pingpong")
public class GuestLoginController {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RankService rankService;
    private final DogUtil dogUtil;

    @Value("${info.web.frontUrl}")
    private String frontUrl;

//    private final String frontUrl = "http://localhost:3000";
    
    // 1. /login/guest 맵핑을 받는다
    //   현재 유저의 로컬스토리지에 토큰이 있는지 확인하고 있으면 메인페이지로 리다이렉트
    @GetMapping(value = "/login/guest")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken;
        // 유저가 토큰을 가지고 있는지 확인
        accessToken = HeaderUtil.getAccessToken(request);
        // 있다면 저장소에 토큰이 있는지 확인
        if (accessToken != null && tokenRepository.existsByAccessToken(accessToken)) {
            redirectStrategy.sendRedirect(request, response, frontUrl);
            return ;
        }
        // 저장소에 토큰이 없거나 아예 토큰을 가지고 있지 않다면 새로 생성
        // 토큰생성
        accessToken = createAccessToken();
        // 유저 & 유저 랭크 생성
        User user = userCreate();
        // 토큰을 저장소에 저장
        tokenRepository.save(new Token(user, accessToken, accessToken));
        // 리다이렉트
        redirectStrategy.sendRedirect(request, response, redirectUrl(accessToken));
    }

    // 2. 토큰이 없으면 토큰을 생성 (UUID)
    private String createAccessToken() {
        return UUID.randomUUID().toString();
    }

    // 3. 유저를 생성
    //   - 유저네임은 랜덤으로 생성 (랜덤 형용사 + 개이름)
    //   - 유저의 프로필 이미지는 개이름에 맞는 개 이미지로 설정
    private User userCreate() {
        String dog = dogUtil.getRandomDog();
        String dogImage = dogUtil.getRandomDogImage(dog);
        String dogName;
        do { dogName = dogUtil.getRandomDogName(dog); }
        while (userRepository.existsByIntraId(dogName));


        User user = User.builder()
                .intraId(dogName)
                .roleType(RoleType.USER)
                .imageUri(dogImage)
                .statusMessage("멍멍")
                .racketType(RacketType.NONE)
                .ppp(1000)
                .totalExp(0)
                .eMail("")
                .build(); // 서비스에서 해결
        // 4. 토큰과 유저를 db에 저장
        userRepository.saveAndFlush(user); // 서비스에서 해결
        // 5. 유저의 랭크를 생성 (레디스)
        rankService.addUserRank(UserDto.from(user));
        return user;
    }

    // 6. 프론트 url에 유저의 토큰을 쿼리 파라미터로 넣어서 리다이렉트
    private String redirectUrl(String accessToken) {
        return frontUrl + "/?token=" + accessToken;
    }
}
