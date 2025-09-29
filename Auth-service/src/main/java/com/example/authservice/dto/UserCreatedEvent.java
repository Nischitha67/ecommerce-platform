	package com.example.authservice.dto;public class UserCreatedEvent {
	
    private String username;
    private String email;
    private String passwordHash; // optional if Auth needs it
}
