package com.ohmy.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Embeddable
@Schema(name = "Address", description = "Embedded address object containing user location details")
public class Address {

    @Schema(description = "Street address", example = "GV/Corts Catalanes 235")
    private String street;

    @Schema(description = "City username", example = "Barcelona")
    private String city;

    @Schema(description = "Postal code", example = "08001")
    private String zipcode;

    @Schema(description = "Country username", example = "Spain")
    private String country;
}