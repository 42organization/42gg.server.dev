package io.pp.arcade.v1.domain.opponent.controller;

import io.pp.arcade.v1.domain.opponent.OpponentService;
import io.pp.arcade.v1.domain.security.jwt.TokenService;
import io.pp.arcade.v1.domain.slot.dto.OpponentResponseDto;
import io.pp.arcade.v1.global.util.HeaderUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/pingpong")
public class OpponentControllerImpl implements OpponentController {
    private final OpponentService opponentService;
    private final TokenService tokenService;

    @GetMapping("/match/opponent")
    public List<OpponentResponseDto> findOpponentList(HttpServletRequest request) {
        tokenService.findUserByAccessToken(HeaderUtil.getAccessToken(request));
        List<OpponentResponseDto> responseDtoList = new ArrayList<>();
        String[] toIntraId = {"daekim", "donghyuk", "hakim", "him", "jabae", "jihyukim", "jiyun", "kipark", "nheo", "rjeong", "salee2", "wochae"};
        while (responseDtoList.size() < 3) {
            OpponentResponseDto opponent = opponentService.findByIntraId(toIntraId[createRandomNumber()]);
            if (opponent.getIsReady())
                responseDtoList.add(opponent);
        }
        return responseDtoList;
    }
    private Integer createRandomNumber() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return random.nextInt() % 12;
    }
}
