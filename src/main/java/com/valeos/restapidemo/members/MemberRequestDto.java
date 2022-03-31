package com.valeos.restapidemo.members;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author joonhokim
 * @date 2022/03/31
 * @description :
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {


    @NotNull @NotEmpty
    @Size(min = 10, max = 255)
    private String password;

    @NotNull @NotEmpty
    @Size(min = 6, max = 100)
    private String email;


}
