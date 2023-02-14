package io.pp.arcade.v1.admin.users.controller;


import io.pp.arcade.v1.admin.users.dto.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface UserAdminController {

    UserDetailResponseAdminDto userFindDetail(@PathVariable String intraId, @RequestBody UserDetailRequestAdminDto userdetailrequest, HttpServletRequest request);
    UserSearchResponseAdminDto userAll(HttpServletRequest request, @RequestParam(value = "q", required = false) String keyword, @RequestParam(value = "page") Long page);
    void userDetailUpdate(@RequestPart UserUpdateRequestAdmintDto userUpdateDto, @RequestPart MultipartFile multipartFile, HttpServletRequest request);
    UserSearchResultAdminResponseDto userSearchResult(@RequestParam(value="q", required = false, defaultValue = "") String inquiringString/* HttpServletRequest request*/);
}
