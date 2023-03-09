package io.pp.arcade.v1.global.notification.slackbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pp.arcade.v1.domain.noti.Noti;
import io.pp.arcade.v1.global.type.NotiType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static io.pp.arcade.v1.global.notification.slackbot.SlackbotUtils.*;

@Service
@Slf4j
public class SlackbotService {
    @Value("${slack.xoxbToken}")
    private String authenticationToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public SlackbotService(RestTemplateBuilder builder, ObjectMapper objectMapper) {
        this.restTemplate = builder.build();
        this.objectMapper = objectMapper;
    }

    private String getSlackUserId(String intraId) throws SlackSendException {
        String userEmail = intraId + intraEmailSuffix;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, authenticationPrefix + authenticationToken);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("email", userEmail);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<SlackUserInfoResponse> responseEntity = restTemplate
                .exchange(userIdGetUrl, HttpMethod.POST, request, SlackUserInfoResponse.class);
        if (!responseEntity.getBody().ok)
            throw new SlackSendException("fail to get slack user info");
        return responseEntity.getBody().user.id;
    }

    private String getDmChannelId(String slackUserId) throws SlackSendException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION,
                authenticationPrefix + authenticationToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("users", slackUserId);
        String contentBody = null;
        try {
            contentBody = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new SlackSendException("json parse error in getDmChannelId()", e);
        }

        HttpEntity<String> entity = new HttpEntity<>(contentBody, httpHeaders);

        ResponseEntity<ConversationResponse> responseEntity = restTemplate
                .exchange(conversationsUrl, HttpMethod.POST, entity, ConversationResponse.class);
        if(!responseEntity.getBody().ok)
            throw new SlackSendException("fail to get user dm channel id");
        return responseEntity.getBody().channel.id;
    }

    @Async("asyncExecutor")
    public void sendSlackNoti(String intraId, Noti noti) {
        try {
            startSendNoti(intraId, noti);
        } catch (SlackSendException e) {
            log.error("SlackSendException message = {}", e.getMessage());
        }
    }

    private void startSendNoti(String intraId, Noti noti) throws SlackSendException {
        String slackUserId = getSlackUserId(intraId);
        String slackChannelId = getDmChannelId(slackUserId);
        String message = getMessage(noti);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION,
                authenticationPrefix + authenticationToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("channel",slackChannelId);
        map.put("text", message);
        String contentBody = null;
        try {
            contentBody = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new SlackSendException("json parse error in sendSlackNoti()", e);
        }

        HttpEntity<String> entity = new HttpEntity<>(contentBody, httpHeaders);

        ResponseEntity<String> respEntity = restTemplate
                .exchange(sendMessageUrl, HttpMethod.POST, entity, String.class);
        if(respEntity.getStatusCode() != HttpStatus.OK)
            throw new SlackSendException("fail to send notification");
    }

    private String getMessage(Noti noti) {
        String message;
        if (noti.getType() != NotiType.ANNOUNCE) {
            message = "🧚: \"새로운 알림이 도착했핑.\"\n" + "🧚: \"" + noti.getType().getMessage() + "\"\n\n 🏓42GG와 함께하는 행복한 탁구생활🏓" +
                    "\n$$지금 즉시 접속$$ ----> https://42gg.kr";
        } else {
            message = "🧚: \"새로운 알림이 도착했핑.\"\n" + "🧚: \"" + noti.getType().getMessage() + "\"\n\n공지사항: "
                    + noti.getMessage() + "\n\n 🏓42GG와 함께하는 행복한 탁구생활🏓" + "\n$$지금 즉시 접속$$ ----> https://42gg.kr";
        }
        return message;
    }

    @Getter
    static class ConversationResponse {
        private Boolean ok;
        private Channel channel;

        @Getter
        static class Channel {
            private String id;
        }

    }

    @Getter
    static class SlackUserInfoResponse {
        private Boolean ok;
        private SlackUser user;

        @Getter
        static class SlackUser{
            private String id;
        }
    }
}
