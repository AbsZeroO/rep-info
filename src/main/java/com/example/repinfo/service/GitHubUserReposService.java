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
        List<GitHubRepositoryDto> repositories = fetchUserRepositories(username);

        if (repositories.isEmpty()) {
            return Collections.emptyList();
        }

        List<RepositoryResponseDto> responseList = new ArrayList<>();
        for (GitHubRepositoryDto repositoryDto : repositories) {
            if (Boolean.TRUE.equals(repositoryDto.fork())) {
                continue;
            }

            List<BranchResponseDto> branches = fetchBranches(repositoryDto.owner().login(), repositoryDto.name());

            responseList.add(new RepositoryResponseDto(repositoryDto.name(), repositoryDto.owner().login(), branches));
        }
        return responseList;
    }

    private List<GitHubRepositoryDto> fetchUserRepositories(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<GitHubRepositoryDto>> repositoryResponse = restTemplate.exchange(
                    userReposUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    },
                    username
            );

            if (repositoryResponse.getBody() == null) {
                return Collections.emptyList();
            }

            return repositoryResponse.getBody();

        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException(username);
        }
    }

    private List<BranchResponseDto> fetchBranches(String owner, String repoName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<GitHubBranchDto>> branchResponse = restTemplate.exchange(
                    repoBranchesUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    },
                    owner,
                    repoName
            );

            List<GitHubBranchDto> branches = branchResponse.getBody();
            List<BranchResponseDto> branchResponses = new ArrayList<>();
            if (branches != null) {
                for (GitHubBranchDto branch : branches) {
                    branchResponses.add(new BranchResponseDto(branch.name(), branch.commit().sha()));
                }
            }
            return branchResponses;

        } catch (HttpClientErrorException e) {
            // Repository might be private or inaccessible, skip branches
            return Collections.emptyList();
        }
    }

}
