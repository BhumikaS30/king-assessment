package com.king.assessment.service;

import com.king.assessment.model.Level;
import com.king.assessment.model.LevelId;
import com.king.assessment.model.User;
import com.king.assessment.repository.LevelRepository;
import com.king.assessment.repository.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.time.Duration.ofMinutes;
import static java.time.LocalTime.now;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GameService.class)
public class GameServiceTest {

    @Autowired
    GameService gameService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    LevelRepository levelRepository;

    private static User testUser;
    private static User testUserWithNoLevels;

    private static final String DEFAULT_SESSION_TOKEN_KEY = "8ef20062-714d-4f4e-9384-e0d443abcf23";

    @BeforeAll
    public static void setUp(){
        testUser = new User();
        testUser.setId(123);
        testUser.setSessionToken(DEFAULT_SESSION_TOKEN_KEY);
        testUser.setSessionTokenExpiry(now().plus(ofMinutes(10)));

        LevelId testLevelId = new LevelId();
        testLevelId.setLevelId(1234);
        testLevelId.setUserId(123);

        Level testLevel = new Level();
        testLevel.setLevelId(testLevelId);
        testLevel.setScore(2221);
        testLevel.setUser(testUser);

        testUserWithNoLevels = new User();
        testUserWithNoLevels.setId(124);
        testUserWithNoLevels.setSessionToken(DEFAULT_SESSION_TOKEN_KEY);
        testUserWithNoLevels.setSessionTokenExpiry(now().plus(ofMinutes(10)));

        testUser.setLevels(of(testLevel));
    }

    @Test
    void testLogin_validRequest_sessionTokenCreatedSuccessfully() {

        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String sessionToken = gameService.login(123);

        assertThat(sessionToken, notNullValue());
        assertThat(sessionToken, is(DEFAULT_SESSION_TOKEN_KEY));
    }

    @Test
    void saveScore_validRequest_scoreSavedSuccessfully() {

        when(userRepository.findBySessionToken(DEFAULT_SESSION_TOKEN_KEY)).thenReturn(testUser);

        Boolean isScoreSaved = gameService.saveScore(DEFAULT_SESSION_TOKEN_KEY, 123, 1234);

        assertTrue(isScoreSaved);
    }

    @Test
    void saveScore_userWithNoLevels_scoreSavedSuccessfully() {

        when(userRepository.findBySessionToken(DEFAULT_SESSION_TOKEN_KEY)).thenReturn(testUserWithNoLevels);

        Boolean isScoreSaved = gameService.saveScore(DEFAULT_SESSION_TOKEN_KEY, 123, 1234);

        assertTrue(isScoreSaved);
    }

    @Test
    void getHighScoreList_validLevelId_highScoreListReturnedSuccessfully() {

        when(userRepository.findAll()).thenReturn(of(testUser));

        String highScoreList = gameService.getHighScoreList(1234);

        assertThat(highScoreList, notNullValue());
        assertThat(highScoreList, is("123=2221"));
    }

    @Test
    void getHighScoreList_validLevelIdAndOneUserWithNoScore_highScoreListReturnedSuccessfully() {

        when(userRepository.findAll()).thenReturn(of(testUser, testUserWithNoLevels));

        String highScoreList = gameService.getHighScoreList(1234);

        assertThat(highScoreList, notNullValue());
        assertThat(highScoreList, is("123=2221,124="));
    }
}
