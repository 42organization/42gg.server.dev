package io.pp.arcade.v1.admin.users.controller;


import io.pp.arcade.v1.admin.users.dto.*;
import org.apache.http.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UserAdminController {

    UserDetailResponseAdminDto userFindDetail(@PathVariable Integer userId);
    UserSearchResponseAdminDto userAll(@RequestParam(value = "q", required = false) String keyword, @RequestParam(value = "page") Long page, HttpResponse httpResponse);
    ResponseEntity userDetailUpdate(@RequestPart UserUpdateRequestAdmintDto userUpdateDto, @RequestPart(required = false) MultipartFile multipartFile) throws IOException;
    UserSearchResultAdminResponseDto userSearchResult(@RequestParam(value="q", required = false, defaultValue = "") String inquiringString);
}
