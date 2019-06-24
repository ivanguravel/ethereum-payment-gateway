package com.crypto.dto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TransactionDto {
    String from;
    String to;
    double amount;
}
