package com.studylink.study.domain.schedule.service;

import com.studylink.study.domain.schedule.entity.Schedule;
import com.studylink.study.domain.schedule.dto.ScheduleRequest;
import com.studylink.study.domain.schedule.repository.ScheduleRepository;
import com.studylink.study.domain.group.repository.GroupRepository;
import com.studylink.study.domain.group.entity.Group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;

    public ScheduleService(ScheduleRepository scheduleRepository,
                                GroupRepository groupRepository) {
        this.scheduleRepository = scheduleRepository;
        this.groupRepository = groupRepository;
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디 스케줄을 찾을 수 없습니다. ID: " + id));
    }

    @Transactional
    public Schedule save(ScheduleRequest request) {
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("스터디 그룹을 찾을 수 없습니다. ID: " + request.getGroupId()));

        Schedule schedule = new Schedule();
        schedule.setGroup(group);
        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setLocation(request.getLocation());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        schedule.setStartTime(LocalDateTime.parse(request.getStartTime(), formatter));
        schedule.setEndTime(LocalDateTime.parse(request.getEndTime(), formatter));

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule update(Long id, ScheduleRequest request) {
        Schedule schedule = findById(id);

        if (request.getGroupId() != null) {
            Group group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("스터디 그룹을 찾을 수 없습니다. ID: " + request.getGroupId()));
            schedule.setGroup(group);
        }

        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setLocation(request.getLocation());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        schedule.setStartTime(LocalDateTime.parse(request.getStartTime(), formatter));
        schedule.setEndTime(LocalDateTime.parse(request.getEndTime(), formatter));

        return scheduleRepository.save(schedule);
    }

    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }
}
