package com.leoric.controllers;

import com.leoric.models.Issue;
import com.leoric.services.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.leoric.config.JwtConstant.JWT_HEADER;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssuesController {
    private final IssueService issueService;

    @GetMapping
    public ResponseEntity<List<Issue>> getAllIssues(
            @RequestHeader(JWT_HEADER) String jwt
    ) throws Exception {
        return ResponseEntity.ok(issueService.getAllIssues());
    }
}
