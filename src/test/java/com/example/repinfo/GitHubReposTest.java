package com.example.repinfo;

import com.example.repinfo.dto.response.BranchResponseDto;
import com.example.repinfo.dto.response.RepositoryResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubReposTest {

    @LocalServerPort
    private int port;

    @Test
    public void testGitHubRepos() {
        RestTemplate restTemplate = new RestTemplate();

        // given
        String existingUser = "octocat";
        String urlExisting = "http://localhost:" + port + "/api/{username}/repositories";

        // when - existing user
        ResponseEntity<List<RepositoryResponseDto>> repositoryResponse = restTemplate.exchange(
                urlExisting,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                existingUser
        );

        // then - existing user
        assertEquals(HttpStatus.OK, repositoryResponse.getStatusCode());

        List<RepositoryResponseDto> repositories = repositoryResponse.getBody();
        assertNotNull(repositories);
        assertFalse(repositories.isEmpty());

        for (RepositoryResponseDto repo : repositories) {
            assertNotNull(repo.repositoryName());
            assertFalse(repo.repositoryName().isBlank());
            assertEquals(existingUser, repo.ownerLogin());

            List<BranchResponseDto> branches = repo.branches();
            assertNotNull(branches);

            // Can have 0 or more branches
            for (BranchResponseDto branch : branches) {
                assertNotNull(branch.name());
                assertFalse(branch.name().isBlank());

                assertNotNull(branch.lastCommitSha());
                assertFalse(branch.lastCommitSha().isBlank());
            }
        }
    }
}

