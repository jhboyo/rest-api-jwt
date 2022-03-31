package com.valeos.restapidemo.members;

import com.valeos.restapidemo.common.ErrorsResource;
import com.valeos.restapidemo.events.EventController;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @description
 * @author joonhokim
 * @date 2022/03/31
 */
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final MemberRepository memberRepository;

    private final MemberValidator memberValidator;

    private final ModelMapper modelMapper;

    /**
     * @apiNote 단일 회원 상세 정보 조회
     * @param id
     * @return ..
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> queryMember(@PathVariable Integer id) {

        Optional<Member> optionalMember = this.memberRepository.findById(id);
        if (optionalMember.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Member member = optionalMember.get();
        MemberResource memberResource = new MemberResource(member);
        memberResource.add(Link.of("/docs/index.html#resources-members-get").withRel("profile"));

        return ResponseEntity.ok(memberResource);
    }


    @PostMapping
    public ResponseEntity createMember(@RequestBody @Valid  MemberDto memberDto, Errors errors) {

        // validation
        memberValidator.validate(memberDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Member member = modelMapper.map(memberDto, Member.class);
        Member newMember = this.memberRepository.save(member);

        Integer memberId = newMember.getId();

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(memberId);
        URI createdUri = selfLinkBuilder.toUri();

        MemberResource memberResource = new MemberResource(member);
        memberResource.add(linkTo(MemberController.class).withRel("query-events"));
        memberResource.add(selfLinkBuilder.withRel("update-event"));
        memberResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(memberResource);
    }


    private ResponseEntity<EntityModel<Errors>> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource().modelOf(errors));
    }

}
