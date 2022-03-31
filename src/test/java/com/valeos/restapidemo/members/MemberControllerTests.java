package com.valeos.restapidemo.members;

import com.valeos.restapidemo.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author joonhokim
 * @date 2022/03/31
 * @description :
 */
public class MemberControllerTests extends BaseTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("정상적으로 사용자를 생성하는 테스트")
    public void createMember() throws Exception {

        // Given
        MemberRole memberRole = new MemberRole();
        memberRole.setRoleName("user");

        // When
        MemberDto member = MemberDto.builder()
                .name("joonho")
                .nickname("derek")
                .password("password")
                .email("derek@email.com")
                .cellphone("01099857514")
                .roles(Arrays.asList(memberRole))
                .build()
                ;
        // Then
        ResultActions resultActions = mockMvc.perform(post("/api/member")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaTypes.HAL_JSON)
                                        .content(objectMapper.writeValueAsString(member)))
                                        .andDo(print())
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("id").exists())
                ;
    }

}