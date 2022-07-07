package com.king.assessment.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.king.assessment.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.time.Duration.ofMinutes;
import static java.time.LocalTime.now;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testFindBySessionToken_validSessionToken_validatedSuccessfully() {
        User user = userRepository.findBySessionToken("8ef20062-714d-4f4e-9384-e0d443abcf23");

        assertThat(user, notNullValue());
        assertThat(user.getSessionToken(), is("8ef20062-714d-4f4e-9384-e0d443abcf23"));
        assertThat(user.getId(), is(1234));
        assertThat(user.getSessionTokenExpiry(), is(LocalTime.parse("13:15:26")));

    }

    @Test
    void testFindBySessionToken_inValidSessionToken_validatedSuccessfully() {
        User user = userRepository.findBySessionToken("1234");

        assertThat(user, nullValue());

    }

    @Test
    void testFindById_validUserId_validatedSuccessfully() {
        Optional<User> byId = userRepository.findById(1234);

        assertTrue(byId.isPresent());
        assertThat(byId.get().getSessionToken(), is("8ef20062-714d-4f4e-9384-e0d443abcf23"));
        assertThat(byId.get().getId(), is(1234));
        assertThat(byId.get().getSessionTokenExpiry(), is(LocalTime.parse("13:15:26")));
    }

    @Test
    void testFindById_inValidUserId_validatedSuccessfully() {
        Optional<User> byId = userRepository.findById(123);

        assertFalse(byId.isPresent());
    }

    @Test
    void testSave_validUser_savedSuccessfully() {

        User user = new User();
        user.setId(123);
        user.setSessionToken(randomUUID().toString());
        user.setSessionTokenExpiry(now().plus(ofMinutes(10)));

        User savedUser = userRepository.save(user);
        Optional<User> byId = userRepository.findById(savedUser.getId());

        assertTrue(byId.isPresent());
        assertThat(byId.get().getSessionToken(), is(savedUser.getSessionToken()));
        assertThat(byId.get().getId(), is(savedUser.getId()));
        assertThat(byId.get().getSessionTokenExpiry(), is(savedUser.getSessionTokenExpiry()));
    }

    @Test
    void testFindAll_returnListOfUsersSuccessfully() {

        User user = new User();
        user.setId(123);
        user.setSessionToken(randomUUID().toString());
        user.setSessionTokenExpiry(now().plus(ofMinutes(10)));

        userRepository.save(user);

        List<User> users = userRepository.findAll();

        assertFalse(users.isEmpty());
        assertThat(users, hasSize(2));
        assertThat(users.get(0).getId(), is(123));
        assertThat(users.get(1).getId(), is(1234));
    }
}
