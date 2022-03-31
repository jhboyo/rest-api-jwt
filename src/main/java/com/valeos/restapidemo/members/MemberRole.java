package com.valeos.restapidemo.members;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author joonhokim
 * @date 2022/03/31
 * @description :
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of= "id")
@Entity
public class MemberRole {

    @Id
    @GeneratedValue
    private Integer id;

    private String roleName;
}
