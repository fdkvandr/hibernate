package com.corp.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentFilter {

    String firstName;
    String lastName;
}
