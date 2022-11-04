package io.pp.arcade.v1.global.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class UserImageHandlerTest {
    @Autowired
    private UserImageHandler userImageHandler;

    @Test
    void downloadImage() throws IOException {
        System.out.println(userImageHandler.uploadAndGetS3ImageUri("abobopong", "https://cdn.intra.42.fr/users/96890e2ba29f71d8459facbcb8f2d0a1/hakim.jpg"));
    }
}