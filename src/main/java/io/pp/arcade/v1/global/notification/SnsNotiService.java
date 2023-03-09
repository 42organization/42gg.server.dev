package io.pp.arcade.v1.global.notification;

import io.pp.arcade.v1.domain.noti.Noti;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.notification.slackbot.SlackSendException;
import io.pp.arcade.v1.global.notification.slackbot.SlackbotService;
import io.pp.arcade.v1.global.type.SnsType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsNotiService {
    private final NotiMailSender notiMailSender;
    private final SlackbotService slackbotService;

    public void sendSnsNotification(Noti noti, User user) {
        SnsType userSnsNotiOpt = user.getSnsNotiOpt();
        if (userSnsNotiOpt == SnsType.NONE)
            return;
        if(userSnsNotiOpt == SnsType.EMAIL)
            notiMailSender.sendMail(noti, user);
        else if (userSnsNotiOpt == SnsType.SLACK)
            slackbotService.sendSlackNoti(user.getIntraId(), noti);
        else if (userSnsNotiOpt == SnsType.BOTH) {
            notiMailSender.sendMail(noti, user);
            slackbotService.sendSlackNoti(user.getIntraId(), noti);
        }
    }
}
