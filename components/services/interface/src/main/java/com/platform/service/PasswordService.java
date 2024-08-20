package com.platform.service;

import java.util.UUID;

public interface PasswordService {
    void changePassword(UUID userId, String newPassword);
}
