package io.pp.arcade.v1.domain.currentmatch;

import io.pp.arcade.v1.domain.admin.dto.create.CurrentMatchCreateRequestDto;
import io.pp.arcade.v1.domain.admin.dto.delete.CurrentMatchDeleteDto;
import io.pp.arcade.v1.domain.admin.dto.update.CurrentMatchUpdateRequestDto;
import io.pp.arcade.v1.domain.game.Game;
import io.pp.arcade.v1.domain.game.GameRepository;
import io.pp.arcade.v1.domain.game.dto.GameDto;
import io.pp.arcade.v1.domain.slot.Slot;
import io.pp.arcade.v1.domain.slot.SlotRepository;
import io.pp.arcade.v1.domain.slot.dto.SlotDto;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.v1.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.domain.user.UserRepository;
import io.pp.arcade.v1.domain.user.dto.UserDto;
import io.pp.arcade.v1.global.exception.BusinessException;
import io.pp.arcade.v1.domain.currentmatch.dto.*;
import io.pp.arcade.v1.global.type.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrentMatchService {
    private final CurrentMatchRepository currentMatchRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final SlotRepository slotRepository;
    private final SlotTeamUserRepository slotTeamUserRepository;

    @Transactional
    public void addCurrentMatch(CurrentMatchAddDto addDto){
        User user = userRepository.findById(addDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));
        if (user.getRoleType() == RoleType.ADMIN) {
            return ;
        }
        Slot slot = slotRepository.findById(addDto.getSlot().getId()).orElseThrow(() -> new BusinessException("E0001"));
        currentMatchRepository.save(CurrentMatch.builder()
                        .slot(slot)
                        .user(user)
                        .isMatched(false)
                        .matchImminent(false)
                        .isDel(false)
                        .build());
    }

    @Transactional
    public void modifyCurrentMatch(CurrentMatchModifyDto modifyDto){
        List<CurrentMatch> currentMatchList = currentMatchRepository.findAllBySlotId(modifyDto.getSlot().getId());
        currentMatchList.forEach(currentMatch -> {
            currentMatch.setMatchImminent(modifyDto.getMatchImminent());
            currentMatch.setIsMatched(modifyDto.getIsMatched());
            currentMatch.setIsDel(currentMatch.getIsDel());
        });
    }

    @Transactional
    public void saveGameInCurrentMatch(CurrentMatchSaveGameDto saveDto) {
        Game game = gameRepository.findById(saveDto.getGame().getId()).orElseThrow(() -> new BusinessException("E0001"));
        List<SlotTeamUser> slotTeamUsers = slotTeamUserRepository.findAllBySlotId(game.getSlot().getId());
        slotTeamUsers.forEach(slotTeamUser -> {
            CurrentMatch currentMatch = currentMatchRepository.findByUserAndIsDel(slotTeamUser.getUser(), false).orElse(null);
            if (currentMatch != null) {
                currentMatch.setGame(game);
            }
        });
    }

    @Transactional
    public CurrentMatchDto findCurrentMatchByUser(UserDto userDto) {
//        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new BusinessException("E0001"));
        CurrentMatch currentMatch = currentMatchRepository.findByUserIdAndIsDel(userDto.getId(), false).orElse(null);
        CurrentMatchDto dto = null;

        if (currentMatch != null) {
            dto = CurrentMatchDto.builder()
                    .game(currentMatch.getGame() == null ? null : GameDto.from(currentMatch.getGame()))
                    .user(userDto)
                    .slot(SlotDto.from(currentMatch.getSlot()))
                    .matchImminent(currentMatch.getMatchImminent())
                    .isMatched(currentMatch.getIsMatched())
                    .build();
        }
        return dto;
    }

    @Transactional
    public CurrentMatchDto findCurrentMatchByIntraId(CurrentMatchFindByUserDto findByUserDto) {
        User user = userRepository.findByIntraId(findByUserDto.getUser().getIntraId()).orElseThrow(() -> new BusinessException("E0001"));
        CurrentMatch currentMatch = currentMatchRepository.findByUserAndIsDel(user, false).orElse(null);
        CurrentMatchDto dto = null;

        if (currentMatch != null) {
            dto = CurrentMatchDto.builder()
                    .game(currentMatch.getGame() == null ? null : GameDto.from(currentMatch.getGame()))
                    .user(UserDto.from(currentMatch.getUser()))
                    .slot(SlotDto.from(currentMatch.getSlot()))
                    .matchImminent(currentMatch.getMatchImminent())
                    .isMatched(currentMatch.getIsMatched())
                    .build();
        }
        return dto;
    }

    @Transactional
    public List<CurrentMatchDto> findCurrentMatchByGame(CurrentMatchFindByGameDto findDto) {
        List<CurrentMatch> matches = currentMatchRepository.findAllByGameId(findDto.getGame().getId());
        List<CurrentMatchDto> currentMatchDtos = matches.stream().map(CurrentMatchDto::from).collect(Collectors.toList());
        return currentMatchDtos;
    }

    @Transactional
    public void removeCurrentMatch(CurrentMatchRemoveDto removeDto) {
        if (removeDto.getSlot() == null) {
            User user = userRepository.findById(removeDto.getUser().getId()).orElseThrow(() -> new BusinessException("E0001"));
            CurrentMatch currentMatch = currentMatchRepository.findByUserAndIsDel(user, false).orElse(null);
            if (currentMatch != null) {
                currentMatch.setIsDel(true);
            }
//            currentMatchRepository.deleteByUser(user);
        } else {
            List<SlotTeamUser> slotTeamUsers = slotTeamUserRepository.findAllBySlotId(removeDto.getSlot().getId());
            slotTeamUsers.forEach(slotTeamUser -> {
                CurrentMatch currentMatch = currentMatchRepository.findByUserAndIsDel(slotTeamUser.getUser(), false).orElse(null);
                if (currentMatch != null) {
                    currentMatch.setIsDel(true);
                }
//                currentMatchRepository.deleteByUser(slotTeamUser.getUser());
            } );
        }
    }

    @Transactional
    public void createCurrentMatchByAdmin(CurrentMatchCreateRequestDto createRequestDto) {
        User user = userRepository.findById(createRequestDto.getUserId()).orElseThrow();
        Slot slot = slotRepository.findById(createRequestDto.getSlotId()).orElseThrow();
        Game game = createRequestDto.getGameId() == null ? null : gameRepository.findById(createRequestDto.getGameId()).orElseThrow();
        CurrentMatch currentMatch = CurrentMatch.builder()
                .user(user)
                .slot(slot)
                .game(game)
                .matchImminent(createRequestDto.getMatchImminent())
                .isMatched(createRequestDto.getIsMatched()).build();
        currentMatchRepository.save(currentMatch);
    }

    @Transactional
    public void updateCurrentMatchByAdmin(CurrentMatchUpdateRequestDto updateRequestDto) {
        CurrentMatch currentMatch = currentMatchRepository.findById(updateRequestDto.getCurrentMatchId()).orElseThrow();
        currentMatch.setMatchImminent(updateRequestDto.getMatchImminent());
        currentMatch.setIsMatched(updateRequestDto.getIsMatched());
        currentMatch.setIsDel(updateRequestDto.getIsDel());
    }

    /* delete by admin 있는게 맞나?*/
    @Transactional
    public void deleteCurrentMatchByAdmin(CurrentMatchDeleteDto deleteDto) {
        CurrentMatch currentMatch = currentMatchRepository.findById(deleteDto.getCurrentMatchId()).orElseThrow();
        currentMatchRepository.delete(currentMatch);
    }

    @Transactional
    public List<CurrentMatchDto> findCurrentMatchByAdmin(Pageable pageable) {
        Page<CurrentMatch> currentMatches = currentMatchRepository.findAllByOrderByIdDesc(pageable);
        List<CurrentMatchDto> currentMatchDtos = currentMatches.stream().map(CurrentMatchDto::from).collect(Collectors.toList());
        return currentMatchDtos;
    }
}
