package com.valeos.rest.sample.club;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    /**
     * @EntityGraph 기본적으로 left out join으로 연결
     */
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT cm from ClubMember cm WHERE cm.fromSocial = :social AND cm.email = :email")
    Optional<ClubMember> findByEmail(String email, boolean social);

}
