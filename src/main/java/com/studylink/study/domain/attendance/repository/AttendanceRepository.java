package com.studylink.study.domain.attendance.repository;

import com.studylink.study.domain.attendance.entity.Attendance;
import com.studylink.study.domain.schedule.entity.Schedule;
import com.studylink.study.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByScheduleAndUser(Schedule schedule, User user);
    List<Attendance> findBySchedule_ScheduleId(Long scheduleId);
}