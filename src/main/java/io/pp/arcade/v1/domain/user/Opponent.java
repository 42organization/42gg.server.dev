//package io.pp.arcade.v1.domain.user;
//
//import io.pp.arcade.v1.global.util.BaseTimeEntity;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//
//@Entity
//@Getter
//@NoArgsConstructor
//public class Opponent extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
////    @ManyToOne
//    @OneToOne
//    @JoinColumn(name = "user")
//    private User user;
//
//    @Column(name = "intra_id")
//    private String intraId;
//
//    @Column(name = "image_uri")
//    private String imageUri;
//
//    @Column(name = "nick")
//    private String nick;
//
//    @Column(name = "detail")
//    private String detail;
//
//    @NotNull
//    @Column(name = "is_ready")
//    private boolean isReady;
//}
