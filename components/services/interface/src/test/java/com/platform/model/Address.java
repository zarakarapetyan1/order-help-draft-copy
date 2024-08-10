package com.platform.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Address {
    private UUID addressId;
    private String country;
    private String city;
    private String street;
}
