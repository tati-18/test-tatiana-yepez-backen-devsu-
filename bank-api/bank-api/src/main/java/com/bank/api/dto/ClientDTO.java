package com.bank.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Minimum age is 18")
    private Integer age;

    @NotBlank(message = "Identification is required")
    private String identification;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "ClientId is required")
    private String clientId;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Status is required")
    private Boolean status;

}