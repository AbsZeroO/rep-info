package com.example.repinfo.service;

import com.example.repinfo.dto.response.BranchResponseDto;
import com.example.repinfo.dto.response.RepositoryResponseDto;
import com.example.repinfo.exception.UserNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import com.example.repinfo.dto.gitHub.GitHubBranchDto;
import com.example.repinfo.dto.gitHub.GitHubRepositoryDto;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

@Service
public class GitHubUserReposService {

    private final RestTemplate restTemplate = new RestTemplate();

    String userReposUrl = "https://api.github.com/users/{username}/repos";
    String repoBranchesUrl = "https://api.github.com/repos/{owner}/{repo}/branches";

    public List<RepositoryResponseDto> getNonForkRepositories(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<GitHubRepositoryDto>> repositoryResponse = restTemplate.exchange(
                userReposUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {
                },
                username
        );

        if (!repositoryResponse.getStatusCode().is2xxSuccessful()) {
            throw new UserNotFoundException(username);
        }


        if (repositoryResponse.getBody() != null && repositoryResponse.getBody().isEmpty()) {
            return Collections.emptyList();
        }

        List<RepositoryResponseDto> responseList = new ArrayList<>();

        for (GitHubRepositoryDto repositoryDto : repositoryResponse.getBody()) {
            if (Boolean.TRUE.equals(repositoryDto.fork())) {
                continue;
            }

            List<BranchResponseDto> branchResponses = new ArrayList<>();

            try {
                ResponseEntity<List<GitHubBranchDto>> branchResponse = restTemplate.exchange(
                        repoBranchesUrl,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<>() {},
                        repositoryDto.owner().login(),
                        repositoryDto.name()
                );

                List<GitHubBranchDto> branches = branchResponse.getBody();
                if (branches != null) {
                    for (GitHubBranchDto branch : branches) {
                        branchResponses.add(new BranchResponseDto(branch.name(), branch.commit().sha()));
                    }
                }

            } catch (HttpClientErrorException e) {
                // Repository might be private or not accessible – skip it
                continue;
            }

            responseList.add(new RepositoryResponseDto(repositoryDto.name(), repositoryDto.owner().login(), branchResponses));
        }

        return responseList;
    }

}
