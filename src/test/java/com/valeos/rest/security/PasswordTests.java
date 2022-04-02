package com.valeos.rest.security;

import com.valeos.rest.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;


public class PasswordTests extends BaseTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testEncode() {
        // Given
        String password = "1234";

        // When
        String enPassword = passwordEncoder.encode(password);
        System.out.println(enPassword);

        // Then
        boolean matchResult = passwordEncoder.matches(password, enPassword);
        assertThat(matchResult).isTrue();
    }

}
