package com.valeos.restapidemo.members;

import com.valeos.restapidemo.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author joonhokim
 * @date 2022/03/31
 * @description :
 */
public class MemberControllerTests extends BaseTest {

    @Autowired
    MemberRepository memberRepository;


    /**
     * Valid Annotation 과  Validator 를 통하여 잘 못 된 입력 값이 들어 올 수 없어서 정상 적인 테스트만 구현
     */
    @Test
    @DisplayName("정상적으로 사용자를 생성하는 테스트")
    public void createMember() throws Exception {

        // Given
        String username = "joonhokim";
        String nickname = "derek";
        String password = "passworD1234$";
        String email = "joomho1@email.com";
        String cellphone = "01099857514";
        String gender = "male";

        // When
        MemberDto member = MemberDto.builder()
                .name(username)
                .nickname(nickname)
                .password(password)
                .email(email)
                .cellphone(cellphone)
                .gender(gender)
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
                        .andExpect(jsonPath("name").exists())
                        .andExpect(jsonPath("password").exists())
                        .andExpect(jsonPath("email").exists())
                        .andExpect(jsonPath("cellphone").exists())
                        .andExpect(header().exists(HttpHeaders.LOCATION))
                        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                        .andDo(document("create-member",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-members").description("link to query-members"),
                                linkWithRel("update-member").description("link to update a existing member"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of a new member"),
                                fieldWithPath("password").description("Password of a new member"),
                                fieldWithPath("nickname").description("Nickname of a new member"),
                                fieldWithPath("email").description("Email of a new member"),
                                fieldWithPath("cellphone").description("Cellphone of a new member"),
                                fieldWithPath("gender").description("Gender of a new member")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier"),
                                fieldWithPath("name").description("Name of a new member"),
                                fieldWithPath("password").description("Password of a new member"),
                                fieldWithPath("nickname").description("Nickname of a new member"),
                                fieldWithPath("email").description("Email of a new member"),
                                fieldWithPath("cellphone").description("Cellphone of a new member"),
                                fieldWithPath("gender").description("Gender of a new member"),
                                fieldWithPath("_links.self.href").description("link to self href"),
                                fieldWithPath("_links.query-members.href").description("link to query member list"),
                                fieldWithPath("_links.update-member.href").description("link to update existing member"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

}