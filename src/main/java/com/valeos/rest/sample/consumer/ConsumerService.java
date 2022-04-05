package com.valeos.rest.sample.consumer;

import com.valeos.rest.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

/**
 * @author joonhokim
 * @date 2022/04/05
 * @description :
 */
@Service
public class ConsumerService {

    private final ConsumerRepository consumerRepository;

    private final PasswordEncoder passwordEncoder;

    public ConsumerService(ConsumerRepository consumerRepository, PasswordEncoder passwordEncoder) {
        this.consumerRepository = consumerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Consumer signup(UserDto userDto) {
        if (consumerRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 사용자입니다.");
        }
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build()
                ;

        Consumer consumer = Consumer.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build()
                ;
        return consumerRepository.save(consumer);
    }

    @Transactional(readOnly = true)
    public Optional<Consumer> getConsumerWithAuthorities(String username) {
        return consumerRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Consumer> getMyConsumerWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(consumerRepository::findOneWithAuthoritiesByUsername);
    }

}
