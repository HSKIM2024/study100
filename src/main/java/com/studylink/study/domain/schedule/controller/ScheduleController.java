package com.studylink.study.domain.schedule.controller;

import com.studylink.study.domain.schedule.entity.Schedule;
import com.studylink.study.domain.schedule.dto.ScheduleRequest;
import com.studylink.study.domain.schedule.dto.ScheduleResponse;
import com.studylink.study.domain.schedule.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/study-schedules")
public class ScheduleController {

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    // 전체 조회
    @GetMapping
    public List<ScheduleResponse> getAll() {
        return service.findAll().stream()
                .map(ScheduleResponse::new)
                .collect(Collectors.toList());
    }

    // 단일 조회
    @GetMapping("/{id}")
    public ScheduleResponse getById(@PathVariable Long id) {
        Schedule schedule = service.findById(id);
        return new ScheduleResponse(schedule);
    }

    @PostMapping
    public ScheduleResponse create(@RequestBody ScheduleRequest request) {
        Schedule schedule = service.save(request);
        return new ScheduleResponse(schedule);
    }

    @PutMapping("/{id}")
    public ScheduleResponse update(@PathVariable Long id, @RequestBody ScheduleRequest request) {
        Schedule schedule = service.update(id, request);
        return new ScheduleResponse(schedule);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
