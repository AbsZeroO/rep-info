package com.example.repinfo.controller;

import com.example.repinfo.service.GitHubUserReposService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GitHubUserReposController {

    private final GitHubUserReposService gitHubUserReposService;

    public GitHubUserReposController(GitHubUserReposService gitHubUserReposService) {
        this.gitHubUserReposService = gitHubUserReposService;
    }

    @GetMapping("/{username}/repositories")
    public ResponseEntity<?> getRepositories(@PathVariable String username) {
        return ResponseEntity.ok(gitHubUserReposService.getNonForkRepositories(username));
    }
}
