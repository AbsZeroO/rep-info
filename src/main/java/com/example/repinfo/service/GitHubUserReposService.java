package com.example.repinfo.service;

import com.example.repinfo.dto.response.BranchResponseDto;
import com.example.repinfo.dto.response.RepositoryResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class GitHubUserReposService {

    private RestTemplate restTemplate = new RestTemplate();

    private final String userReposUrl = "https://api.github.com/users/{username}/repos";
    private final String repoBranchesUrl = "https://api.github.com/repos/{owner}/{repo}/branches";

    public ArrayList<RepositoryResponseDto> getNonForkRepositories(String username) {


    }

    public BranchResponseDto getBranch(String branchName) {

        return null;
    }

}
