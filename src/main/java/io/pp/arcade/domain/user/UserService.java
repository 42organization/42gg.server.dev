package io.pp.arcade.domain.user;

import io.pp.arcade.domain.admin.dto.create.UserCreateDto;
import io.pp.arcade.domain.user.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto findByIntraId(UserFindDto findDto) {
        User user = userRepository.findByIntraId(findDto.getIntraId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        return UserDto.from(user);
    }

    @Transactional
    public UserDto findById(UserFindDto findDto) {
        User user = userRepository.findById(findDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수입니다."));
        return UserDto.from(user);
    }

    /* 유저 등록 */
    @Transactional
    public void addUser(UserAddDto addDto) {
        User user = User.builder()
                .intraId(addDto.getIntraId())
                .eMail(addDto.getEMail())
                .statusMessage("")
                .ppp(0)
                .build();
        userRepository.save(user);
    }

    /* 유저 ppp 변경 */
    @Transactional
    public void modifyUserPpp(UserModifyPppDto modifyDto) {
        User user = userRepository.findById(modifyDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수 입니다."));
        user.setPpp(modifyDto.getPpp());
    }

    /* 유저 정보 업데이트 */
    public void modifyUserProfile(UserModifyProfileDto modifyDto) {
        User user = userRepository.findById(modifyDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("잘못된 매개변수 입니다."));
        user.setImageUri(modifyDto.getUserImageUri());
        user.setRacketType(modifyDto.getRacketType());
        user.setStatusMessage(modifyDto.getStatusMessage());
    }

    /* 문자열을 포함하는 intraId를 가진 유저 찾기 */
    @Transactional
    public List<UserDto> findByPartsOfIntraId(UserSearchDto userSearchDto) {
        List<User> users = userRepository.findByIntraIdContains(userSearchDto.getIntraId());
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }

    @Transactional
    public void createUserByAdmin(UserCreateDto userCreateDto) {
        User user = User.builder()
                .intraId(userCreateDto.getIntraId())
                .eMail(userCreateDto.getEMail())
                .imageUri(userCreateDto.getUserImageUri())
                .racketType(userCreateDto.getRacketType())
                .statusMessage(userCreateDto.getStatusMessage() == null ? "" : userCreateDto.getStatusMessage())
                .ppp(userCreateDto.getPpp() == null ? 0 : userCreateDto.getPpp())
                .build();
        userRepository.save(user);
    }

    @Transactional
    public List<UserDto> findUserByAdmin(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserDto> userDtos = users.stream().map(UserDto::from).collect(Collectors.toList());
        return userDtos;
    }
}
