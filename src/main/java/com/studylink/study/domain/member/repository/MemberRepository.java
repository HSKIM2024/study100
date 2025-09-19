package com.studylink.study.domain.member.repository;

import com.studylink.study.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 특정 그룹 + 특정 유저 단건 조회 (중복 확인용)
    Optional<Member> findByGroup_GroupIdAndUser_UserId(Long groupId, Long userId);

    // 특정 그룹의 모든 멤버 조회
    List<Member> findByGroup_GroupId(Long groupId);
}
