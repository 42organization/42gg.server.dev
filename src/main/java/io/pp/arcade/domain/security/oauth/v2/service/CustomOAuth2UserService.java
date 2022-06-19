package io.pp.arcade.domain.security.oauth.v2.service;

import io.pp.arcade.domain.rank.RankRedis;
import io.pp.arcade.domain.rank.dto.RankAddDto;
import io.pp.arcade.domain.security.oauth.v2.domain.ProviderType;
import io.pp.arcade.domain.security.oauth.v2.domain.UserPrincipal;
import io.pp.arcade.domain.security.oauth.v2.info.OAuthUserInfo;
import io.pp.arcade.domain.security.oauth.v2.info.OAuthUserInfoFactory;
import io.pp.arcade.domain.user.User;
import io.pp.arcade.domain.user.UserRepository;
import io.pp.arcade.global.type.GameType;
import io.pp.arcade.global.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.keyOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuthUserInfo userInfo = OAuthUserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        String intraId = userInfo.getIntraId();
        User savedUser = userRepository.findByIntraId(userInfo.getIntraId())
                .orElse(null);
        if (savedUser != null)
        {
            /*
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }*/
            updateUser(savedUser , userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
            createRank(savedUser);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private void createRank(User savedUser) {
        if (redisTemplate.opsForValue().get(savedUser.getIntraId() + GameType.SINGLE) == null) {
            RankRedis singleRank =  RankRedis.from(savedUser, GameType.SINGLE);
            RankRedis doubleRank =  RankRedis.from(savedUser, GameType.DOUBLE);
            redisTemplate.opsForValue().set(savedUser.getIntraId() + GameType.SINGLE, singleRank);
            redisTemplate.opsForValue().set(savedUser.getIntraId() + GameType.DOUBLE, doubleRank);
        }
        if (redisTemplate.opsForZSet().getOperations() == null) {
            redisTemplate.opsForZSet().add(GameType.SINGLE.getKey(), savedUser.getIntraId() + GameType.SINGLE, savedUser.getPpp());
            redisTemplate.opsForZSet().add(GameType.DOUBLE.getKey(), savedUser.getIntraId() + GameType.DOUBLE, savedUser.getPpp());
        }
    }

    private User createUser(OAuthUserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .intraId(userInfo.getIntraId())
                .roleType(RoleType.USER)
                .imageUri(userInfo.getImageUrl())
                .statusMessage("")
                .ppp(1000)
                .eMail(userInfo.getEmail())
                .build();
        return userRepository.saveAndFlush(user);
    }

    private User updateUser(User user, OAuthUserInfo userInfo) {
        if (userInfo.getIntraId() != null && !user.getIntraId().equals(userInfo.getIntraId())) {
            user.setIntraId(userInfo.getIntraId());
        }
        return user;
    }
}