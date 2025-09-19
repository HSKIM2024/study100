package com.studylink.study.domain.group.service;

import com.studylink.study.domain.group.entity.Group;
import com.studylink.study.domain.group.dto.RecommendedGroupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studylink.study.domain.user.entity.User;
import com.studylink.study.domain.group.repository.GroupRepository;
import com.studylink.study.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository repository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // 모든 스터디 그룹 조회
    public List<Group> findAll() {
        return repository.findAll();
    }

    // 특정 스터디 그룹 조회
    public Group findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디 그룹을 찾을 수 없습니다. ID: " + id));
    }

    // 스터디 그룹 생성 (leader 자동 지정)
    public Group createGroup(Group group, Long userId) {
        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + userId));

        group.setLeader(leader);
        return repository.save(group);
    }

    // 스터디 그룹 삭제
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 스터디 그룹이 존재하지 않습니다. ID: " + id);
        }
        repository.deleteById(id);
    }

    // 추천 그룹 조회 (Object[] → DTO 변환)
    public List<RecommendedGroupDto> findRecommendedGroups(
            Double userLat,
            Double userLon,
            List<String> interestTags,
            Double distanceKm
    ) {
        String tagsJson;
        try {
            tagsJson = new ObjectMapper().writeValueAsString(interestTags); // List -> JSON 문자열
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // JSON 변환 실패 시 빈 리스트 반환
        }

        List<Object[]> results = repository.findRecommendedGroups(userLat, userLon, tagsJson, distanceKm);

        return results.stream()
                .map(row -> new RecommendedGroupDto(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        ((Number) row[3]).doubleValue(),
                        ((Number) row[4]).doubleValue(),
                        ((Number) row[5]).doubleValue()
                ))
                .collect(Collectors.toList());
    }
}
