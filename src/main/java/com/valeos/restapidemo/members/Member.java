package com.valeos.restapidemo.members;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author joonhokim
 * @date 2022/03/31
 * @description 회원 속성
 */
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of= "id")
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String cellphone;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = true, length =6)
    private String gender;


}
