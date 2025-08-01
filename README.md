# RepInfo

## Project Description

RepInfo is a REST API built with Java 21 (Eclipse Temurin) and Spring Boot 3.5.4 that retrieves information about public
GitHub repositories for a given username.  
It filters out forked repositories and returns repository details including repository name, owner login, and for each 
branch, the branch name and the latest commit SHA.

The API also handles cases where the GitHub user does not exist, responding with a proper 404 error message.

---

## Technologies Used

- Java 21 (Eclipse Temurin)
- Spring Boot 3.5.4
- Gradle

---

## Prerequisites

- Java 21 installed
- Gradle (optional, if using the Gradle Wrapper)

---

## Installation and Running

Clone the repository:

```bash
git clone https://github.com/yourusername/repinfo.git
cd repinfo
```

Run the application using the Gradle Wrapper:

```bash
./gradlew bootRun
```

---

## Running Tests

Run integration tests with:

```bash
./gradlew test
```

---

## API Usage

1. List Repositories (non-fork) with branches and last commit SHA

* Request:
```http request
GET /api/github/{username}/repositories
```

Note: {username} is a placeholder and should be replaced with the actual GitHub username.

* Response

HTTP 200 and body with:

```json
[
  {
    "repositoryName": "example-repo",
    "ownerLogin": "{username}",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "a6c2h364g5fxg7h8i9j0"
      },
      {
        "name": "develop",
        "lastCommitSha": "z9y8x7w6vaswt3s2r1q0"
      }
    ]
  }
]
```

* Non-existing user

HTTP 404 and body with:

```json
{
  "status": 404,
  "message": "GitHub {username} not found"
}
```

Note: {username} in the message will be replaced with the requested username.