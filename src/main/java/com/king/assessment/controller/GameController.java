package com.king.assessment.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.king.assessment.model.SaveScoreRequest;
import com.king.assessment.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @ApiOperation(value = "Login in to the game")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Session Token returned successfully", response = String.class)
    })
    @GetMapping(value = "{userId}/login")
    public ResponseEntity<String> login( @NotNull(message = "'userId' is required") @PathVariable Integer userId) {
        return ResponseEntity.ok().body(gameService.login(userId));
    }

    @ApiOperation(value = "Post a user's score to a level")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User's score saved to a level successfully", response = String.class)
    })
    @PostMapping(value = "{levelId}/score")
    public ResponseEntity saveScore(
        @NotNull(message = "'levelId' is required") @PathVariable Integer levelId,
        @NotNull(message = "'score' is required") @RequestBody @Valid SaveScoreRequest saveScoreRequest,
        @NotNull(message = "'sessionKey' is required") @RequestParam(value = "sessionKey") String sessionKey
    ) {
        Boolean validRequest = gameService.saveScore(sessionKey, saveScoreRequest.getScore(), levelId);
        if (validRequest) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ApiOperation(value = "Returns high score list by Level Id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "High score list returned successfully", response = String.class)
    })
    @GetMapping(value = "/{levelId}/highscorelist")
    public String getHighScoreList(@NotNull(message = "'levelId' is required")@PathVariable Integer levelId) {
        return gameService.getHighScoreList(levelId);
    }
}
