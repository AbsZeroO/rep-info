package com.example.repinfo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BranchDto(
        String name,
        String lastCommitSha
) {
    @JsonCreator
    public BranchDto(
            @JsonProperty("name") String name,
            @JsonProperty("commit") BranchCommit commit
    ) {
        this(name, commit.sha());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record BranchCommit(String sha) {}
}
