package com.example.authservice.dto;

import java.util.Map;

public class UserUpdatedEvent {
    private String username;
    private String email;
    private Map<String, Object> updatedFields;
}
