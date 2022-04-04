package com.valeos.rest.sample.consumer;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @author joonhokim
 * @date 2022/04/04
 * @description :
 */
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    /**
     * EntityGraph 는 쿼리가 수행이 될 때 Eager 조회 authorities 정보를 함께 가져온다.
     */
    @EntityGraph(attributePaths = "authorities")
    Optional<Consumer> findOneWithAuthoritiesByUsername(String username);
}
