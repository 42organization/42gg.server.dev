package io.pp.arcade.v1.domain.feedback;

import io.pp.arcade.v1.domain.user.User;
import io.pp.arcade.v1.global.type.FeedbackType;
import io.pp.arcade.v1.global.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Feedback extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "intra_id")
    private User user;

    @NotNull
    @Column(name = "category")
    private FeedbackType category;

    @NotNull
    @Column(name = "content", length = 600)
    private String content;

    @Setter
    @NotNull
    @Column(name = "is_solved")
    private Boolean isSolved;

    @Builder
    public Feedback(User user, FeedbackType category, String content) {
        this.user = user;
        this.category = category;
        this.content = content;
        this.isSolved = false;
    }
}
