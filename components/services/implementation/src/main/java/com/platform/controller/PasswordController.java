package com.platform.controller;

import com.platform.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> changePassword(@PathVariable UUID userId, @RequestBody String newPassword) {
        passwordService.changePassword(userId, newPassword);
        return ResponseEntity.ok().build();
    }
}
