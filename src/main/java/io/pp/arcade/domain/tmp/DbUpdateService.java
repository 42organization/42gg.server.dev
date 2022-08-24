package io.pp.arcade.domain.tmp;

import io.pp.arcade.domain.game.Game;
import io.pp.arcade.domain.game.GameRepository;
import io.pp.arcade.domain.slot.Slot;
import io.pp.arcade.domain.slot.SlotRepository;
import io.pp.arcade.domain.slotteamuser.SlotTeamUser;
import io.pp.arcade.domain.slotteamuser.SlotTeamUserRepository;
import io.pp.arcade.domain.team.Team;
import io.pp.arcade.domain.team.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class DbUpdateService {
    private final SlotTeamUserRepository slotTeamUserRepository;
    private final SlotRepository slotRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;

    @Transactional
    public void saveFromSlot() {
//        slotTeamUserRepository.deleteAll();
        List<Slot> slots = slotRepository.findAll();
        for (Slot slot : slots) {
            if (slot.getTeam1() != null) {
                if (slot.getTeam1().getUser1() != null) {
                    SlotTeamUser slotTeamUser = SlotTeamUser.builder()
                            .slot(slot)
                            .team(slot.getTeam1())
                            .user(slot.getTeam1().getUser1())
                            .build();
                    slotTeamUserRepository.save(slotTeamUser);
                }
                if (slot.getTeam2().getUser1() != null) {
                    SlotTeamUser slotTeamUser = SlotTeamUser.builder()
                            .slot(slot)
                            .team(slot.getTeam2())
                            .user(slot.getTeam2().getUser1())
                            .build();
                    slotTeamUserRepository.save(slotTeamUser);
                }
            }
        }
    }

    @Transactional
    public void addSlotInTeam() {
        List<SlotTeamUser> slotTeamUsers = slotTeamUserRepository.findAll();
        for (SlotTeamUser slotTeamUser : slotTeamUsers) {
            Team team = slotTeamUser.getTeam();
            team.setSlot(slotTeamUser.getSlot());
        }
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            if (team.getSlot() == null) {
                team.setSlot(slotRepository.findById((team.getId() + 1) / 2).orElseThrow());
            }
        }
    }

    @Transactional
    public void deleteTeamInSlot() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            team.setUser1(null);
            team.setUser2(null);
        }
        List<Slot> slt = slotRepository.findAll();
        for (Slot slot : slt) {
            if (slot.getTeam1() != null) {;
                slot.getTeam1().setUser1(null);
                slot.setTeam1(null);
            }
            if (slot.getTeam2() != null) {
                slot.getTeam2().setUser1(null);
                slot.setTeam2(null);
            }
        }
    }

    @Transactional
    public void deleteTeamInGame() {
        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            game.setTeam1(null);
            game.setTeam2(null);
            game.setType(null);
            game.setTime(null);
        }
    }

}
