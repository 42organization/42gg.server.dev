package io.pp.arcade.v1.domain.noti.slackbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import static io.pp.arcade.v1.domain.noti.slackbot.SlackbotUtils.*;

@Service
public class SlackbotService {
    @Value("${slack.xoxbToken}")
    private String authenticationToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public SlackbotService(RestTemplateBuilder builder, ObjectMapper objectMapper) {
        this.restTemplate = builder.build();
        this.objectMapper = objectMapper;
    }

    private String getSlackUserId(String intraId) {
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
            throw new RuntimeException("fail to get slack user info");
        return responseEntity.getBody().user.id;
    }

    private String getDmChannelId(String slackUserId) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION,
                authenticationPrefix + authenticationToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("users", slackUserId);
        String contentBody = objectMapper.writeValueAsString(map);

        HttpEntity<String> entity = new HttpEntity<>(contentBody, httpHeaders);

        ResponseEntity<ConversationResponse> responseEntity = restTemplate
                .exchange(conversationsUrl, HttpMethod.POST, entity, ConversationResponse.class);
        if(!responseEntity.getBody().ok)
            throw new RuntimeException("fail to get user dm channel id");
        return responseEntity.getBody().channel.id;
    }

    @Async("asyncExecutor")
    public void sendSlackNoti(String intraId, String message) throws JsonProcessingException {
        String slackUserId = getSlackUserId(intraId);
        String slackChannelId = getDmChannelId(slackUserId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION,
                authenticationPrefix + authenticationToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("channel",slackChannelId);
        map.put("text", message);
        String contentBody = objectMapper.writeValueAsString(map);

        HttpEntity<String> entity = new HttpEntity<>(contentBody, httpHeaders);

        ResponseEntity<String> respEntity = restTemplate
                .exchange(sendMessageUrl, HttpMethod.POST, entity, String.class);
        if(respEntity.getStatusCode() != HttpStatus.OK)
            throw new RuntimeException("fail to send notification");
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
