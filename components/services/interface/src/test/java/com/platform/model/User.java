package com.platform.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID userId;


    private String name;


    private String surname;

    private String email;
    private String password;

    private Address address;
}
