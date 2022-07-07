package com.king.assessment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.king.assessment.exception.UnAuthorizedException;
import com.king.assessment.model.Level;
import com.king.assessment.model.LevelId;
import com.king.assessment.model.User;
import com.king.assessment.repository.LevelRepository;
import com.king.assessment.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.time.Duration.ofMinutes;
import static java.time.LocalTime.now;
import static java.util.Objects.isNull;
import static java.util.UUID.randomUUID;

@Service
public class GameService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LevelRepository levelRepository;

    public String login(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return getOrSaveUser(userId, user);
    }

    private String getOrSaveUser(Integer userId, Optional<User> user) {
        if (user.isPresent()) {
            if (now().isBefore(user.get().getSessionTokenExpiry())) {
                return user.get().getSessionToken();
            }
            else {
                throw new UnAuthorizedException("Session Token Expired!");
            }
        }
        else {
            User newUSer = new User();
            newUSer.setId(userId);
            newUSer.setSessionToken(randomUUID().toString());
            newUSer.setSessionTokenExpiry(now().plus(ofMinutes(10)));
            User savedUser = userRepository.save(newUSer);
            return savedUser.getSessionToken();
        }
    }

    public Boolean saveScore(String sessionKey, Integer score, Integer levelId) {
        User user = userRepository.findBySessionToken(sessionKey);
        if (now().isBefore(user.getSessionTokenExpiry())) {
            List<Level> levels = getAndSaveLevels(score, levelId, user);
            user.setLevels(levels);
            userRepository.save(user);
            return true;
        }
        else {
            return false;
        }
    }

    private List<Level> getAndSaveLevels(Integer score, Integer levelId, User user) {
        Level level;
        List<Level> levels = user.getLevels();
        if (!levels.isEmpty()) {
            levels.forEach(level1 -> level1.setScore(level1.getLevelId().getLevelId().equals(levelId) ?
                                                     level1.getScore() + score :
                                                     score));
        }
        else {
            levels = new ArrayList<>();
            LevelId levelId1 = new LevelId();
            levelId1.setLevelId(levelId);
            levelId1.setUserId(user.getId());
            level = levelRepository.findByLevelId(levelId1);
            if (isNull(level)) {
                level = new Level();
                level.setLevelId(levelId1);
                level.setScore(score);
                level.setUser(user);
                levels.add(level);
            }
            else {
                level.setScore(level.getScore() + score);
            }
            levelRepository.save(level);
        }
        return levels;
    }

    public String getHighScoreList(Integer levelId) {
        List<User> users = userRepository.findAll();
        List<String> result = new ArrayList<>();
        users.forEach(user -> {
            Optional<Level> levelById = user.getLevels().
                                            stream()
                                            .filter(level -> level.getLevelId().getLevelId().equals(levelId)).findAny();

            result.add(levelById.map(level -> user.getId() + "=" + level.getScore())
                                .orElseGet(() -> user.getId() + "=" + ""));
        });

        return String.join(",", result);
    }
}
