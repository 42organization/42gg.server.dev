package io.pp.arcade.v1.global.util;

import io.pp.arcade.v1.domain.user.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncNewUserImageUploader {
    private final UserImageHandler userImageHandler;
    private final UserRepository userRepository;

    public AsyncNewUserImageUploader(UserImageHandler userImageHandler, UserRepository userRepository) {
        this.userImageHandler = userImageHandler;
        this.userRepository = userRepository;
    }

    @Async("asyncExecutor")
    public void upload(String intraId, String imageUrl) {
        String s3ImageUrl = userImageHandler.uploadAndGetS3ImageUri(intraId, imageUrl);
        if ("https://42gg-public-image.s3.ap-northeast-2.amazonaws.com/images/small_default.jpeg".equals(s3ImageUrl)) {
            return ;
        }
        userRepository.findByIntraId(intraId).ifPresent(user -> {
            user.setImageUri(s3ImageUrl);
            userRepository.save(user);
        });
    }
}
