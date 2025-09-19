package com.studylink.study.domain.member.service;

import com.studylink.study.domain.member.entity.Member;
import com.studylink.study.domain.member.dto.MemberRequest;
import com.studylink.study.domain.member.dto.MemberResponse;
import com.studylink.study.domain.group.entity.Group;
import com.studylink.study.domain.user.entity.User;
import com.studylink.study.domain.member.repository.MemberRepository;
import com.studylink.study.domain.group.repository.GroupRepository;
import com.studylink.study.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository repository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public MemberService(MemberRepository repository,
                              GroupRepository groupRepository,
                              UserRepository userRepository) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    // 전체 조회
    public List<MemberResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(MemberResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 단건 조회 (그룹+유저)
    public Optional<MemberResponse> findByGroupIdAndUserId(Long groupId, Long userId) {
        return repository.findByGroup_GroupIdAndUser_UserId(groupId, userId)
                .map(MemberResponse::fromEntity);
    }

    // 그룹별 조회
    public List<MemberResponse> findByGroup(Long groupId) {
        return repository.findByGroup_GroupId(groupId)
                .stream()
                .map(MemberResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 멤버 추가
    @Transactional
    public MemberResponse addMember(MemberRequest request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // 중복 체크
        repository.findByGroup_GroupIdAndUser_UserId(request.getGroupId(), request.getUserId())
                .ifPresent(existing -> {
                    throw new RuntimeException("이미 이 그룹에 가입된 유저입니다");
                });

        Member member = new Member();
        member.setGroup(group);
        member.setUser(user);

        if (request.getRole() != null) member.setRole(Member.Role.valueOf(request.getRole()));
        if (request.getStatus() != null) member.setStatus(Member.Status.valueOf(request.getStatus()));

        return MemberResponse.fromEntity(repository.save(member));
    }

    // 상태 업데이트
    @Transactional
    public MemberResponse updateStatus(Long memberId, String status) {
        Member member = repository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없습니다"));
        member.setStatus(Member.Status.valueOf(status));
        return MemberResponse.fromEntity(repository.save(member));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
