package io.pp.arcade.domain.statistic;

import io.pp.arcade.global.util.BaseTimeEntity;
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

    @Column(name = "rank")
    private Integer rank;
}