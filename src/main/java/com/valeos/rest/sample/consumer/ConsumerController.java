package com.valeos.rest.sample.consumer;

import com.valeos.rest.common.jwt.JwtFilter;
import com.valeos.rest.common.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author joonhokim
 * @date 2022/04/04
 * @description :
 */

@RestController
@RequestMapping("/api")
public class ConsumerController {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final ConsumerService consumerService;

    public ConsumerController(TokenProvider tokenProvider,
                              AuthenticationManagerBuilder authenticationManagerBuilder,
                              ConsumerService consumerService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.consumerService = consumerService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }



    @PostMapping("/signup")
    public ResponseEntity<Consumer> signup(@Valid @RequestBody UserDto userDto) {
        logger.info("sss");
        return ResponseEntity.ok(consumerService.signup(userDto));
    }

    @GetMapping("/consumer")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // USER 와 ADMIN Role 모두 호출 가능
    public ResponseEntity<Consumer> getMyConsumerInfo() {
        return ResponseEntity.ok(consumerService.getMyConsumerWithAuthorities().get());
    }


    @GetMapping("/consumer/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')") // USER 와 ADMIN Role 모두 호출 가능
    public ResponseEntity<Consumer> getConsumerInfo(@PathVariable String username) {
        return ResponseEntity.ok(consumerService.getConsumerWithAuthorities(username).get());
    }
}