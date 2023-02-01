package io.pp.arcade.v1.admin.users.service;

import io.pp.arcade.v1.admin.users.dto.*;
import io.pp.arcade.v1.admin.users.repository.UserAdminRepository;
import io.pp.arcade.v1.domain.rank.RankRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.exception.BusinessException;
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
    private final UserAdminRepository userAdminRepository;

    @Transactional
    public List<UserAdminDto> findAll() {
        List<User> users = userAdminRepository.findAll();
        List<UserAdminDto> userAdminDtos = users.stream().map(UserAdminDto::from).collect(Collectors.toList());
        return userAdminDtos;
    }
    @Transactional
    public UserAdminDto findByIntraId(UserFindAdminDto findDto) {
        User user = userAdminRepository.findByIntraId(findDto.getIntraId()).orElseThrow(() -> new BusinessException("E0001"));
        return UserAdminDto.from(user);
    }

    @Transactional
    public UserAdminDto findById(UserFindAdminDto findDto) {
        User user = userAdminRepository.findById(findDto.getUserId()).orElseThrow(() -> new BusinessException("E0001"));
        return UserAdminDto.from(user);
    }

    /* 문자열을 포함하는 intraId를 가진 유저 찾기 */
    @Transactional
    public UserSearchResponseAdminDto findByPartsOfIntraId(UserSearchRequestAdminDto userSearchDto, Pageable pageable) {
        Page<UserSearchAdminDto> result;
        Page<User> users = userAdminRepository.findByIntraIdContains(userSearchDto.getIntraId(), pageable);
        result = users.map(UserSearchAdminDto::from);
        return UserSearchResponseAdminDto.builder()
                .userSearchAdminDtos(result.getContent())
                .totalPage(result.getTotalPages())
                .currentPage(result.getNumber())
                .build();
    }

    @Transactional
    public UserSearchResponseAdminDto findUserByAdmin(Pageable pageable) {
        Page<User> users = userAdminRepository.findAll(pageable);
        Page<UserSearchAdminDto> page = users.map(UserSearchAdminDto::from);
        return UserSearchResponseAdminDto.builder()
                .userSearchAdminDtos(page.getContent())
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .build();
    }

    @Transactional
    public void updateUserDetailByAdmin(UserUpdateRequesAdmintDto updateRequestDto) {
        User user = userAdminRepository.findById(updateRequestDto.getUserId()).orElseThrow();
        user.setEMail(updateRequestDto.getEMail());
        user.setRacketType(updateRequestDto.getRacketType());
        user.setStatusMessage(updateRequestDto.getStatusMessage());
        user.setRoleType(updateRequestDto.getRoleType());


    }
}
