package com.automa.dto.payment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotNull(message = "Please enter a valid number of credits.")
    @Min(value = 50, message = "Minimum 50 credits required")
    @Max(value = 500, message = "Maximum 500 credits allowed")
    private Integer credits;
}
