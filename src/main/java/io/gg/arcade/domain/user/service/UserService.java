package io.gg.arcade.domain.user.service;

import io.gg.arcade.domain.user.dto.UserAddRequestDto;
import io.gg.arcade.domain.user.dto.UserDto;
import io.gg.arcade.domain.user.dto.UserModifyPppRequestDto;
import io.gg.arcade.domain.user.entity.User;
import io.gg.arcade.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void UserModifyPpp(UserModifyPppRequestDto dto){
        User user = userRepository.findById(dto.getUserId()).orElseThrow(RuntimeException::new);
        user.setPpp(dto.getPpp());
    }

    @Transactional
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

    @Transactional
    public UserDto findById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        return UserDto.builder()
                .id(user.getId())
                .intraId(user.getIntraId())
                .isPlaying(user.getIsPlaying())
                .userImgUri(user.getUserImgUri())
                .ppp(user.getPpp())
                .racketType(user.getRacketType())
                .statusMessage(user.getStatusMessage())
                .build();
    }

    @Transactional
    public UserDto findByIntraId(String userId) {
        User user = userRepository.findByIntraId(userId).orElseThrow(RuntimeException::new);
        return UserDto.builder()
                .id(user.getId())
                .intraId(user.getIntraId())
                .isPlaying(user.getIsPlaying())
                .userImgUri(user.getUserImgUri())
                .ppp(user.getPpp())
                .racketType(user.getRacketType())
                .statusMessage(user.getStatusMessage())
                .build();
    }
}
