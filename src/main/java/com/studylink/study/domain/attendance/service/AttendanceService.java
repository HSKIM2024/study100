package com.studylink.study.domain.attendance.service;

import com.studylink.study.domain.attendance.entity.Attendance;
import com.studylink.study.domain.attendance.dto.AttendanceRequest;
import com.studylink.study.domain.attendance.dto.AttendanceResponse;
import com.studylink.study.domain.schedule.entity.Schedule;
import com.studylink.study.domain.user.entity.User;
import com.studylink.study.domain.attendance.repository.AttendanceRepository;
import com.studylink.study.domain.schedule.repository.ScheduleRepository;
import com.studylink.study.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final AttendanceRepository repository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public AttendanceService(AttendanceRepository repository,
                             ScheduleRepository scheduleRepository,
                             UserRepository userRepository) {
        this.repository = repository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    // 전체 조회
    public List<AttendanceResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(AttendanceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 단건 조회
    public Optional<AttendanceResponse> findById(Long id) {
        return repository.findById(id)
                .map(AttendanceResponse::fromEntity);
    }

    // 출석 체크(기존 있으면 상태 변경 + 시간 갱신)
    @Transactional
    public AttendanceResponse checkIn(AttendanceRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 스케줄 ID입니다."));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 유저 ID입니다."));

        Optional<Attendance> existing = repository.findByScheduleAndUser(schedule, user);

        Attendance attendance;
        if (existing.isPresent()) {
            attendance = existing.get();
            attendance.setStatus(Attendance.Status.valueOf(request.getStatus()));
            attendance.setCheckedAt(LocalDateTime.now());
        } else {
            attendance = new Attendance();
            attendance.setSchedule(schedule);
            attendance.setUser(user);
            attendance.setStatus(Attendance.Status.valueOf(request.getStatus()));
            attendance.setCheckedAt(LocalDateTime.now());
        }

        Attendance saved = repository.save(attendance);
        return AttendanceResponse.fromEntity(saved);
    }

    // 스케줄별 조회
    public List<AttendanceResponse> findBySchedule(Long scheduleId) {
        return repository.findBySchedule_ScheduleId(scheduleId)
                .stream()
                .map(AttendanceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 출석 상태 변경
    @Transactional
    public AttendanceResponse updateStatus(Long id, String status) {
        Attendance attendance = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("출석 기록을 찾을 수 없습니다."));
        attendance.setStatus(Attendance.Status.valueOf(status));
        attendance.setCheckedAt(LocalDateTime.now());
        return AttendanceResponse.fromEntity(repository.save(attendance));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
