package io.gg.arcade.domain.user.service;

import io.gg.arcade.domain.user.dto.UserAddRequestDto;
import io.gg.arcade.domain.user.dto.UserModifyPppRequestDto;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Transactional
    @Override
    public void UserModifyPpp(UserModifyPppRequestDto dto){
        User user = userRepository.findById(dto.getUserId()).orElseThrow(RuntimeException::new);
        user.setPpp(dto.getPpp());
    }

    @Transactional
    @Override
    public void addUser(UserAddRequestDto dto) {
        userRepository.save(User.builder()
                .intraId(dto.getIntraId())
                .userImgUri(dto.getUserImgUri())
                .racketType("pen")
                .statusMessage("")
                .isPlaying(false)
                .ppp(1000)
                .build()
        );
    }
}
