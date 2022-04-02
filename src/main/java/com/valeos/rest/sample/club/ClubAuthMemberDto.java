package com.valeos.rest.sample.club;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * ClubAuthMemberDto 는 DTO 역할을 수행하는 동시에 스프링 시큐리티에서 인가 및 인증 작업에 사용한다.
 * password는 부모 클래스를 사용하므로 별도로 멤버 변수로 사용하지 않는다
 * User 객체는 UserDetails 인터페이스를 구현한 객체이므로 이를 가져다 사용한다.
 */
@Getter
@Setter
@ToString
public class ClubAuthMemberDto extends User {

    private String email;

    private String name;

    private boolean fromSocial;

    public ClubAuthMemberDto(String username,
                             String password,
                             boolean fromSocial,
                             String name,
                             Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);

        this.email = username;
        this.fromSocial = fromSocial;
        this.name = name;

    }
}
