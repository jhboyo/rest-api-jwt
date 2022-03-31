package com.valeos.restapidemo.members;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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
    private String name;

    @NotNull @NotEmpty
    private String nickname;

    @NotNull @NotEmpty
    @Size(min = 10)
    private String password;

    @NotNull @NotEmpty
    private String cellphone;

    @NotNull @NotEmpty
    private String email;

    private List<MemberRole> roles;
}
