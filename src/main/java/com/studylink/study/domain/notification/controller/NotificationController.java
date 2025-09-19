package com.studylink.study.domain.notification.controller;

import com.studylink.study.domain.notification.dto.NotificationRequest;
import com.studylink.study.domain.notification.dto.NotificationResponse;
import com.studylink.study.domain.notification.service.NotificationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // GET 요청: NotificationResponse DTO 반환
    @GetMapping
    public List<NotificationResponse> getAll() {
        return service.findAllResponses();  // DTO로 변환 후 반환
    }

    // POST 요청: NotificationRequest DTO 사용
    @PostMapping
    public NotificationResponse create(@RequestBody NotificationRequest request) {
        return service.save(request);  // save 메서드에서 NotificationResponse 반환하도록 수정 필요
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}