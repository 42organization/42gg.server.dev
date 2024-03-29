package io.pp.arcade.v1.global.util;

import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class AsyncNewUserImageUploader {
    private final UserImageHandler userImageHandler;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${info.image.defaultUrl}")
    private String defaultImageUrl;

    public AsyncNewUserImageUploader(UserImageHandler userImageHandler, UserRepository userRepository) {
        this.userImageHandler = userImageHandler;
        this.userRepository = userRepository;
    }

    @Async("asyncExecutor")
    public void upload(String intraId, String imageUrl) {
        String s3ImageUrl = userImageHandler.uploadAndGetS3ImageUri(intraId, imageUrl);
        if (defaultImageUrl.equals(s3ImageUrl)) {
            return ;
        }
        userRepository.findByIntraId(intraId).ifPresent(user -> {
            if (s3ImageUrl == null) {
                user.setImageUri(defaultImageUrl);
            } else {
                user.setImageUri(s3ImageUrl);
            }
            userRepository.save(user);
        });
    }

    @Transactional
    @Async("asyncExecutor")
    public void update(String intraId, MultipartFile multipartFile) throws IOException {
        User user =  userRepository.getUserByIntraId(intraId);
        entityManager.lock(user, LockModeType.PESSIMISTIC_WRITE);
        String s3ImageUrl = userImageHandler.updateAndGetS3ImageUri(multipartFile, user);
        if (s3ImageUrl == null) {
            userRepository.updateUserImageUri(user.getId(), defaultImageUrl);
        } else {
            userRepository.updateUserImageUri(user.getId(), s3ImageUrl);
        }
        entityManager.lock(user, LockModeType.NONE);
        entityManager.flush();
        entityManager.clear();
    }
}
