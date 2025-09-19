package com.studylink.study.domain.schedule.repository;

import com.studylink.study.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {}
