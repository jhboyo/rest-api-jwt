package com.valeos.rest.members;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    @Query("SELECT m FROM Member m WHERE m.name=?1 AND m.email= ?2")
    Page<Member> findMembersByNameAndEmail(String name, String email, Pageable  pageable);

    @Query("SELECT m FROM Member m WHERE m.email=?1")
    Member findByEmailAndPassword(String email, String password);
}
