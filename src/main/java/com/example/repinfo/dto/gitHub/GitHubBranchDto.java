package com.example.repinfo.dto.gitHub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubBranchDto(
        String name,
        BranchCommit commit
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BranchCommit(String sha) {
    }
}
