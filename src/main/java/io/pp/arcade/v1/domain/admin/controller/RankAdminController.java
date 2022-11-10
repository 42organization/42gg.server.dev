package io.pp.arcade.v1.domain.admin.controller;

import io.pp.arcade.v1.domain.admin.dto.create.RankCreateRequestDto;
import io.pp.arcade.v1.domain.admin.dto.update.RankUpdateRequestDto;
import io.pp.arcade.v1.domain.rank.dto.RankDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RankAdminController {
    /*
    void rankCreate(@RequestBody RankCreateRequestDto rankCreateRequestDto, HttpServletRequest request);
    void rankUpdate(@RequestBody RankUpdateRequestDto rankUpdateDto, HttpServletRequest request);
    void rankDelete(@PathVariable Integer rankId, HttpServletRequest request);
    */
    List<RankDto> rankAll(Pageable pageable, HttpServletRequest request);
}
