package com.example.repinfo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RepositoryDto(
        String name,
        String ownerLogin,
        Boolean fork,
        ArrayList<BranchDto> branches
) {
    @JsonCreator
    public RepositoryDto(
            @JsonProperty("name") String name,
            @JsonProperty("owner") RepositoryDto.RepositoryOwner owner,
            @JsonProperty("fork") Boolean fork
    ) {
        this(name, owner.login(), fork, new ArrayList<>());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RepositoryOwner(String login) {}
}
