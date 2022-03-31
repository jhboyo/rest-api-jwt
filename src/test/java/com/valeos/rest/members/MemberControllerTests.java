package com.valeos.rest.members;

import com.valeos.rest.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        String email = "joomho111@email.com";
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




    @Test
    @DisplayName("10명의 사용자를 5명씩 두번째 페이지 조회하기")
    public void queryMembers() throws Exception {
        //Given
        IntStream.range(0, 10).forEach(this::generateMembers);

        // When & Then
        this.mockMvc.perform(get("/api/member")
                        .param("name", "")
                        .param("email", "")
                        .param("page", "1")
                        .param("size", "5")
                        .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
//                .andExpect(jsonPath("_embedded.memberList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-members"))
        ;
    }


    @Test
    @DisplayName("사용자를 이름과 이메일로 조회 후 2명씩 두번째 페이지 조회하기")
    public void queryMembersByNameAndEmail() throws Exception {
        //Given
        IntStream.range(0, 10).forEach(this::generateMembers);

        // When & Then
        this.mockMvc.perform(get("/api/member")
                        .param("name", "member1")
                        .param("email", "member@email.com1")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
//                .andExpect(jsonPath("_embedded.memberList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-members"))
        ;
    }



    private Member generateMembers(int index) {
        MemberDto memberDto = buildMembers(index);

        Member member = modelMapper.map(memberDto, Member.class);

        return this.memberRepository.save(member);
    }



    private MemberDto buildMembers(int index) {
        return MemberDto.builder()
                .name("member" + index)
                .nickname("nickname" + index)
                .password("passWord$12345" + Integer.toString(index))
                .cellphone("0101122" + Integer.toString(index))
                .email("member@email.com" + Integer.toString(index))
                .gender("male")
                .build();
    }


    @Test
    @DisplayName("사용자 로그인 테스트")
    public void loginMember() throws Exception {

        // Given
        String username = "joonhokim";
        String nickname = "derek";
        String password = "passworD1234$";
        String email = "joomho1@email.com";
        String cellphone = "01099857514";
        String gender = "male";

        // When
        MemberDto memberDto = MemberDto.builder()
                .name(username)
                .nickname(nickname)
                .password(password)
                .email(email)
                .cellphone(cellphone)
                .gender(gender)
                .build()
                ;

        Member member = modelMapper.map(memberDto, Member.class);
        memberRepository.save(member);

        System.out.println("****** Email: " + member.getEmail());

        String loginEmail = "joomho1@email.com";
        String loginPassword = "passworD1234$";
        MemberRequestDto memberRequestDto = new MemberRequestDto();
        memberRequestDto.setEmail(loginEmail);
        memberRequestDto.setPassword(loginPassword);

        // Then
        ResultActions resultActions = mockMvc.perform(post("/api/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("cellphone").exists())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))

                ;
    }

}