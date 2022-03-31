package com.valeos.restapidemo.members;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class MemberResource extends EntityModel<Object> {
    public MemberResource(Member member, Link... links) {
        super(member, Arrays.asList(links));
        add(linkTo(MemberController.class).slash(member.getId()).withSelfRel());
    }
}
