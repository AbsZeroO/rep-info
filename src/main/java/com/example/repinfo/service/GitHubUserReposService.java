package com.example.repinfo.service;

import com.example.repinfo.dto.RepositoryDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class GitHubUserReposService {

    private RestTemplate restTemplate = new RestTemplate();



    public ArrayList<RepositoryDto> getNonForkRepositories(String username) {

    }

}
