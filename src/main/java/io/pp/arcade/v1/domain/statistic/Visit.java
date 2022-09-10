package io.pp.arcade.v1.domain.statistic;

import io.pp.arcade.v1.global.util.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Visit extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "intra_id")
    private String intraId;

    @Column(name = "ranking")
    private Integer ranking;
}
