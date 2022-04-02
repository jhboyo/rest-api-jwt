package com.valeos.rest.sample.club;

import com.valeos.rest.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author joonhokim
 * @date 2022/04/02
 * @description :
 */
class ClubMemberTests extends BaseTest {

    @Autowired
    ClubMemberRepository clubMemberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user"+i+"@valeos.com")
                    .name("user"+i)
                    .password(passwordEncoder.encode("1111"))
                    .build();
            clubMember.addMemberRole(ClubMemberRole.USER);
            if (i> 80) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i>90) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            clubMemberRepository.save(clubMember);
        });

        // Given
        String email = "user1@valeos.com";
        // When
        Optional<ClubMember> clubMember = clubMemberRepository.findById(email);
        ClubMember clubMember1 = clubMember.get();
        // Then
        assertThat(clubMember1.getName()).isEqualTo("user1");
    }


    @Test
    public void testQuery() {

        Optional<ClubMember> result = clubMemberRepository.findByEmail("user95@valeos.com", false);
        ClubMember clubMember = result.get();

        System.out.println(clubMember);
    }

}