package io.pp.arcade.v1.domain.security.oauth.v2.info.impl;


import io.pp.arcade.v1.domain.security.oauth.v2.info.OAuthUserInfo;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public class FortyTwoOAuthUserInfo extends OAuthUserInfo {

    @Value("${info.image.defaultUrl}")
    private String defaultImageUrl;

    public FortyTwoOAuthUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getIntraId() {
        return attributes.get("login").toString();
    }

    public String getEmail() {
        return attributes.get("email").toString();
    }

    public String getImageUrl() {
        if (attributes.get("image_url") == null) {
            return defaultImageUrl;
        }
        return attributes.get("image_url").toString();
    }
}
