package com.valeos.restapidemo.members;

import com.valeos.restapidemo.common.ErrorsResource;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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



@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api/member", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final MemberRepository memberRepository;

    private final MemberValidator memberValidator;

    private final ModelMapper modelMapper;

    /**
     * @apiNote 단일 회원 상세 정보 조회
     * @param id 회원 고유 번호
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

        WebMvcLinkBuilder selfLinkBuilder = linkTo(MemberController.class).slash(id);

        memberResource.add(linkTo(MemberController.class).withRel("query-members"));
        memberResource.add(selfLinkBuilder.withRel("update-member"));
        memberResource.add(Link.of("/docs/index.html#resources-members-get").withRel("profile"));

        return ResponseEntity.ok(memberResource);
    }


    /**
     * @apiNote 여러 회원 목록 정보 조회
     * @param
     * @return ..
     */
    @GetMapping()
    public ResponseEntity queryMembers(@RequestParam String name,
                                       @RequestParam String email,
                                       Pageable pageable,
                                       PagedResourcesAssembler<Member> assembler) {

        //assembler를 통해서 repository로부터 받아온 데이터를 resource로 변환이 가능하다.

        Page<Member> page = null;
        if (name.isBlank() && email.isBlank()) {
            page = this.memberRepository.findAll(pageable);
        } else {
            page = this.memberRepository.findMembersByNameAndEmail(name, email, pageable);
        }

//        Page<Member> page = this.memberRepository.findMembersByNameAndEmail(name, email, pageable);

        var pagedResources = assembler.toModel(page, e -> new MemberResource(e));
        pagedResources.add(Link.of("/docs/index.html#resources-members-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }



    @PostMapping()
    public ResponseEntity createMember(@RequestBody @Valid MemberDto memberDto, Errors errors) {

        // validation
        memberValidator.validate(memberDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Member member = modelMapper.map(memberDto, Member.class);
        Member newMember = this.memberRepository.save(member);

        Integer memberId = newMember.getId();

        WebMvcLinkBuilder selfLinkBuilder = linkTo(MemberController.class).slash(memberId);
        URI createdUri = selfLinkBuilder.toUri();

        MemberResource memberResource = new MemberResource(member);
        memberResource.add(linkTo(MemberController.class).withRel("query-members"));
        memberResource.add(selfLinkBuilder.withRel("update-member"));
        memberResource.add(Link.of("/docs/index.html#resources-members-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(memberResource);
    }



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequestDto memberLoginRequestDto, Errors errors) {

        if (memberLoginRequestDto.getEmail() == null || memberLoginRequestDto.getPassword() == null) {
            return badRequest(errors);
        }
        if (memberLoginRequestDto.getEmail().isBlank() || memberLoginRequestDto.getPassword().isBlank()) {
            return badRequest(errors);
        }

        Member member = memberRepository.findByEmailAndPassword(memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword());
        if (member == null || member.getEmail().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        WebMvcLinkBuilder selfLinkBuilder = linkTo(MemberController.class).slash(member.getId());
        URI createdUri = selfLinkBuilder.toUri();

        MemberResource memberResource = new MemberResource(member);
        memberResource.add(linkTo(MemberController.class).withRel("logout-member"));
        memberResource.add(Link.of("/docs/index.html#resources-members-login").withRel("profile"));

        return ResponseEntity.ok(memberResource);
    }






    private ResponseEntity<EntityModel<Errors>> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource().modelOf(errors));
    }

}
