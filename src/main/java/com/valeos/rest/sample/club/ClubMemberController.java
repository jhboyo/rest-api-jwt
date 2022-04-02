package com.valeos.rest.sample.club;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author joonhokim
 * @date 2022/04/01
 * @description :
 */

@Controller
@Log4j2
@RequestMapping("/sample")
public class ClubMemberController {

    @GetMapping("/all")
    public ResponseEntity exAll() {
        log.info("exAll...");

        return ResponseEntity.ok("All");
    }

    @GetMapping("/member")
    public ResponseEntity exMember(@AuthenticationPrincipal ClubAuthMemberDto clubAuthMemberDto) {
        log.info("exMember...");
        log.info("-----------");
        log.info(clubAuthMemberDto);
        return ResponseEntity.ok("Member");
    }

    @GetMapping("/admin")
    public ResponseEntity exAdmin() {
        log.info("exAdmin...");
        return ResponseEntity.ok("Admin");
    }
}
