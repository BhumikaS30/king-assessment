package com.king.assessment.controller;

import com.king.assessment.service.GameService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {GameController.class})
@AutoConfigureMockMvc
@EnableWebMvc
@WebAppConfiguration
public class GameServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private GameService gameService;

    private static final String DEFAULT_SESSION_TOKEN_KEY = "8ef20062-714d-4f4e-9384-e0d443abcf23";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void login_validUserId_returnsSessionToken() throws Exception {

        when(gameService.login(anyInt())).thenReturn(DEFAULT_SESSION_TOKEN_KEY);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                                                  .get("/125/login")
                                                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                  .accept(MediaType.APPLICATION_JSON_VALUE))
                                     .andExpect(status().isOk())
                                     .andReturn();

        assertThat(result.getResponse().getContentAsString(), is(DEFAULT_SESSION_TOKEN_KEY));
    }

    @Test
    public void getHighScoreList_validLevelId_returnsListOfScoresByUserId() throws Exception {

        when(gameService.getHighScoreList(anyInt())).thenReturn("123=1221");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                                               .get("/123/highscorelist")
                                               .contentType(MediaType.APPLICATION_JSON_VALUE)
                                               .accept(MediaType.APPLICATION_JSON_VALUE))
                                  .andExpect(status().isOk())
                                  .andReturn();

        assertThat(result.getResponse().getContentAsString(), is("123=1221"));
    }

    @Test
    public void saveScore_authorizedUserAndValidRequest_savedSuccessfully() throws Exception {

        when(gameService.saveScore(anyString(), anyInt(), anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                            .post("/123")
                            .queryParam("score", "987")
                            .queryParam("sessionKey", DEFAULT_SESSION_TOKEN_KEY)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andReturn();
    }

    @Test
    public void saveScore_authorizedUserAndInValidRequest_savedSuccessfully() throws Exception {

        when(gameService.saveScore(anyString(), anyInt(), anyInt())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                            .post("/"+null)
                            .queryParam("sessionKey", DEFAULT_SESSION_TOKEN_KEY)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    public void saveScore_validSalesforceCustomerId_returnsUnAuthorizedResponseStatus() throws Exception {

        when(gameService.saveScore(anyString(), anyInt(), anyInt())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                            .post("/123")
                            .queryParam("score", "987")
                            .queryParam("sessionKey", DEFAULT_SESSION_TOKEN_KEY)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized())
               .andReturn();
    }
}
