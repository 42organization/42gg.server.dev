package io.pp.arcade.v1.admin.users.service;

import io.pp.arcade.v1.admin.users.dto.*;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.global.type.RoleType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserAdminService {
    private final UserRepository userRepository;

    @Transactional
    public List<UserAdminDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserAdminDto> userAdminDtos = users.stream().map(UserAdminDto::from).collect(Collectors.toList());
        return userAdminDtos;
    }
    @Transactional
    public UserAdminDto findByIntraId(UserFindAdminDto findDto) {
        User user = userRepository.findByIntraId(findDto.getIntraId()).orElseThrow(() -> new BusinessException("E0001"));
        return UserAdminDto.from(user);
    }

    @Transactional
    public UserAdminDto findById(UserFindAdminDto findDto) {
        User user = userRepository.findById(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        return UserAdminDto.from(user);
    }

    /* 문자열을 포함하는 intraId를 가진 유저 찾기 */
    @Transactional
    public UserSearchResponseAdminDto findByPartsOfIntraId(UserSearchRequestAdminDto userSearchDto, Pageable pageable) {
        Page<UserSearchAdminDto> result;
        Page<User> users = userRepository.findByIntraIdContains(userSearchDto.getIntraId(), pageable);
        result = users.map(UserSearchAdminDto::from);
        return UserSearchResponseAdminDto.builder()
                .userSearchAdminDtos(result.getContent())
                .totalPage(result.getTotalPages())
                .currentPage(result.getNumber())
                .build();
    }

    @Transactional
    public UserSearchResponseAdminDto findUserByAdmin(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserSearchAdminDto> page = users.map(UserSearchAdminDto::from);
        return UserSearchResponseAdminDto.builder()
                .userSearchAdminDtos(page.getContent())
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .build();
    }

    @Transactional
    public List<UserAdminDto> findAllByRoleType(RoleType roleType) {
        List<User> users = userRepository.findAllByRoleType(roleType);
        List<UserAdminDto> userAdminDtos = users.stream().map(UserAdminDto::from).collect(Collectors.toList());
        return userAdminDtos;
    }

    @Transactional
    public void updateUserByAdmin(UserUpdateRequesAdmintDto updateRequestDto) {
        User user = userRepository.findById(updateRequestDto.getUserId()).orElseThrow();
        user.setImageUri(updateRequestDto.getUserImageUri());
        user.setRacketType(updateRequestDto.getRacketType());
        user.setStatusMessage(updateRequestDto.getStatusMessage());
        user.setPpp(updateRequestDto.getPpp());
    }
}
