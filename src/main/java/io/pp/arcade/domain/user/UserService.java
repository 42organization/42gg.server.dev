package io.pp.arcade.domain.user;

import io.pp.arcade.domain.user.dto.UserDto;
import io.pp.arcade.domain.user.dto.UserModifyPppDto;
import io.pp.arcade.domain.user.dto.UserSearchDto;
import io.pp.arcade.domain.user.dto.UserUpdateInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto findByIntraId(String intraId) {
        User user = userRepository.findByIntraId(intraId).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        return UserDto.from(user);
    }

    @Transactional
    public UserDto findById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        return UserDto.from(user);
    }

    /* 유저 등록 */
    @Transactional
    public void addUser(String intraId) {
        User user = User.builder()
                .intraId(intraId)
                .statusMessage("")
                .ppp(1000)
                .build();
        userRepository.save(user);
    }

    /* 유저 ppp 변경 */
    @Transactional
    public void modifyUserPpp(UserModifyPppDto modifyDto) {
        User user = userRepository.findById(modifyDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수 입니다."));
        user.update(modifyDto.getPpp());
    }

    /* 문자열을 포함하는 intraId를 가진 유저 찾기 */
    @Transactional
    public List<UserDto> findByPartsOfIntraId(UserSearchDto userSearchDto) {
        List<User> users = userRepository.findByIntraIdContains(userSearchDto.getIntraId());
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }

    /* 유저 정보 업데이트 */
    public void updateUserInfo(UserUpdateInfoDto updateDto) {

    }

}
