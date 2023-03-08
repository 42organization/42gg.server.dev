package io.pp.arcade.v1.global.notification;

import io.pp.arcade.v1.domain.noti.Noti;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.NotiType;
import io.pp.arcade.v1.global.util.AsyncMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class NotiMailSender {

    private final JavaMailSender javaMailSender;
    private final AsyncMailSender asyncMailSender;

    public void sendMail(Noti noti, User user) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("핑퐁요정🧚으로부터 도착한 편지");
        helper.setTo(user.getEMail());
        if (noti.getType() != NotiType.ANNOUNCE) {
            helper.setText("🧚: \"새로운 알림이 도착했핑.\"\n" + "🧚: \"" + noti.getType().getMessage() + "\"\n\n 🏓42GG와 함께하는 행복한 탁구생활🏓" +
                    "\n$$지금 즉시 접속$$ ----> https://42gg.kr");
        } else {
            helper.setText("🧚: \"새로운 알림이 도착했핑.\"\n" + "🧚: \"" + noti.getType().getMessage() + "\"\n\n공지사항: " + noti.getMessage() + "\n\n 🏓42GG와 함께하는 행복한 탁구생활🏓" +
                    "\n$$지금 즉시 접속$$ ----> https://42gg.kr");
        }
        asyncMailSender.send(message);
    }
}
