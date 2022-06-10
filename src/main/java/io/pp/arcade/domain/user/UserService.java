package io.pp.arcade.domain.user;

import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserModifyPppRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto findByIntraId(String intraId) {
        User user = userRepository.findByIntraId(intraId).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        return UserDto.from(user);
    }

    public UserDto findById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        return UserDto.from(user);
    }

    /* 유저 등록 */
    public void addUser(String intraId) {
        User user = User.builder()
                .intraId(intraId)
                .statusMessage("")
                .ppp(1)
                .build();
        userRepository.save(user);
    }

    /* 유저 ppp 변경 */
    public void modifyUserPpp(UserModifyPppRequestDto modifyDto) {
        User user = userRepository.findById(modifyDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수 입니다."));
        user.update(modifyDto.getPpp());
    }
}
