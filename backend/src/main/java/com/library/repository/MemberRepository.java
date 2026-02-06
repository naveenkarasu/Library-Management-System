package com.library.repository;

import com.library.model.Member;
import com.library.model.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNameContainingIgnoreCase(String name);
    List<Member> findByMemberType(MemberType memberType);

    @Query("SELECT m FROM Member m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(m.email) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Member> search(@Param("q") String query);
}
