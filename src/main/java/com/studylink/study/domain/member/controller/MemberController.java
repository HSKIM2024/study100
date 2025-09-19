package com.studylink.study.domain.member.controller;

import com.studylink.study.domain.member.dto.MemberRequest;
import com.studylink.study.domain.member.dto.MemberResponse;
import com.studylink.study.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/group-members")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // 단건 조회 (그룹 + 유저)
    @GetMapping("/group/{groupId}/user/{userId}")
    public ResponseEntity<MemberResponse> getOne(@PathVariable Long groupId,
                                                      @PathVariable Long userId) {
        return service.findByGroupIdAndUserId(groupId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    } // GET /api/group-members/group/{groupId}/user/{userId} > api 형식

    // 그룹별 조회
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MemberResponse>> getByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(service.findByGroup(groupId));
    }

    // 멤버 추가
    @PostMapping
    public ResponseEntity<MemberResponse> addMember(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(service.addMember(request));
    }

    // 상태 업데이트
    @PutMapping("/{memberId}/status")
    public ResponseEntity<MemberResponse> updateStatus(@PathVariable Long memberId,
                                                            @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(memberId, status));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("멤버가 삭제되었습니다.");
    }
}
