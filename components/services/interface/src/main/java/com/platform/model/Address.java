package com.platform.model;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor
public class Address {

    @Hidden
    private UUID addressId;
    @Schema(example = "Armenia")
    private String country;
    @Schema(example = "Yerevan")
    private String city;
    @Schema(example = "Margaryan45/23")
    private String street;

}
