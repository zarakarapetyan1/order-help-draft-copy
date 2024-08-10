package com.platform.controller;

import com.platform.model.LoginRequest;
import com.platform.service.TokenService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Hidden

public class AuthenticationController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestBody LoginRequest loginRequest) {

        return tokenService.getToken(loginRequest);
    }
}
