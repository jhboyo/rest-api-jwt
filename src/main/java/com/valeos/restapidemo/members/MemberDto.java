package com.valeos.restapidemo.members;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author joonhokim
 * @date 2022/03/31
 * @description :
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberDto {

    @NotNull @NotEmpty
    @Size(min = 3, max = 20)
    private String name;

    @NotNull @NotEmpty
    @Size(min = 1, max = 30)
    private String nickname;

    @NotNull @NotEmpty
    @Size(min = 10, max = 255)
    private String password;

    @NotNull @NotEmpty
    @Size(min = 9, max = 20)
    private String cellphone;

    @NotNull @NotEmpty
    @Size(min = 6, max = 100)
    private String email;

    private String gender;
}
