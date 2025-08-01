package com.example.repinfo.dto.gitHub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubRepositoryDto(
        String name,
        RepositoryOwner owner,
        Boolean fork
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RepositoryOwner(String login) {
    }
}
