package io.pp.arcade.domain.rank;

import io.pp.arcade.domain.rank.dto.FindRankDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RankService {
    private final RankRepository rankRepository;

    public List<FindRankDto> findRankList(Pageable pageable) {
        Page<Rank> pageListDto = rankRepository.findRankList(pageable);
        return pageListDto.stream().map(FindRankDto::from).collect(Collectors.toList());
    }
}
