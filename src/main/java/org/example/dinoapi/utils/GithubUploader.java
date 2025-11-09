package org.example.dinoapi.utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class GithubUploader {

    private static final String REPO_OWNER = "VIZ3NT04";
    private static final String REPO_NAME = "DINO-API-IMAGES";
    private static final String BRANCH = "main";
    private static final String API_URL =
            "https://api.github.com/repos/" + REPO_OWNER + "/" + REPO_NAME + "/contents/";

    @Value("${github.token}")
    private String githubToken;

    public String subir(MultipartFile file, String fileName) throws IOException {
        String base64 = Base64.getEncoder().encodeToString(file.getBytes());

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Subiendo imagen " + fileName);
        body.put("content", base64);
        body.put("branch", BRANCH);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("Accept", "application/vnd.github+json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL + "dinosaurios/" + fileName,
                HttpMethod.PUT,
                request,
                Map.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Map content = (Map) response.getBody().get("content");
            return (String) content.get("path");
        } else {
            throw new RuntimeException("Error subiendo imagen: " + response.getStatusCode());
        }
    }
}
