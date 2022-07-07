package com.king.assessment.integration;

import com.king.assessment.KingAssessmentApplication;
import com.king.assessment.model.SaveScoreRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(classes = KingAssessmentApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void login_validUserId_returnsSessionToken() {

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/123/login", String.class),
                   is(notNullValue()));
    }

    @Test
    public void getHighScoreList_validLevelId_returnsListOfScoresByUserId() {

        String highScoreList =
            this.restTemplate.getForObject("http://localhost:" + port + "/12345/highscorelist", String.class);
        assertThat(highScoreList, is("123=,1234=123456"));
    }

    @Test
    public void saveScore_unAuthorizedUser_savedSuccessfully() {

        SaveScoreRequest saveScoreRequest = new SaveScoreRequest();
        saveScoreRequest.setScore(100);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/123/score?sessionKey=8ef20062-714d-4f4e-9384-e0d443abcf23",
                                                   saveScoreRequest,
                                                   ResponseEntity.class), nullValue());
    }
}
