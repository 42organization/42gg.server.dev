package io.pp.arcade.v1.domain.admin.controller;

import io.pp.arcade.v1.domain.admin.dto.create.PChangeCreateRequestDto;
import io.pp.arcade.v1.domain.admin.dto.update.PChangeUpdateRequestDto;
import io.pp.arcade.v1.domain.pchange.dto.PChangeDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PChangeAdminController {
    void pChangeCreate(@RequestBody PChangeCreateRequestDto pChangeCreateDto, HttpServletRequest request);
    void pChangeUpdate(@RequestBody PChangeUpdateRequestDto pChangeUpdateDto, HttpServletRequest request);
    void pChangeDelete(@PathVariable Integer pChangeId, HttpServletRequest request);
    List<PChangeDto> pChangeAll(Pageable pageable, HttpServletRequest request);
}
